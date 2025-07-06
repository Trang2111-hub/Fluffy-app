package com.fluffy.app.ui.signup;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.fluffy.app.databinding.ActivitySignUpBinding;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        addEvents();
    }

    private void addEvents() {
        binding.btnSignUp.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString();
            String password = binding.edtPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();

            if (email.isEmpty()) {
                binding.edtEmail.setError("Email là bắt buộc *");
                binding.edtEmail.requestFocus();
            } else if (!isValidEmail(email)) {
                binding.edtEmail.setError("Email không hợp lệ");
                binding.edtEmail.requestFocus();
            } else if (password.isEmpty()) {
                binding.edtPassword.setError("Mật khẩu là bắt buộc *");
                binding.edtPassword.requestFocus();
            } else if (!isValidPassword(password)) {
                binding.edtPassword.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
                binding.edtPassword.requestFocus();
            } else if (!password.equals(confirmPassword)) {
                binding.edtConfirmPassword.setError("Mật khẩu không khớp");
                binding.edtConfirmPassword.requestFocus();
            } else {
                checkEmailExists(email);
            }
        });

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

        binding.edtConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtConfirmPassword.setBackgroundResource(R.drawable.edittext_bg_focus);
            } else {
                binding.edtConfirmPassword.setBackgroundResource(R.drawable.edittext_bg);
            }
        });

        binding.signIn.setOnClickListener(v -> finish());
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=<>?])[A-Za-z\\d!@#$%^&*()_+=<>?]{8,50}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(password).matches();
    }

    private void checkEmailExists(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                if (result != null && result.getSignInMethods().size() > 0) {
                    Toast.makeText(SignUpActivity.this, "Email đã được đăng ký!", Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(email);
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Lỗi khi kiểm tra email. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(String email) {
        String password = binding.edtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        showSuccessDialog();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        dialogView.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // MainActivity là màn hình đăng nhập
            startActivity(intent);
            finish();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
