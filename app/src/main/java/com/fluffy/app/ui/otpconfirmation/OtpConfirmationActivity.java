package com.fluffy.app.ui.otpconfirmation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import  com.fluffy.app.databinding.ActivityOtpConfirmationBinding;
import com.fluffy.app.ui.changepw.ChangePasswordActivity;

public class OtpConfirmationActivity extends AppCompatActivity{
    private ActivityOtpConfirmationBinding binding;
    private String generatedOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        generatedOtp = getIntent().getStringExtra("otp");

        addEvents();
    }

    private void addEvents() {
        binding.btnVerifyOtp.setOnClickListener(v -> {
            String enteredOtp = binding.edtOtp1.getText().toString() +
                    binding.edtOtp2.getText().toString() +
                    binding.edtOtp3.getText().toString() +
                    binding.edtOtp4.getText().toString();

            if (enteredOtp.equals(generatedOtp)) {
                Toast.makeText(OtpConfirmationActivity.this, "Mã OTP hợp lệ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpConfirmationActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Nếu mã OTP sai
                Toast.makeText(OtpConfirmationActivity.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


