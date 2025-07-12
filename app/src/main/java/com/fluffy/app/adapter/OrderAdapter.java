package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fluffy.app.R;
import com.fluffy.app.model.Order;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.order.OrderDetailFragment; // Import đúng package

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private Order[] orders;
    private OnOrderActionListener onOrderActionListener;
    private NumberFormat vietnameseFormat;

    public OrderAdapter(Context context, Order[] orders, OnOrderActionListener onOrderActionListener) {
        this.context = context;
        this.orders = orders != null ? orders : new Order[0];
        this.onOrderActionListener = onOrderActionListener;
        this.vietnameseFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        vietnameseFormat.setMaximumFractionDigits(0);
    }

    public void updateOrders(Order[] newOrders) {
        this.orders = newOrders != null ? newOrders : new Order[0];
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders[position];
        holder.orderIdTextView.setText("#" + order.getId() + " (" + order.getStatus() + ")");

        if (!order.getProducts().isEmpty()) {
            Product product = order.getProducts().get(0);
            holder.productNameTextView.setText(product.getName());
            String priceToShow = product.getDiscountPrice() != null && !product.getDiscountPrice().isEmpty() ?
                    product.getDiscountPrice() : vietnameseFormat.format((int) product.getPrice()) + " đ";
            holder.productQuantityTextView.setText(product.getQuantity() + " x " + priceToShow);
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(context).load(product.getImageUrl()).into(holder.productImageView);
            }
        }

        int totalProducts = order.getProducts().stream().mapToInt(Product::getQuantity).sum();
        double totalPrice = order.getProducts().stream()
                .mapToDouble(p -> {
                    String discountPriceStr = p.getDiscountPrice() != null ? p.getDiscountPrice().replaceAll("[^\\d.]", "").trim() : "";
                    double price = discountPriceStr.isEmpty() ? p.getPrice() : Double.parseDouble(discountPriceStr);
                    return price * p.getQuantity();
                })
                .sum();
        holder.totalItemsTextView.setText("Tổng tiền (" + totalProducts + " sản phẩm)");
        holder.totalPriceTextView.setText(vietnameseFormat.format((int) totalPrice) + " đ");

        Set<String> uniqueProducts = new HashSet<>();
        for (Product product : order.getProducts()) {
            uniqueProducts.add(product.getName());
        }
        int distinctProductCount = uniqueProducts.size();
        if (distinctProductCount > 1) {
            int unseenProducts = distinctProductCount - 1;
            holder.viewMoreTextView.setText("Xem thêm (" + unseenProducts + ")");
            holder.viewMoreTextView.setVisibility(View.VISIBLE);
        } else {
            holder.viewMoreTextView.setVisibility(View.GONE);
        }

        updateButtons(holder, order);

        // Điều hướng đến OrderDetailFragment
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, OrderDetailFragment.newInstance(order.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.length;
    }

    private void updateButtons(OrderViewHolder holder, Order order) {
        holder.cancelOrderButton.setVisibility(View.GONE);
        holder.rateButton.setVisibility(View.GONE);
        holder.buyAgainButton.setVisibility(View.GONE);
        holder.returnOrderButton.setVisibility(View.GONE);
        holder.confirmReceivedButton.setVisibility(View.GONE);

        switch (order.getStatus()) {
            case "Chờ xác nhận":
                holder.cancelOrderButton.setVisibility(View.VISIBLE);
                holder.cancelOrderButton.setOnClickListener(v -> onOrderActionListener.onCancelOrder(order));
                break;
            case "Đang vận chuyển":
                holder.confirmReceivedButton.setVisibility(View.VISIBLE);
                holder.confirmReceivedButton.setOnClickListener(v -> onOrderActionListener.onConfirmReceived(order));
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
        Button cancelOrderButton, rateButton, buyAgainButton, returnOrderButton, confirmReceivedButton;

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
            confirmReceivedButton = itemView.findViewById(R.id.btnConfirmReceived);
        }
    }

    public interface OnOrderActionListener {
        void onCancelOrder(Order order);
        void onRateOrder(Order order);
        void onBuyAgain(Order order);
        void onReturnOrder(Order order);
        void onConfirmReceived(Order order);
    }
}