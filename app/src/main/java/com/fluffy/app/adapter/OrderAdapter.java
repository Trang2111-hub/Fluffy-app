package com.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.fluffy.app.R;
import com.fluffy.app.databinding.ItemOrderBinding;
import com.fluffy.app.model.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    // Constructor để truyền dữ liệu vào Adapter
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item layout và tạo view holder
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        // Lấy đơn hàng tại vị trí hiện tại
        Order order = orderList.get(position);

        // Bind dữ liệu từ Order vào các view trong layout
        holder.binding.orderId.setText("#" + order.getOrderId());
        holder.binding.productName.setText(order.getProductName());
        holder.binding.productInfo.setText(order.getProductInfo());
        holder.binding.orderTotal.setText(String.format("%,.0f đ", order.getTotalAmount()));

        // Hiển thị hoặc ẩn nút "Hủy đơn" nếu đơn hàng có thể hủy
        if (order.isCancelable()) {
            holder.binding.cancelOrderButton.setVisibility(View.VISIBLE);
        } else {
            holder.binding.cancelOrderButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    // Phương thức để cập nhật danh sách đơn hàng
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged(); // Cập nhật RecyclerView khi danh sách thay đổi
    }

    // ViewHolder sử dụng View Binding
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
