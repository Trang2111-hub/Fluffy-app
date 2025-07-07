package com.fluffy.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Gán listener cho imgMenu
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(androidx.fragment.app.FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                View menuIcon = v.findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(view -> drawerLayout.openDrawer(navigationView));
                }
            }
        }, true);

        // Xử lý click menu icon trong header khi backstack thay đổi (vẫn giữ lại để đảm bảo)
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null && currentFragment.getView() != null) {
                View menuIcon = currentFragment.getView().findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
                }
            }
        });

        // Xử lý drawer_menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.homepage.HomePageFragment())
                        .addToBackStack(null)
                        .commit();
            }   else if (id == R.id.nav_policy) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.policy.ChinhSachFragment())
                        .addToBackStack(null)
                        .commit();
            }
            else if (id == R.id.nav_product) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.product.ProductFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_ve_fluffy) {
                Intent intent = new Intent(this, com.fluffy.app.ui.aboutfluffy.VeFluffyActivity.class);
                startActivity(intent);
                return true;
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // XỬ LÝ BOTTOM MENU
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.homepage.HomePageFragment())
                        .commit();
                return true;
            } else if (id == R.id.favorites) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.product.ProductFragment())
                        .commit();
                return true;
            } else if (id == R.id.notifications) {
                // Hiện tại chưa có Activity cho Thông báo, có thể bổ sung sau
                return true;
            } else if (id == R.id.account) {

                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new com.fluffy.app.ui.homepage.HomePageFragment())
                    .commit();
        }
    }
}