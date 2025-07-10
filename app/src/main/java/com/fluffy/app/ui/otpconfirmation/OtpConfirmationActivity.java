package com.fluffy.app.ui.otpconfirmation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityOtpConfirmationBinding;
import com.fluffy.app.ui.changepw.ChangePasswordActivity;
import com.fluffy.app.ui.forgotpw.ForgotPasswordActivity;
import com.fluffy.app.ui.login.LoginActivity;

public class OtpConfirmationActivity extends AppCompatActivity {

    private ActivityOtpConfirmationBinding binding;
    private String generatedOtp; // OTP nhận được từ ForgotPasswordActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ImageView imgBack = findViewById(R.id.imgBack);
        TextView txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText("Xác nhận OTP");

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(OtpConfirmationActivity.this, ForgotPasswordActivity.class);
            finish();
        });

        generatedOtp = getIntent().getStringExtra("otp");

        codeFocus();

        // Sự kiện nút xác nhận
        binding.btnVerifyOtp.setOnClickListener(v -> {
            String otpInput = binding.edtOtp1.getText().toString()
                    + binding.edtOtp2.getText().toString()
                    + binding.edtOtp3.getText().toString()
                    + binding.edtOtp4.getText().toString();

            if (otpInput.length() < 4) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (otpInput.equals(generatedOtp)) {
                Toast.makeText(this, "Mã OTP hợp lệ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
            }
        });

        // Gửi lại OTP
        binding.resendOtp.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng gửi lại OTP chưa được kích hoạt!", Toast.LENGTH_SHORT).show();
        });
    }

    private void codeFocus() {
        binding.edtOtp1.addTextChangedListener(new GenericTextWatcher(binding.edtOtp2));
        binding.edtOtp2.addTextChangedListener(new GenericTextWatcher(binding.edtOtp3));
        binding.edtOtp3.addTextChangedListener(new GenericTextWatcher(binding.edtOtp4));
    }

    // class giúp tự động focus sang ô tiếp theo
    private class GenericTextWatcher implements TextWatcher {
        private final android.view.View nextView;

        GenericTextWatcher(android.view.View nextView) {
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty()) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }
}
