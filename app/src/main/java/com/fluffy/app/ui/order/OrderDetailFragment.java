package com.fluffy.app.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.fluffy.app.R;
import com.fluffy.app.data.database.OrderDatabase;
import com.fluffy.app.model.Order;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderDetailFragment extends BaseHeaderFragment {

    private static final String ARG_ORDER_ID = "extra_order_id";
    private static final String TAG = "OrderDetailFragment";
    private NumberFormat vietnameseFormat;
    private OrderDatabase db;

    public static OrderDetailFragment newInstance(int orderId) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(HeaderType.CUSTOM, "Chi tiết đơn hàng");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_order_detail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState); // Gọi super để BaseHeaderFragment xử lý header

        vietnameseFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        vietnameseFormat.setMaximumFractionDigits(0);
        db = new OrderDatabase(requireContext());

        Bundle args = getArguments();
        int orderId = args != null ? args.getInt(ARG_ORDER_ID, -1) : -1;
        if (orderId != -1) {
            loadOrderDetails(orderId, view);
        } else {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }

        return view;
    }

    private void loadOrderDetails(int orderId, View view) {
        android.database.Cursor cursor = db.getOrderByIdWithDetails(orderId);
        Map<Integer, Order> orderMap = new HashMap<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int currentOrderId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                if (currentOrderId == orderId) {
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                    if (!orderMap.containsKey(currentOrderId)) {
                        orderMap.put(currentOrderId, new Order(currentOrderId, status, new ArrayList<>()));
                    }
                    if (productName != null && imageUrl != null) {
                        String categoryInfo = generateCategoryInfo(productName);
                        orderMap.get(currentOrderId).getProducts().add(new Product(
                                0, currentOrderId, productName, "", "", imageUrl, price, null, quantity,
                                categoryInfo, 0.0, null, null, "", ""
                        ));
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        Order order = orderMap.get(orderId);
        if (order != null) {
            TextView orderIdTextView = view.findViewById(R.id.tvOrderId);
            LinearLayout productsContainer = view.findViewById(R.id.productsContainer);
            TextView shippingInfoTextView = view.findViewById(R.id.tvShippingInfo);
            TextView shippingAddressTextView = view.findViewById(R.id.shipping_address);
            TextView shippingPhoneTextView = view.findViewById(R.id.shipping_phone);
            TextView shippingEmailTextView = view.findViewById(R.id.shipping_email);
            TextView shippingServiceTextView = view.findViewById(R.id.shipping_service);
            TextView paymentInfoTextView = view.findViewById(R.id.tvPaymentInfo);
            TextView paymentMethodTextView = view.findViewById(R.id.payment_method);
            TextView paymentAmountTextView = view.findViewById(R.id.payment_amount);
            TextView paymentShippingTextView = view.findViewById(R.id.payment_shipping);
            TextView paymentDiscountTextView = view.findViewById(R.id.payment_discount);
            TextView paymentTotalTextView = view.findViewById(R.id.payment_total);
            AppCompatButton cancelOrderButton = view.findViewById(R.id.btnCancelOrder);
            AppCompatButton rateButton = view.findViewById(R.id.btnRate);
            AppCompatButton buyAgainButton = view.findViewById(R.id.btnReorder);
            AppCompatButton returnOrderButton = view.findViewById(R.id.btnReturnOrder);
            AppCompatButton confirmReceivedButton = view.findViewById(R.id.btnConfirmReceived);

            Log.d(TAG, "orderIdTextView: " + (orderIdTextView != null));
            Log.d(TAG, "cancelOrderButton: " + (cancelOrderButton != null));

            if (orderIdTextView != null) {
                orderIdTextView.setText("Đơn hàng #" + order.getId() + " (" + order.getStatus() + ")");
            }

            if (productsContainer != null) {
                productsContainer.removeAllViews();
                for (Product product : order.getProducts()) {
                    View productView = getLayoutInflater().inflate(R.layout.item_product_detail, productsContainer, false);
                    TextView productNameTextView = productView.findViewById(R.id.tvProductName);
                    TextView productCategoryTextView = productView.findViewById(R.id.tvProductCategory);
                    TextView productPriceTextView = productView.findViewById(R.id.tvProductPrice);
                    ImageView productImageView = productView.findViewById(R.id.imgProduct);

                    productNameTextView.setText(product.getName());
                    productCategoryTextView.setText(product.getCategoryInfo());
                    productPriceTextView.setText(product.getQuantity() + " x " + vietnameseFormat.format(product.getPrice()) + " đ");
                    if (product.getImageUrl() != null) {
                        Glide.with(requireContext()).load(product.getImageUrl()).into(productImageView);
                    }

                    productsContainer.addView(productView);
                }
            }

            int totalProducts = order.getProducts().stream().mapToInt(Product::getQuantity).sum();
            double totalItemPrice = order.getProducts().stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
            double shippingFee = 0.0;
            double discount = 0.0;
            double totalPrice = totalItemPrice + shippingFee - discount;

            if (shippingInfoTextView != null) shippingInfoTextView.setText("Thông tin giao hàng");
            if (shippingAddressTextView != null) shippingAddressTextView.setText("Địa chỉ nhận hàng: 585 Trường Chinh phường Tân Thới Nhất Quận 12");
            if (shippingPhoneTextView != null) shippingPhoneTextView.setText("Số điện thoại: 0813849476");
            if (shippingEmailTextView != null) shippingEmailTextView.setText("Email: khoatrinhtiendat@gmail.com");
            if (shippingServiceTextView != null) shippingServiceTextView.setText("Dịch vụ: không");
            if (paymentInfoTextView != null) paymentInfoTextView.setText("Thông tin thanh toán");
            if (paymentMethodTextView != null) paymentMethodTextView.setText("Hình thức thanh toán: Ví điện tử");
            if (paymentAmountTextView != null) paymentAmountTextView.setText("Tiền hàng: " + vietnameseFormat.format(totalItemPrice) + " đ");
            if (paymentShippingTextView != null) paymentShippingTextView.setText("Phí vận chuyển: " + vietnameseFormat.format(shippingFee) + " đ");
            if (paymentDiscountTextView != null) paymentDiscountTextView.setText("Giảm giá: " + vietnameseFormat.format(discount) + " đ");
            if (paymentTotalTextView != null) paymentTotalTextView.setText("Tổng cộng: " + vietnameseFormat.format(totalPrice) + " đ");

            if (cancelOrderButton != null) cancelOrderButton.setVisibility(View.GONE);
            if (rateButton != null) rateButton.setVisibility(View.GONE);
            if (buyAgainButton != null) buyAgainButton.setVisibility(View.GONE);
            if (returnOrderButton != null) returnOrderButton.setVisibility(View.GONE);
            if (confirmReceivedButton != null) confirmReceivedButton.setVisibility(View.GONE);

            if (order != null && cancelOrderButton != null && rateButton != null && buyAgainButton != null && returnOrderButton != null && confirmReceivedButton != null) {
                switch (order.getStatus()) {
                    case "Chờ xác nhận":
                        cancelOrderButton.setVisibility(View.VISIBLE);
                        cancelOrderButton.setOnClickListener(v -> {
                            db.updateOrderStatus(order.getId(), "Đã hủy");
                            loadOrderDetails(orderId, view);
                        });
                        break;
                    case "Đang vận chuyển":
                        confirmReceivedButton.setVisibility(View.VISIBLE);
                        confirmReceivedButton.setOnClickListener(v -> {
                            db.updateOrderStatus(order.getId(), "Thành công");
                            loadOrderDetails(orderId, view);
                        });
                        break;
                    case "Thành công":
                        rateButton.setVisibility(View.VISIBLE);
                        buyAgainButton.setVisibility(View.VISIBLE);
                        returnOrderButton.setVisibility(View.VISIBLE);
                        rateButton.setOnClickListener(v -> {
                            // Xử lý đánh giá (chưa triển khai)
                        });
                        buyAgainButton.setOnClickListener(v -> {
                            // Xử lý mua lại (chưa triển khai)
                        });
                        returnOrderButton.setOnClickListener(v -> {
                            db.updateOrderStatus(order.getId(), "Đã trả");
                            loadOrderDetails(orderId, view);
                        });
                        break;
                    case "Đã trả":
                    case "Đã hủy":
                        buyAgainButton.setVisibility(View.VISIBLE);
                        buyAgainButton.setOnClickListener(v -> {
                            // Xử lý mua lại (chưa triển khai)
                        });
                        break;
                }
            }
        } else {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }
    }

    private String generateCategoryInfo(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return "Không xác định";
        }
        switch (productName) {
            case "Gấu bông Sanrio":
                return "Nhiều màu, 50cm";
            case "Cappy barra nước mũi":
                return "Nâu, 30cm";
            case "Gấu bông heo con":
                return "Hồng, 60cm";
            case "Cappy barra matcha":
                return "Xanh lá, 40cm";
            default:
                return "Nhiều màu, 50cm";
        }
    }
}