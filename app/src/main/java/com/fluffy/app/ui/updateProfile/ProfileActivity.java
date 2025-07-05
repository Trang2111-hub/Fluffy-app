package com.fluffy.app.ui.updateProfile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fluffy.app.R;
import com.fluffy.app.databinding.ActivityProfileBinding;
import com.fluffy.app.ui.updateProfile.UpdateProfileActivity;

public class ProfileActivity extends AppCompatActivity {

    // Đối tượng ViewBinding
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Thông tin cá nhân");

        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });

        binding.btnLinkFacebook.setOnClickListener(v -> {
            linkSocialAccount("Facebook");
        });

        binding.btnLinkGoogle.setOnClickListener(v -> {
            linkSocialAccount("Google");
        });
    }

    private void linkSocialAccount(String socialMedia) {
        if (socialMedia.equals("Facebook")) {
            binding.btnLinkFacebook.setText("Đã liên kết");
            binding.btnLinkFacebook.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.btnLinkFacebook.setTextColor(getResources().getColor(R.color.white));
        } else if (socialMedia.equals("Google")) {
            binding.btnLinkGoogle.setText("Đã liên kết");
            binding.btnLinkGoogle.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.btnLinkGoogle.setTextColor(getResources().getColor(R.color.white));
        }


        saveSocialLinkStatus(socialMedia);

        Toast.makeText(this, socialMedia + " liên kết thành công", Toast.LENGTH_SHORT).show();
    }

    private void saveSocialLinkStatus(String socialMedia) {
    }
}
