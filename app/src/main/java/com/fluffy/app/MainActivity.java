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

        // Xử lý click menu icon trong header
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null && currentFragment.getView() != null) {
                View menuIcon = currentFragment.getView().findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_policy) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.fluffy.app.ui.policy.ChinhSachFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_ve_fluffy) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new com.fluffy.app.ui.about.VeFluffyFragment())
//                        .addToBackStack(null)
//                        .commit();
            } // Thêm các case khác nếu cần
            drawerLayout.closeDrawers();
            return true;
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                // Ở lại MainActivity (Trang chủ)
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
                Intent intent = new Intent(this, com.fluffy.app.ui.policy.ChinhSachActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}