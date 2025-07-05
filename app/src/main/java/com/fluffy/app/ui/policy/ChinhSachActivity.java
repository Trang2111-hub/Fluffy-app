package com.fluffy.app.ui.policy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fluffy.app.R;

public class ChinhSachActivity extends AppCompatActivity {

    private Spinner dropdownMenu;
    private ScrollView scrollView;
    private TextView chinhSachDoiTra, chinhSachBaoMat, chinhSachVanChuyen, huongDanMuaHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chinh_sach);

        // Ràng buộc các thành phần giao diện
        dropdownMenu = findViewById(R.id.dropdownMenu);
        scrollView = findViewById(R.id.scrollView);

        // Ràng buộc các TextViews để dễ dàng cuộn đến
        chinhSachDoiTra = findViewById(R.id.chinhSachDoiTra);
        chinhSachBaoMat = findViewById(R.id.chinhSachBaoMat);
        chinhSachVanChuyen = findViewById(R.id.chinhSachVanChuyen);
        huongDanMuaHang = findViewById(R.id.huongDanMuaHang);

        // Cấu hình Adapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_menu_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMenu.setAdapter(adapter);

        // Xử lý sự kiện khi chọn item trong Spinner
        dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        scrollToView(chinhSachDoiTra);
                        break;
                    case 1:
                        scrollToView(chinhSachBaoMat);
                        break;
                    case 2:
                        scrollToView(chinhSachVanChuyen);
                        break;
                    case 3:
                        scrollToView(huongDanMuaHang);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Tùy chọn: Xử lý khi không có mục nào được chọn
            }
        });

        // Điều chỉnh padding để tránh bị che bởi hệ thống (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Hàm cuộn đến View tương ứng
    private void scrollToView(View view) {
        scrollView.smoothScrollTo(0, view.getTop());
    }
}