package com.fluffy.app.ui.updateProfile;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityUpdateProfileBinding;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Thông tin cá nhân");

        loadUserProfile();

        binding.imgCalendar.setOnClickListener(v -> showDatePicker());
        binding.btnSave.setOnClickListener(v -> showConfirmationDialog());
        binding.btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String name = sharedPreferences.getString("name", null);
        String dob = sharedPreferences.getString("dob", null);
        String phone = sharedPreferences.getString("phone", null);
        String email = sharedPreferences.getString("email", null);

        if (name == null && dob == null && phone == null && email == null) {
            name = "Trịnh Tiến Đạt Khoa";
            dob = "21/02/2008";
            phone = "0762855298";
            email = "Khoatrinh@gmail.com";

            editor.putString("name", name);
            editor.putString("dob", dob);
            editor.putString("phone", phone);
            editor.putString("email", email);
            editor.apply();

            Log.d("UpdateProfileDebug", "Initialized default - Name: " + name + ", DOB: " + dob + ", Phone: " + phone + ", Email: " + email);
        }

        binding.edtName.setText(name);
        binding.edtDob.setText(dob);
        binding.edtPhone.setText(phone);
        binding.edtEmail.setText(email);
    }

    private void showDatePicker() {
        // Implement nếu cần
        Toast.makeText(this, "Date picker not implemented", Toast.LENGTH_SHORT).show();
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirmation_in4);

        // Tìm nút Save trong dialog
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (btnSave == null) {
            Log.e("UpdateProfileDebug", "btnSave not found in dialog!");
            return;
        }

        btnSave.setOnClickListener(v -> {
            validateAndSaveProfile();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void validateAndSaveProfile() {
        String name = binding.edtName.getText().toString().trim();
        String dob = binding.edtDob.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();

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

        saveUserProfile(name, dob, email, phone);
    }

    private void saveUserProfile(String name, String dob, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("dob", dob);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.apply();

        Log.d("UpdateProfileDebug", "Saved - Name: " + name + ", DOB: " + dob + ", Phone: " + phone + ", Email: " + email);

        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }
}