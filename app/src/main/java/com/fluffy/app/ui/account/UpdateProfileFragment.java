package com.fluffy.app.ui.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fluffy.app.R;
import com.fluffy.app.databinding.FragmentUpdateProfileBinding;

import java.util.Calendar;

public class UpdateProfileFragment extends Fragment {

    private FragmentUpdateProfileBinding binding;
    private String oldName, oldDob, oldPhone, oldEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);

        // Lưu thông tin cũ để reset khi nhấn Hủy
        oldName = binding.edtName.getText().toString();
        oldDob = binding.edtDob.getText().toString();
        oldPhone = binding.edtPhone.getText().toString();
        oldEmail = binding.edtEmail.getText().toString();

        // Xử lý sự kiện khi người dùng click vào EditText
        binding.edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtName.setText("");
            }
        });

        // Hiển thị DatePicker khi click vào ngày sinh
        binding.edtDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        binding.edtDob.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Validate số điện thoại khi thay đổi
        binding.edtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String phone = binding.edtPhone.getText().toString();
                if (!phone.matches("^0\\d{9}$")) {
                    binding.edtPhone.setError("Số điện thoại không hợp lệ");
                }
            }
        });

        // Validate email khi thay đổi
        binding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = binding.edtEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edtEmail.setError("Email không hợp lệ");
                }
            }
        });

        // Nút Hủy: reset lại thông tin cũ
        binding.btnCancel.setOnClickListener(v -> {
            binding.edtName.setText(oldName);
            binding.edtDob.setText(oldDob);
            binding.edtPhone.setText(oldPhone);
            binding.edtEmail.setText(oldEmail);
        });

        // Nút Lưu: xác nhận trước khi lưu
        binding.btnSave.setOnClickListener(v -> {
            View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);

            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
            Button btnSaveDialog = dialogView.findViewById(R.id.btnSave);

            // Create the dialog with the custom layout
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialogView);
            builder.setCancelable(true);

            // Create the dialog
            AlertDialog dialog = builder.create();

            btnSaveDialog.setOnClickListener(v1 -> {
                Toast.makeText(getContext(), "Đã lưu thông tin!", Toast.LENGTH_SHORT).show();
                // Dismiss the dialog
                dialog.dismiss();

                getParentFragmentManager().popBackStack();
            });

            dialog.show();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Set the binding to null to avoid memory leaks
    }
}
