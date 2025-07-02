package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.fluffy.app.R;
import com.fluffy.app.model.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private OnOrderActionListener onOrderActionListener;
    private NumberFormat vietnameseFormat;

    public OrderAdapter(Context context, List<Order> orders, OnOrderActionListener onOrderActionListener) {
        this.context = context;
        this.orders = orders;
        this.onOrderActionListener = onOrderActionListener;
        this.vietnameseFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        vietnameseFormat.setMaximumFractionDigits(0);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderIdTextView.setText("#" + order.getOrderId() + " (" + order.getStatus() + ")");
        holder.productNameTextView.setText(order.getProductName());
        holder.productQuantityTextView.setText(order.getQuantity() + " x " + vietnameseFormat.format(order.getPrice()) + " đ");
        holder.totalItemsTextView.setText("Tổng tiền (" + order.getQuantity() + " sản phẩm)");
        holder.totalPriceTextView.setText(vietnameseFormat.format(order.getQuantity() * order.getPrice()) + " đ");

        // Show "Xem thêm(n)" if quantity > 1, where n = quantity - 1
        if (order.getQuantity() > 1) {
            int additionalItems = order.getQuantity() - 1;
            holder.viewMoreTextView.setText("Xem thêm(" + additionalItems + ")");
            holder.viewMoreTextView.setVisibility(View.VISIBLE);
        } else {
            holder.viewMoreTextView.setVisibility(View.GONE);
        }

        holder.productImageView.setImageBitmap(order.getProductImage());
        updateButtons(holder, order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void updateButtons(OrderViewHolder holder, Order order) {
        holder.cancelOrderButton.setVisibility(View.GONE);
        holder.rateButton.setVisibility(View.GONE);
        holder.buyAgainButton.setVisibility(View.GONE);
        holder.returnOrderButton.setVisibility(View.GONE);

        switch (order.getStatus()) {
            case "Chờ xác nhận":
                holder.cancelOrderButton.setVisibility(View.VISIBLE);
                holder.cancelOrderButton.setOnClickListener(v -> onOrderActionListener.onCancelOrder(order));
                break;
            case "Thành công":
                holder.rateButton.setVisibility(View.VISIBLE);
                holder.buyAgainButton.setVisibility(View.VISIBLE);
                holder.returnOrderButton.setVisibility(View.VISIBLE);
                holder.rateButton.setOnClickListener(v -> onOrderActionListener.onRateOrder(order));
                holder.buyAgainButton.setOnClickListener(v -> onOrderActionListener.onBuyAgain(order));
                holder.returnOrderButton.setOnClickListener(v -> onOrderActionListener.onReturnOrder(order));
                break;
            case "Đã trả":
            case "Đã hủy":
                holder.buyAgainButton.setVisibility(View.VISIBLE);
                holder.buyAgainButton.setOnClickListener(v -> onOrderActionListener.onBuyAgain(order));
                break;
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, productNameTextView, productQuantityTextView, totalItemsTextView, totalPriceTextView, viewMoreTextView;
        ImageView productImageView;
        Button cancelOrderButton, rateButton, buyAgainButton, returnOrderButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.tvProductId);
            productNameTextView = itemView.findViewById(R.id.tvProductName);
            productQuantityTextView = itemView.findViewById(R.id.tvProductPrice);
            totalItemsTextView = itemView.findViewById(R.id.tvTotalItems);
            totalPriceTextView = itemView.findViewById(R.id.tvTotalPrice);
            viewMoreTextView = itemView.findViewById(R.id.tvViewMore);
            productImageView = itemView.findViewById(R.id.imgProduct);
            cancelOrderButton = itemView.findViewById(R.id.btnCancelOrder);
            rateButton = itemView.findViewById(R.id.btnRate);
            buyAgainButton = itemView.findViewById(R.id.btnReorder);
            returnOrderButton = itemView.findViewById(R.id.btnReturnOrder);
        }
    }

    public interface OnOrderActionListener {
        void onCancelOrder(Order order);
        void onRateOrder(Order order);
        void onBuyAgain(Order order);
        void onReturnOrder(Order order);
    }
}