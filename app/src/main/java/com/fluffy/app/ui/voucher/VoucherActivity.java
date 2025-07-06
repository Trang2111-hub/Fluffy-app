package com.fluffy.app.ui.voucher;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ListView;
import com.fluffy.app.adapter.VoucherListAdapter;
import com.fluffy.app.model.VoucherItem;

import com.fluffy.app.R;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher);
        // Voucher Data
        List<VoucherItem> voucherItems = new ArrayList<>();
        voucherItems.add(new VoucherItem("FLUFFY tặng mã giảm 20% cho khách hàng mới!", "Giảm 20%", "Đơn tối thiểu đ200k", "30/06/2025    23:59", true));
        voucherItems.add(new VoucherItem("FLUFFY tặng bạn mã giảm 10% nhé!", "Giảm 10%", "Đơn tối thiểu đ100k", "31/12/2025    23:59", false));

        // Setup ListView
        VoucherListAdapter voucherAdapter = new VoucherListAdapter(this, voucherItems);
        ListView listViewVoucher = findViewById(R.id.listViewVoucher);
        listViewVoucher.setAdapter(voucherAdapter);
    }
}