package com.fluffy.app.ui.updatePassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.databinding.ActivityUpdatePassword2Binding;

public class UpdatePassword2Activity extends AppCompatActivity {
    ActivityUpdatePassword2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePassword2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(v -> {
            String newPassword = binding.edtNewPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới và xác nhận mật khẩu mới", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới và mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu mật khẩu mới
            saveNewPassword(newPassword);
        });
    }

    private void saveNewPassword(String newPassword) {

        Toast.makeText(this, "Mật khẩu đã được thay đổi!", Toast.LENGTH_SHORT).show();
    }
}