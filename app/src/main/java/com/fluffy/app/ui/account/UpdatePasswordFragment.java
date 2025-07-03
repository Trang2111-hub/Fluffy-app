package com.fluffy.app.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fluffy.app.R;
import com.fluffy.app.databinding.FragmentUpdatePasswordBinding;

public class UpdatePasswordFragment extends Fragment {
    private FragmentUpdatePasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Forgot password click
        binding.tvForgotPassword.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_updatePasswordFragment_to_otpUpdatePasswordFragment);
        });

        // Save password button click
        binding.btnSave.setOnClickListener(v -> {
            String newPassword = binding.edtNewPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();

            // Validate inputs
            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(requireContext(), "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                binding.edtNewPassword.setText("");
                binding.edtConfirmPassword.setText("");
                binding.edtNewPassword.requestFocus();
                return;
            }

            if (newPassword.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Mật khẩu đã được thay đổi!", Toast.LENGTH_SHORT).show();
                binding.edtNewPassword.setText("");
                binding.edtConfirmPassword.setText("");
            } else {
                Toast.makeText(requireContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                binding.edtNewPassword.setText("");
                binding.edtConfirmPassword.setText("");
                binding.edtNewPassword.requestFocus();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}