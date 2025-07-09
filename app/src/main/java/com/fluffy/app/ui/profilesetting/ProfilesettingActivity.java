package com.fluffy.app.ui.profilesetting;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fluffy.app.R;
import com.fluffy.app.adapter.AccountOrFluffyAdapter;
import com.fluffy.app.adapter.ProfileAdapter;
import com.fluffy.app.databinding.ActivityProfilesettingBinding;
import com.fluffy.app.model.ProfileItem;
import com.fluffy.app.ui.updatePassword.UpdatePasswordActivity;
import com.fluffy.app.ui.updateProfile.ProfileActivity;
import com.fluffy.app.ui.updateProfile.UpdateProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfilesettingActivity extends AppCompatActivity {

    private ActivityProfilesettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable view binding
        binding = ActivityProfilesettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Prepare data for RecyclerView - "Đơn của tôi"
        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(R.drawable.profile2, "Chờ xác nhận"));
        profileItems.add(new ProfileItem(R.drawable.profile3, "Đang giao"));
        profileItems.add(new ProfileItem(R.drawable.profile4, "Thành công"));
        profileItems.add(new ProfileItem(R.drawable.profile5, "Trả hàng"));

        // Set up RecyclerView for "Đơn của tôi" section (horizontal orientation)
        ProfileAdapter profileAdapter = new ProfileAdapter(profileItems);
        LinearLayoutManager profileLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(profileLayoutManager);
        binding.recyclerView.setAdapter(profileAdapter);

        // Prepare data for "Tài khoản" section
        List<ProfileItem> accountItems = new ArrayList<>();
        accountItems.add(new ProfileItem(R.drawable.profile6, "Quản lí thông tin cá nhân"));
        accountItems.add(new ProfileItem(R.drawable.profile7, "Quản lí đơn hàng"));
        accountItems.add(new ProfileItem(R.drawable.profile8, "Quản lí địa chỉ"));
        accountItems.add(new ProfileItem(R.drawable.profile9, "Quản lí voucher"));
        accountItems.add(new ProfileItem(R.drawable.profile10, "Quản lí sản phẩm yêu thích"));
        accountItems.add(new ProfileItem(R.drawable.profile11, "Cập nhật mật khẩu"));

        // Prepare data for "Về Fluffy" section
        List<ProfileItem> fluffyItems = new ArrayList<>();
        fluffyItems.add(new ProfileItem(R.drawable.profile12, "Giới thiệu thông tin"));
        fluffyItems.add(new ProfileItem(R.drawable.profile13, "Chính sách bán hàng"));


        // Set up RecyclerView for "Tài khoản" section (vertical orientation)
        /// Khởi tạo accountAdapter với listener để xử lý sự kiện click
        AccountOrFluffyAdapter accountAdapter = new AccountOrFluffyAdapter(accountItems,
                new AccountOrFluffyAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(String title) {
                        if (title.equals("Cập nhật mật khẩu")) {
                            // Chuyển sang trang cập nhật mật khẩu
                            Intent intent = new Intent(ProfilesettingActivity.this, UpdatePasswordActivity.class);
                            startActivity(intent);
                        } else if (title.equals("Quản lí thông tin cá nhân")) {
                            Intent intent = new Intent(ProfilesettingActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        LinearLayoutManager accountLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAccount.setLayoutManager(accountLayoutManager);
        binding.recyclerViewAccount.setAdapter(accountAdapter);

// Khởi tạo fluffyAdapter mà không cần xử lý sự kiện click, nên truyền null cho listener
        AccountOrFluffyAdapter fluffyAdapter = new AccountOrFluffyAdapter(fluffyItems, null);
        LinearLayoutManager fluffyLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewFluffy.setLayoutManager(fluffyLayoutManager);
        binding.recyclerViewFluffy.setAdapter(fluffyAdapter);
    }
}
