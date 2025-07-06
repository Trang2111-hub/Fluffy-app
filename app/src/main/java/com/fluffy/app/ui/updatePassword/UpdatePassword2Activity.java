package com.fluffy.app.ui.updatePassword;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityUpdatePassword2Binding;

public class UpdatePassword2Activity extends AppCompatActivity {
    private ActivityUpdatePassword2Binding binding;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePassword2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPasswordVisibilityToggles();
        setupButtonListeners();
    }

    private void setupPasswordVisibilityToggles() {
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
            String newPassword = binding.edtNewPassword.getText().toString().trim();
            String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
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
}