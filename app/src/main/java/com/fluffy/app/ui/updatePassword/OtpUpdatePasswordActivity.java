package com.fluffy.app.ui.updatePassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityOtpUpdatePasswordBinding;

public class OtpUpdatePasswordActivity extends AppCompatActivity {
    ActivityOtpUpdatePasswordBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOtpUpdatePasswordBinding binding = ActivityOtpUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String otp = OTPGenerator.generateOTP(4);
        sendOtpToEmail("recipient-email@example.com", otp);

        binding.tvResendOtp.setOnClickListener(v -> {
            // Khi người dùng yêu cầu gửi lại OTP
            sendOtpToEmail("recipient-email@example.com", otp);
            Toast.makeText(this, "Mã OTP đã được gửi lại", Toast.LENGTH_SHORT).show();
        });

        binding.btnConfirm.setOnClickListener(v -> {
            String userOtp = binding.otp1.getText().toString() +
                    binding.otp2.getText().toString() +
                    binding.otp3.getText().toString() +
                    binding.otp4.getText().toString();

            if (TextUtils.isEmpty(userOtp) || userOtp.length() != 4) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userOtp.equals(otp)) {
                // OTP hợp lệ, chuyển đến màn hình tiếp theo
                navigateToUpdatePassword();
            } else {
                Toast.makeText(this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                binding.otp1.setText("");
                binding.otp2.setText("");
                binding.otp3.setText("");
                binding.otp4.setText("");
                binding.otp1.requestFocus();
            }
        });
    }

    private void sendOtpToEmail(String email, String otp) {
        EmailSender.sendEmail(email, otp);  // Gửi OTP qua email
    }

    private void navigateToUpdatePassword() {
        // Điều hướng đến trang đổi mật khẩu
        // Intent intent = new Intent(this, UpdatePasswordActivity.class);
        // startActivity(intent);
        Toast.makeText(this, "Mã OTP hợp lệ, chuyển đến trang thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
    }
}