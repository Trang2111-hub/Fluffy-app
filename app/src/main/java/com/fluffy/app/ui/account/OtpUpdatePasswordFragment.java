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
import com.fluffy.app.databinding.FragmentOtpUpdatePasswordBinding;

public class OtpUpdatePasswordFragment extends Fragment {
    private FragmentOtpUpdatePasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtpUpdatePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnConfirm.setOnClickListener(v -> {
            String otp = binding.otp1.getText().toString() +
                    binding.otp2.getText().toString() +
                    binding.otp3.getText().toString() +
                    binding.otp4.getText().toString();

            if (TextUtils.isEmpty(otp) || otp.length() != 4) {
                Toast.makeText(requireContext(), "Vui lòng nhập đủ 4 chữ số OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (otp.equals("1234")) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_otpUpdatePasswordFragment_to_updatePassword2Fragment);
            } else {
                Toast.makeText(requireContext(), "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
                binding.otp1.setText("");
                binding.otp2.setText("");
                binding.otp3.setText("");
                binding.otp4.setText("");
                binding.otp1.requestFocus();
            }
        });

        // Resend OTP button click
        binding.tvResendOtp.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Mã OTP đã được gửi lại", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}