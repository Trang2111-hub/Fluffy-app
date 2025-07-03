package com.fluffy.app.ui.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffy.app.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment {

    private EditText edtName, edtDob, edtPhone, edtEmail;
    private Button btnCancel, btnSave;
    private String oldName, oldDob, oldPhone, oldEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        edtName = view.findViewById(R.id.edtName);
        edtDob = view.findViewById(R.id.edtDob);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);

        // Lưu thông tin cũ để reset khi nhấn Hủy
        oldName = edtName.getText().toString();
        oldDob = edtDob.getText().toString();
        oldPhone = edtPhone.getText().toString();
        oldEmail = edtEmail.getText().toString();

        // Xử lý sự kiện khi người dùng click vào EditText
        edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtName.setText("");
            }
        });


        // Hiển thị DatePicker khi click vào ngày sinh
        edtDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        edtDob.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Validate số điện thoại khi thay đổi
        edtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String phone = edtPhone.getText().toString();
                if (!phone.matches("^0\\d{9}$")) {
                    edtPhone.setError("Số điện thoại không hợp lệ");
                }
            }
        });

        // Validate email khi thay đổi
        edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = edtEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("Email không hợp lệ");
                }
            }
        });

        // Nút Hủy: reset lại thông tin cũ
        btnCancel.setOnClickListener(v -> {
            edtName.setText(oldName);
            edtDob.setText(oldDob);
            edtPhone.setText(oldPhone);
            edtEmail.setText(oldEmail);
        });

        // Nút Lưu: xác nhận trước khi lưu
        btnSave.setOnClickListener(v -> {
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

        return view;
    }
}
