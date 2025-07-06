package com.fluffy.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fluffy.app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
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