package com.fluffy.app.ui.account.order;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fluffy.app.R;
import com.fluffy.app.adapter.OrderAdapter;
import com.fluffy.app.data.database.OrderDatabase;
import com.fluffy.app.model.Order;
import com.fluffy.app.model.Product;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagementActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Order[] orders;
    private OrderDatabase db;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        recyclerView = findViewById(R.id.recyclerOrders);
        tabLayout = findViewById(R.id.statusTabLayout);
        db = new OrderDatabase(this);
        db.createSampleData(this);
        orders = new Order[0];
        orderAdapter = new OrderAdapter(this, orders, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        setupTabs();
        loadOrders();
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String status = tab.getText().toString();
                loadOrdersByStatus(status);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String status = tab.getText().toString();
                loadOrdersByStatus(status);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Chờ xác nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang chuẩn bị hàng"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang vận chuyển"));
        tabLayout.addTab(tabLayout.newTab().setText("Thành công"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã trả"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));
    }

    private void loadOrders() {
        loadOrdersByStatus("Tất cả");
    }

    private void loadOrdersByStatus(String status) {
        android.database.Cursor cursor;
        if ("Tất cả".equals(status)) {
            cursor = db.getAllOrdersWithDetails();
        } else {
            cursor = db.getOrdersByStatusWithDetails(status);
        }
        Map<Integer, Order> orderMap = new HashMap<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String orderStatus = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    Bitmap image = (imageBytes != null && imageBytes.length > 0) ? BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length) : null;

                    Log.d("OrderManagement", "OrderId: " + orderId + ", Status: " + orderStatus + ", Product: " + productName);

                    if (!orderMap.containsKey(orderId)) {
                        orderMap.put(orderId, new Order(orderId, orderStatus, new ArrayList<>()));
                    }
                    if (productName != null && image != null) {
                        orderMap.get(orderId).getProducts().add(new Product(orderId, productName, price, image, quantity));
                    } else {
                        Log.w("OrderManagement", "Skipping product due to null name or image for orderId: " + orderId);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d("OrderManagement", "No data found for status: " + status);
            }
            cursor.close();
        } else {
            Log.e("OrderManagement", "Cursor is null for status: " + status);
        }
        orders = orderMap.values().toArray(new Order[0]);
        orderAdapter.updateOrders(orders); // Cập nhật adapter với phương thức mới
        Log.d("OrderManagement", "Total orders loaded: " + orders.length);
    }

    @Override
    public void onCancelOrder(Order order) {

    }

    @Override
    public void onRateOrder(Order order) {
        // Handle rating logic
    }

    @Override
    public void onBuyAgain(Order order) {
        // Handle buy again logic
    }

    @Override
    public void onReturnOrder(Order order) {
        // Handle return logic
    }
}