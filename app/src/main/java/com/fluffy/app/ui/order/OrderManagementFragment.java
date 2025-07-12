package com.fluffy.app.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.adapter.OrderAdapter;
import com.fluffy.app.data.database.OrderDatabase;
import com.fluffy.app.model.Order;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderManagementFragment extends BaseHeaderFragment implements OrderAdapter.OnOrderActionListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Order[] orders;
    private OrderDatabase db;
    private TabLayout tabLayout;

    public static OrderManagementFragment newInstance(String status) {
        OrderManagementFragment fragment = new OrderManagementFragment();
        Bundle args = new Bundle();
        args.putString("order_status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(HeaderType.CUSTOM, "Quản lý đơn hàng");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_order_management;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState); // Gọi super để BaseHeaderFragment xử lý header

        recyclerView = view.findViewById(R.id.recyclerOrders);
        tabLayout = view.findViewById(R.id.statusTabLayout);
        db = new OrderDatabase(requireContext());
        db.createSampleData(requireContext());
        orders = new Order[0];
        orderAdapter = new OrderAdapter(requireContext(), orders, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orderAdapter);

        setupTabs();
        loadOrders();

        Bundle args = getArguments();
        String status = args != null ? args.getString("order_status") : null;
        if (status != null) {
            selectTabByStatus(status);
        }

        return view;
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
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                    Log.d("OrderManagement", "OrderId: " + orderId + ", Status: " + orderStatus + ", Product: " + productName);

                    if (!orderMap.containsKey(orderId)) {
                        orderMap.put(orderId, new Order(orderId, orderStatus, new ArrayList<>()));
                    }
                    if (productName != null && imageUrl != null) {
                        orderMap.get(orderId).getProducts().add(new Product(
                                0, orderId, productName, "", "", imageUrl, price, null, quantity,
                                "", 0.0, null, null, "", ""
                        ));
                    } else {
                        Log.w("OrderManagement", "Skipping product due to null name or imageUrl for orderId: " + orderId);
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
        orderAdapter.updateOrders(orders);
        Log.d("OrderManagement", "Total orders loaded: " + orders.length);
    }

    private void selectTabByStatus(String status) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getText().toString().equals(status)) {
                tab.select();
                loadOrdersByStatus(status);
                break;
            }
        }
    }

    @Override
    public void onCancelOrder(Order order) {
        db.updateOrderStatus(order.getId(), "Đã hủy");
        loadOrdersByStatus(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
    }

    @Override
    public void onRateOrder(Order order) {
        // Xử lý đánh giá (chưa triển khai)
    }

    @Override
    public void onBuyAgain(Order order) {
        // Xử lý mua lại (chưa triển khai)
    }

    @Override
    public void onReturnOrder(Order order) {
        db.updateOrderStatus(order.getId(), "Đã trả");
        loadOrdersByStatus(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
    }

    @Override
    public void onConfirmReceived(Order order) {
        db.updateOrderStatus(order.getId(), "Thành công");
        loadOrdersByStatus(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString());
    }
}