package com.fluffy.app.ui.updatePassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityUpdatePasswordBinding;

public class UpdatePasswordActivity extends AppCompatActivity {
    private ActivityUpdatePasswordBinding binding;
    private boolean isCurrentPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private static final String DUMMY_PASSWORD = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPasswordVisibilityToggles();
        setupButtonListeners();
        setupForgotPasswordClick();
    }

    private void setupPasswordVisibilityToggles() {
        binding.ivShowCurrentPassword.setOnClickListener(v -> {
            isCurrentPasswordVisible = !isCurrentPasswordVisible;
            binding.edtCurrentPassword.setTransformationMethod(
                    isCurrentPasswordVisible ? SingleLineTransformationMethod.getInstance()
                            : PasswordTransformationMethod.getInstance());
            binding.ivShowCurrentPassword.setImageResource(
                    isCurrentPasswordVisible ? R.drawable.show_ic : R.drawable.unshow_ic);
            binding.edtCurrentPassword.setSelection(binding.edtCurrentPassword.getText().length());
        });

        binding.ivShowNewPassword.setOnClickListener(v -> {
            isNewPasswordVisible = !isNewPasswordVisible;
            binding.edtNewPassword.setTransformationMethod(
                    isNewPasswordVisible ? SingleLineTransformationMethod.getInstance()
                            : PasswordTransformationMethod.getInstance());
            binding.ivShowNewPassword.setImageResource(
                    isNewPasswordVisible ? R.drawable.show_ic : R.drawable.unshow_ic);
            binding.edtNewPassword.setSelection(binding.edtNewPassword.getText().length());
        });

        binding.ivShowConfirmPassword.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            binding.edtConfirmPassword.setTransformationMethod(
                    isConfirmPasswordVisible ? SingleLineTransformationMethod.getInstance()
                            : PasswordTransformationMethod.getInstance());
            binding.ivShowConfirmPassword.setImageResource(
                    isConfirmPasswordVisible ? R.drawable.show_ic : R.drawable.unshow_ic);
            binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.getText().length());
        });
    }

    private void setupButtonListeners() {
        binding.btnCancel.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> {
            String currentPassword = binding.edtCurrentPassword.getText().toString().trim();
            String newPassword = binding.edtNewPassword.getText().toString().trim();
            String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!currentPassword.equals(DUMMY_PASSWORD)) {
                Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupForgotPasswordClick() {
        binding.tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(UpdatePasswordActivity.this, OtpUpdatePasswordActivity.class);
            startActivity(intent);
        });
    }
}