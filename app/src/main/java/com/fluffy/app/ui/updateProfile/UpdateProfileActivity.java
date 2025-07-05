package com.fluffy.app.ui.updateProfile;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityUpdateProfileBinding;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable view binding
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Thông tin cá nhân");

        loadUserProfile();

        binding.imgCalendar.setOnClickListener(v -> showDatePicker());

        binding.btnSave.setOnClickListener(v -> showConfirmationDialog());

        binding.btnCancel.setOnClickListener(v -> finish());
    }

    private void loadUserProfile() {
        // Giả sử đây là dữ liệu người dùng
        String savedName = "Trần Thị Thùy Trang";
        String savedDob = "21/02/2008";
        String savedPhone = "0762855298";
        String savedEmail = "trangtran@gmail.com";

        // Hiển thị thông tin lên các EditText
        binding.edtName.setText(savedName);
        binding.edtDob.setText(savedDob);
        binding.edtPhone.setText(savedPhone);
        binding.edtEmail.setText(savedEmail);
    }

    private void showDatePicker() {
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(this);

        // Đảm bảo layout đúng
        dialog.setContentView(R.layout.dialog_confirmation_in4);

        // Lấy các button trong dialog
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Set hành động cho nút Lưu
        btnSave.setOnClickListener(v -> {
            validateAndSaveProfile();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void validateAndSaveProfile() {
        String name = binding.edtName.getText().toString();
        String email = binding.edtEmail.getText().toString();
        String phone = binding.edtPhone.getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.edtName.setError("Họ và tên không được để trống");
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email không hợp lệ");
            return;
        }

        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches()) {
            binding.edtPhone.setError("Số điện thoại không hợp lệ");
            return;
        }

        saveUserProfile(name, email, phone);
    }

    private void saveUserProfile(String name, String email, String phone) {
        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}
