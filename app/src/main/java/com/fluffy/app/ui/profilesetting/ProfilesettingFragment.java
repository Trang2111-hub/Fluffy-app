package com.fluffy.app.ui.profilesetting;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluffy.app.R;
import com.fluffy.app.adapter.AccountOrFluffyAdapter;
import com.fluffy.app.adapter.ProfileAdapter;
import com.fluffy.app.databinding.ActivityProfilesettingBinding;
import com.fluffy.app.model.ProfileItem;
import com.fluffy.app.ui.login.LoginActivity;
import com.fluffy.app.ui.signup.SignUpActivity;
import com.fluffy.app.ui.updatePassword.UpdatePasswordActivity;
import com.fluffy.app.ui.updateProfile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfilesettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ActivityProfilesettingBinding binding;

    public ProfilesettingFragment() {
        // Required empty public constructor
    }

    public static ProfilesettingFragment newInstance(String param1, String param2) {
        ProfilesettingFragment fragment = new ProfilesettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityProfilesettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // === Đoạn logic từ activity gốc ===
        String name = requireActivity().getIntent().getStringExtra("name");
        String phone = requireActivity().getIntent().getStringExtra("phone");

        if (name != null && phone != null) {
            binding.imgProfile.setImageResource(R.drawable.logo_fluffy);
            binding.txtLogin.setText(name);
            binding.txtRegister.setText(phone);
            binding.txtLogin.setOnClickListener(null);
            binding.txtRegister.setOnClickListener(null);
        } else {
            binding.txtLogin.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            });
            binding.txtRegister.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), SignUpActivity.class);
                startActivity(intent);
            });
        }

        // --- RecyclerView "Đơn của tôi"
        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(R.drawable.profile2, "Chờ xác nhận"));
        profileItems.add(new ProfileItem(R.drawable.profile3, "Đang giao"));
        profileItems.add(new ProfileItem(R.drawable.profile4, "Thành công"));
        profileItems.add(new ProfileItem(R.drawable.profile5, "Trả hàng"));

        ProfileAdapter profileAdapter = new ProfileAdapter(profileItems);
        LinearLayoutManager profileLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(profileLayoutManager);
        binding.recyclerView.setAdapter(profileAdapter);

        // --- RecyclerView "Tài khoản"
        List<ProfileItem> accountItems = new ArrayList<>();
        accountItems.add(new ProfileItem(R.drawable.profile6, "Quản lí thông tin cá nhân"));
        accountItems.add(new ProfileItem(R.drawable.profile7, "Quản lí đơn hàng"));
        accountItems.add(new ProfileItem(R.drawable.profile8, "Quản lí địa chỉ"));
        accountItems.add(new ProfileItem(R.drawable.profile9, "Quản lí voucher"));
        accountItems.add(new ProfileItem(R.drawable.profile10, "Quản lí sản phẩm yêu thích"));
        accountItems.add(new ProfileItem(R.drawable.profile11, "Cập nhật mật khẩu"));

        AccountOrFluffyAdapter accountAdapter = new AccountOrFluffyAdapter(accountItems,
                title -> {
                    if ("Cập nhật mật khẩu".equals(title)) {
                        Intent intent = new Intent(requireContext(), UpdatePasswordActivity.class);
                        startActivity(intent);
                    } else if ("Quản lí thông tin cá nhân".equals(title)) {
                        startActivity(new Intent(requireContext(), ProfileActivity.class));
                    }
                });

        LinearLayoutManager accountLayoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewAccount.setLayoutManager(accountLayoutManager);
        binding.recyclerViewAccount.setAdapter(accountAdapter);

        // --- RecyclerView "Về Fluffy"
        List<ProfileItem> fluffyItems = new ArrayList<>();
        fluffyItems.add(new ProfileItem(R.drawable.profile12, "Giới thiệu thông tin"));
        fluffyItems.add(new ProfileItem(R.drawable.profile13, "Chính sách bán hàng"));

        AccountOrFluffyAdapter fluffyAdapter = new AccountOrFluffyAdapter(fluffyItems, null);
        LinearLayoutManager fluffyLayoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewFluffy.setLayoutManager(fluffyLayoutManager);
        binding.recyclerViewFluffy.setAdapter(fluffyAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
