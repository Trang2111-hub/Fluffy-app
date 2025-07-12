package com.fluffy.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fluffy.app.ui.order.OrderManagementFragment;
import com.fluffy.app.ui.favorite_product.FavoriteProductFragment;
import com.fluffy.app.ui.homepage.HomePageFragment;
import com.fluffy.app.ui.policy.ChinhSachFragment;
import com.fluffy.app.ui.product.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FavoriteProductFragment favoriteFragment;
    private OrderManagementFragment orderManagementFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        favoriteFragment = new FavoriteProductFragment();
        orderManagementFragment = new OrderManagementFragment(); // Khởi tạo instance

        // Gán listener cho imgMenu
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                View menuIcon = v.findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(view -> drawerLayout.openDrawer(navigationView));
                }
            }
        }, true);

        // Xử lý click menu icon trong header khi backstack thay đổi
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
                        .replace(R.id.fragment_container, new HomePageFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_policy) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ChinhSachFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_product) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProductFragment())
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

        // Xử lý Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomePageFragment())
                        .commit();
                return true;
            } else if (id == R.id.favorites) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, favoriteFragment)
                        .commit();
                return true;
            } else if (id == R.id.notifications) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, orderManagementFragment)
                        .commit();
                return true;
            } else if (id == R.id.account) {
                Intent intent = new Intent(this, com.fluffy.app.ui.profilesetting.ProfilesettingActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomePageFragment())
                    .commit();
        }
    }

    public FavoriteProductFragment getFavoriteFragment() {
        return favoriteFragment;
    }

    public OrderManagementFragment getOrderManagementFragment() {
        return orderManagementFragment;
    }
}