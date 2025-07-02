package com.fluffy.app.ui.account.order;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityOrderManagementBinding;

public class OrderManagementActivity extends AppCompatActivity {
    ActivityOrderManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}