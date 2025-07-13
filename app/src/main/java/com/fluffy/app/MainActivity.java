package com.fluffy.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fluffy.app.ui.cart.CartFragment;
import com.fluffy.app.ui.order.OrderManagementFragment;
import com.fluffy.app.ui.favorite_product.FavoriteProductFragment;
import com.fluffy.app.ui.homepage.HomePageFragment;
import com.fluffy.app.ui.policy.ChinhSachFragment;
import com.fluffy.app.ui.product.ProductFragment;
import com.fluffy.app.ui.profilesetting.ProfilesettingFragment;
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
        orderManagementFragment = new OrderManagementFragment();

        // Xử lý imgMenu + imgCart trong tất cả fragment header
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                // Xử lý menu mở drawer
                View menuIcon = v.findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(view -> drawerLayout.openDrawer(navigationView));
                }

                // Xử lý icon mở giỏ hàng
                View cartIcon = v.findViewById(R.id.imgCart);
                if (cartIcon != null) {
                    cartIcon.setOnClickListener(view -> {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new CartFragment())
                                .addToBackStack(null)
                                .commit();
                    });
                }
            }
        }, true);

        // Khi thay đổi fragment (backstack thay đổi), set lại listener cho menu & cart
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null && currentFragment.getView() != null) {
                View menuIcon = currentFragment.getView().findViewById(R.id.imgMenu);
                if (menuIcon != null) {
                    menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
                }

                View cartIcon = currentFragment.getView().findViewById(R.id.imgCart);
                if (cartIcon != null) {
                    cartIcon.setOnClickListener(v -> {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new CartFragment())
                                .addToBackStack(null)
                                .commit();
                    });
                }
            }
        });

        // Xử lý drawer menu
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

        // Xử lý bottom nav
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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfilesettingFragment())
                        .commit();
                return true;
            }
            return false;
        });

        // Mở HomePage mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomePageFragment())
                    .commit();
        }

        // Xử lý intent mở account
        if (getIntent().hasExtra("openAccount")) {
            bottomNavigationView.setSelectedItemId(R.id.account);
        }
    }

    public FavoriteProductFragment getFavoriteFragment() {
        return favoriteFragment;
    }

    public OrderManagementFragment getOrderManagementFragment() {
        return orderManagementFragment;
    }
}
