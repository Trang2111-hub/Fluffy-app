package com.fluffy.app.ui.updatePassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityOtpUpdatePasswordBinding;
import java.util.Random;

public class OtpUpdatePasswordActivity extends AppCompatActivity {
    private ActivityOtpUpdatePasswordBinding binding;
    private String generatedOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Đổi mật khẩu");

        generatedOtp = generateOtp();
        setupOtpInput();
        setupButtonListeners();
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            otp.append(random.nextInt(10));
        }
        Toast.makeText(this, "Mã OTP: " + otp.toString(), Toast.LENGTH_LONG).show();
        return otp.toString();
    }

    private void setupOtpInput() {
        binding.otp1.requestFocus();

        TextWatcher otpTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    if (binding.otp1.getText().hashCode() == s.hashCode()) {
                        binding.otp2.requestFocus();
                    } else if (binding.otp2.getText().hashCode() == s.hashCode()) {
                        binding.otp3.requestFocus();
                    } else if (binding.otp3.getText().hashCode() == s.hashCode()) {
                        binding.otp4.requestFocus();
                    }
                }
            }
        };

        binding.otp1.addTextChangedListener(otpTextWatcher);
        binding.otp2.addTextChangedListener(otpTextWatcher);
        binding.otp3.addTextChangedListener(otpTextWatcher);
        binding.otp4.addTextChangedListener(otpTextWatcher);
    }

    private void setupButtonListeners() {
        binding.btnConfirm.setOnClickListener(v -> {
            String enteredOtp = binding.otp1.getText().toString() +
                    binding.otp2.getText().toString() +
                    binding.otp3.getText().toString() +
                    binding.otp4.getText().toString();

            if (enteredOtp.length() != 4) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredOtp.equals(generatedOtp)) {
                Intent intent = new Intent(OtpUpdatePasswordActivity.this, UpdatePassword2Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvResendOtp.setOnClickListener(v -> {
            generatedOtp = generateOtp();
            Toast.makeText(this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
        });
    }
}