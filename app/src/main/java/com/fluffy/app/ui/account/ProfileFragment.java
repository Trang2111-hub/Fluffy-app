package com.fluffy.app.ui.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityMainBinding;
import com.fluffy.app.databinding.FragmentProfileBinding;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Sự kiện click nút "Chỉnh sửa thông tin"
        binding.btnEditProfile.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new UpdateProfileFragment());
            transaction.addToBackStack(null); // Cho phép quay lại
            transaction.commit();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
