package com.fluffy.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.fluffy.app.ui.forgotpw.ForgotPasswordActivity;
import com.fluffy.app.ui.profilesetting.ProfilesettingActivity;
import com.fluffy.app.ui.signup.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.fluffy.app.databinding.ActivityLoginBinding;

import android.util.Patterns;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        ImageView imgBack = findViewById(R.id.imgBack);
        TextView txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText("Đăng nhập");

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ProfilesettingActivity.class);
            finish();
        });

        addEvents();
    }



    private void addEvents() {
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString();
            String password = binding.edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đúng định dạng email", Toast.LENGTH_SHORT).show();
            } else {
                loginWithEmailPassword(email, password);
            }
        });

        binding.signUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        binding.forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        binding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtEmail.setBackgroundResource(R.drawable.edittext_bg_focus);
            } else {
                binding.edtEmail.setBackgroundResource(R.drawable.edittext_bg);
            }
        });

        binding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtPassword.setBackgroundResource(R.drawable.edittext_bg_focus);
            } else {
                binding.edtPassword.setBackgroundResource(R.drawable.edittext_bg);
            }
        });

        binding.imgEyePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.imgEyePassword.setImageResource(R.drawable.ic_eye_close);
                isPasswordVisible = false;
            } else {
                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.imgEyePassword.setImageResource(R.drawable.ic_eye_open);
                isPasswordVisible = true;
            }
            binding.edtPassword.setSelection(binding.edtPassword.getText().length()); // giữ nguyên vị trí con trỏ
        });

    }

    private void loginWithEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, ProfilesettingActivity.class);
                        // hardcode dữ liệu profile cho demo
                        intent.putExtra("name", "Trịnh Tiến Đạt Khoa");
                        intent.putExtra("phone", "0813849476");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
