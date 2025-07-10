package com.fluffy.app.ui.updateProfile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> profileUpdateLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);

        // Khởi tạo ActivityResultLauncher
        profileUpdateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("ProfileDebug", "Profile updated, refreshing UI");
                        loadUserProfile(); // Làm mới giao diện
                    }
                });

        // Thiết lập tiêu đề
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Thông tin cá nhân");

        // Bắt sự kiện nút chỉnh sửa hồ sơ
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            profileUpdateLauncher.launch(intent);
        });

        // Liên kết mạng xã hội
        binding.btnLinkFacebook.setOnClickListener(v -> linkSocialAccount("Facebook"));
        binding.btnLinkGoogle.setOnClickListener(v -> linkSocialAccount("Google"));

        // Tải thông tin ban đầu
        loadUserProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ProfileDebug", "onResume called, refreshing UI");
        loadUserProfile(); // Làm mới dữ liệu mỗi khi quay lại
    }

    private void loadUserProfile() {
        String name = sharedPreferences.getString("name", "Trịnh Tiến Đạt Khoa");
        String dob = sharedPreferences.getString("dob", "21/02/2008 in ProfileActivity");
        String phone = sharedPreferences.getString("phone", "0762855298");
        String email = sharedPreferences.getString("email", "Khoatrinh@gmail.com");

        Log.d("ProfileDebug", "Loaded - Name: " + name + ", DOB: " + dob + ", Phone: " + phone + ", Email: " + email);

        // Kiểm tra null cho binding
        if (binding.txtName == null || binding.txtDob == null || binding.txtPhone == null || binding.txtEmail == null) {
            Log.e("ProfileDebug", "One or more binding views are null!");
            return;
        }

        binding.txtName.setText(name);
        binding.txtDob.setText(dob);
        binding.txtPhone.setText(phone);
        binding.txtEmail.setText(email);
    }

    private void linkSocialAccount(String socialMedia) {
        if (socialMedia.equals("Facebook")) {
            binding.btnLinkFacebook.setText("Đã liên kết");
            binding.btnLinkFacebook.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.btnLinkFacebook.setTextColor(getResources().getColor(R.color.white));
        } else if (socialMedia.equals("Google")) {
            binding.btnLinkGoogle.setText("Đã liên kết");
            binding.btnLinkGoogle.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.btnLinkGoogle.setTextColor(getResources().getColor(R.color.white));
        }

        saveSocialLinkStatus(socialMedia);
        Toast.makeText(this, socialMedia + " liên kết thành công", Toast.LENGTH_SHORT).show();
    }

    private void saveSocialLinkStatus(String socialMedia) {
        // Lưu trạng thái liên kết nếu cần
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("linked_" + socialMedia, true);
        editor.apply();
    }
}