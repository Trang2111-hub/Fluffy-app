package com.fluffy.app.ui.updateProfile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityProfileBinding;
import com.fluffy.app.ui.updateProfile.UpdateProfileActivity;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);

        // Thiết lập tiêu đề
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Thông tin cá nhân");

        // Bắt sự kiện nút chỉnh sửa hồ sơ
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });

        // Liên kết mạng xã hội
        binding.btnLinkFacebook.setOnClickListener(v -> linkSocialAccount("Facebook"));
        binding.btnLinkGoogle.setOnClickListener(v -> linkSocialAccount("Google"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void loadUserProfile() {
        String name = sharedPreferences.getString("name", "Chưa có tên");
        String email = sharedPreferences.getString("email", "Chưa có email");
        String phone = sharedPreferences.getString("phone", "Chưa có SĐT");
        String dob = sharedPreferences.getString("dob", "Chưa có ngày sinh");

        // Gán vào binding
        binding.txtName.setText(name);
        binding.txtEmail.setText(email);
        binding.txtPhone.setText(phone);
        binding.txtDob.setText(dob);
    }

    private void linkSocialAccount(String socialMedia) {
        if (socialMedia.equals("Facebook")) {
            binding.btnLinkFacebook.setText("Đã liên kết");
            binding.btnLinkFacebook.setBackgroundColor(getResources().getColor(com.fluffy.app.R.color.primary));
            binding.btnLinkFacebook.setTextColor(getResources().getColor(com.fluffy.app.R.color.white));
        } else if (socialMedia.equals("Google")) {
            binding.btnLinkGoogle.setText("Đã liên kết");
            binding.btnLinkGoogle.setBackgroundColor(getResources().getColor(com.fluffy.app.R.color.primary));
            binding.btnLinkGoogle.setTextColor(getResources().getColor(com.fluffy.app.R.color.white));
        }

        saveSocialLinkStatus(socialMedia);
        Toast.makeText(this, socialMedia + " liên kết thành công", Toast.LENGTH_SHORT).show();
    }

    private void saveSocialLinkStatus(String socialMedia) {
        // Bạn có thể lưu trạng thái liên kết vào SharedPreferences nếu muốn
    }
}
