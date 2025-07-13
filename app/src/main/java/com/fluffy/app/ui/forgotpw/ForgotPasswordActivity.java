package com.fluffy.app.ui.forgotpw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.MainActivity;
import com.fluffy.app.R;
import com.fluffy.app.ui.login.LoginActivity;
import com.fluffy.app.ui.otpconfirmation.OtpConfirmationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.fluffy.app.databinding.ActivityForgotPasswordBinding;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth mAuth;
    private String generatedOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        addEvents();

        ImageView imgBack = findViewById(R.id.imgBack);
        TextView txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText("Quên mật khẩu");

        imgBack.setOnClickListener(v -> {
            // Sửa lại: mở MainActivity với flag mở account
            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            intent.putExtra("openAccount", true);
            startActivity(intent);
            finish();
        });
    }

    private void addEvents() {
        binding.btnSendLink.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
            } else {
                sendPasswordResetEmail(email);
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgotPasswordActivity.this, "Kiểm tra email của bạn để thay đổi mật khẩu!", Toast.LENGTH_SHORT).show();
                sendOtpToEmail(email);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Có lỗi khi gửi email. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtpToEmail(String email) {
        generatedOtp = String.format("%04d", new Random().nextInt(10000));

        sendEmailWithOtp(email);

        Intent intent = new Intent(ForgotPasswordActivity.this, OtpConfirmationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("otp", generatedOtp);
        startActivity(intent);
    }

    private void sendEmailWithOtp(String email) {
        String subject = "Mã OTP xác nhận thay đổi mật khẩu";
        String body = "Mã OTP của bạn là: " + generatedOtp;
    }
}
