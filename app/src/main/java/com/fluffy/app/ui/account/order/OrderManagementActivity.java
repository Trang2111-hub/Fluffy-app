package com.fluffy.app.ui.account.order;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fluffy.app.R;
import com.fluffy.app.adapter.OrderAdapter;
import com.fluffy.app.data.database.OrderDatabase;
import com.fluffy.app.databinding.ActivityOrderManagementBinding;
import com.fluffy.app.model.Order;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class OrderManagementActivity extends AppCompatActivity {
    private ActivityOrderManagementBinding binding;
    private OrderDatabase db;
    private OrderAdapter adapter;
    private ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new OrderDatabase(this);
        db.createSampleData(this); // Initialize sample data
        orders = new ArrayList<>();

        // Setup RecyclerView
        adapter = new OrderAdapter(this, orders, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onCancelOrder(Order order) {
                db.deleteOrder(order.getOrderId());
                loadOrders(binding.statusTabLayout.getSelectedTabPosition() == 0 ? "Tất cả" : binding.statusTabLayout.getTabAt(binding.statusTabLayout.getSelectedTabPosition()).getText().toString());
            }

            @Override
            public void onRateOrder(Order order) {
                // Implement rating logic
            }

            @Override
            public void onBuyAgain(Order order) {
                // Implement buy again logic
            }

            @Override
            public void onReturnOrder(Order order) {
                // Implement return logic
            }
        });
        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerOrders.setAdapter(adapter);

        // Setup TabLayout
        setupTabs();

        // Load orders with default status (Tất cả)
        loadOrders("Tất cả");

    }

    private void setupTabs() {
        String[] statuses = {"Tất cả", "Chờ xác nhận", "Đang chuẩn bị", "Đang vận chuyển", "Thành công", "Đã trả", "Đã hủy"};
        TabLayout tabLayout = binding.statusTabLayout;
        for (String status : statuses) {
            tabLayout.addTab(tabLayout.newTab().setText(status));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadOrders(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadOrders(String status) {
        orders.clear();
        Cursor cursor = status.equals("Tất cả") ? db.getAllOrdersWithDetails() : db.getOrdersByStatusWithDetails(status);
        if (cursor.moveToFirst()) {
            do {
                byte[] imageByteArray = cursor.getBlob(5);
                Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                orders.add(new Order(
                        cursor.getInt(0),  // Order ID
                        cursor.getString(1),  // Status
                        cursor.getString(2),  // Product Name
                        cursor.getDouble(3),  // Price
                        cursor.getInt(4),  // Quantity
                        image  // Product Image
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}