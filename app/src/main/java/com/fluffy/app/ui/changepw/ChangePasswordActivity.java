package com.fluffy.app.ui.changepw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.ui.forgotpw.ForgotPasswordActivity;
import com.fluffy.app.ui.login.LoginActivity;
import com.fluffy.app.ui.otpconfirmation.OtpConfirmationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.fluffy.app.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

        ImageView imgBack = findViewById(R.id.imgBack);
        TextView txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText("Thay đổi mật khẩu");

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePasswordActivity.this, OtpConfirmationActivity.class);
            finish();
        });

    }

    private void addEvents() {
        binding.btnChangePassword.setOnClickListener(v -> {
            String currentPassword = binding.edtNewPassword.getText().toString(); // Mật khẩu cũ
            String newPassword = binding.edtNewPassword.getText().toString(); // Mật khẩu mới
            String confirmPassword = binding.edtConfirmPassword.getText().toString(); // Xác nhận mật khẩu mới

            Log.d("ChangePassword", "Current Password: " + currentPassword);
            Log.d("ChangePassword", "New Password: " + newPassword);
            Log.d("ChangePassword", "Confirm Password: " + confirmPassword);

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            } else if (newPassword.equals(currentPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không được giống mật khẩu cũ", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("ChangePassword", "Re-authentication successful");
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Log.d("ChangePassword", "Error: " + task1.getException().getMessage());
                                                    Toast.makeText(ChangePasswordActivity.this, "Không thể thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Log.d("ChangePassword", "Re-authentication failed: " + task.getException().getMessage());
                                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
