package com.fluffy.app.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fluffy.app.R;
import com.fluffy.app.databinding.FragmentUpdatePassword2Binding;

public class UpdatePassword2Fragment extends Fragment {
    private FragmentUpdatePassword2Binding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdatePassword2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Example: Add a button to trigger navigation back to UpdatePasswordFragment
        // Replace with actual UI element in fragment_update_password2.xml
        binding.btnCancel.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_updatePassword2Fragment_to_updatePasswordFragment);
            Toast.makeText(requireContext(), "Hoàn tất cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}