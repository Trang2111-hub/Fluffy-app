package com.fluffy.app.ui.profilesetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fluffy.app.R;
import com.fluffy.app.adapter.AccountOrFluffyAdapter;
import com.fluffy.app.adapter.ProfileAdapter;
import com.fluffy.app.databinding.ActivityProfilesettingBinding;
import com.fluffy.app.model.ProfileItem;
import com.fluffy.app.ui.login.LoginActivity;
import com.fluffy.app.ui.signup.SignUpActivity;
import com.fluffy.app.ui.updatePassword.UpdatePasswordActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfilesettingActivity extends AppCompatActivity {

    private ActivityProfilesettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfilesettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- Xử lý hiện thông tin đăng nhập hoặc Đăng nhập | Đăng ký
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");

        if (name != null && phone != null) {
            binding.imgProfile.setImageResource(R.drawable.logo_fluffy);
            binding.txtLogin.setText(name);
            binding.txtRegister.setText(phone);
            binding.txtLogin.setOnClickListener(null);
            binding.txtRegister.setOnClickListener(null);
        } else {
            binding.txtLogin.setOnClickListener(v -> {
                Intent intent = new Intent(ProfilesettingActivity.this, LoginActivity.class);
                startActivity(intent);
            });
            binding.txtRegister.setOnClickListener(v -> {
                Intent intent = new Intent(ProfilesettingActivity.this, SignUpActivity.class);
                startActivity(intent);
            });
        }

        // --- Set up RecyclerView "Đơn của tôi"
        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(R.drawable.profile2, "Chờ xác nhận"));
        profileItems.add(new ProfileItem(R.drawable.profile3, "Đang giao"));
        profileItems.add(new ProfileItem(R.drawable.profile4, "Thành công"));
        profileItems.add(new ProfileItem(R.drawable.profile5, "Trả hàng"));

        ProfileAdapter profileAdapter = new ProfileAdapter(profileItems);
        LinearLayoutManager profileLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(profileLayoutManager);
        binding.recyclerView.setAdapter(profileAdapter);

        // --- Set up RecyclerView "Tài khoản"
        List<ProfileItem> accountItems = new ArrayList<>();
        accountItems.add(new ProfileItem(R.drawable.profile6, "Quản lí thông tin cá nhân"));
        accountItems.add(new ProfileItem(R.drawable.profile7, "Quản lí đơn hàng"));
        accountItems.add(new ProfileItem(R.drawable.profile8, "Quản lí địa chỉ"));
        accountItems.add(new ProfileItem(R.drawable.profile9, "Quản lí voucher"));
        accountItems.add(new ProfileItem(R.drawable.profile10, "Quản lí sản phẩm yêu thích"));
        accountItems.add(new ProfileItem(R.drawable.profile11, "Cập nhật mật khẩu"));

        AccountOrFluffyAdapter accountAdapter = new AccountOrFluffyAdapter(accountItems,
                new AccountOrFluffyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String title) {
                        if ("Cập nhật mật khẩu".equals(title)) {
                            Intent intent = new Intent(ProfilesettingActivity.this, UpdatePasswordActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        LinearLayoutManager accountLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAccount.setLayoutManager(accountLayoutManager);
        binding.recyclerViewAccount.setAdapter(accountAdapter);

        // --- Set up RecyclerView "Về Fluffy"
        List<ProfileItem> fluffyItems = new ArrayList<>();
        fluffyItems.add(new ProfileItem(R.drawable.profile12, "Giới thiệu thông tin"));
        fluffyItems.add(new ProfileItem(R.drawable.profile13, "Chính sách bán hàng"));

        // Khởi tạo fluffyAdapter mà không cần xử lý click, truyền null
        AccountOrFluffyAdapter fluffyAdapter = new AccountOrFluffyAdapter(fluffyItems, null);
        LinearLayoutManager fluffyLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewFluffy.setLayoutManager(fluffyLayoutManager);
        binding.recyclerViewFluffy.setAdapter(fluffyAdapter);
    }
}
