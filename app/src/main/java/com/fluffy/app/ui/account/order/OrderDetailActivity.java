package com.fluffy.app.ui.account.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.fluffy.app.R;
import com.fluffy.app.data.database.OrderDatabase;
import com.fluffy.app.model.Order;
import com.fluffy.app.model.Product;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ORDER_ID = "extra_order_id";
    private NumberFormat vietnameseFormat;
    private OrderDatabase db;

    public static void start(Context context, int orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        vietnameseFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        vietnameseFormat.setMaximumFractionDigits(0);
        db = new OrderDatabase(this);

        int orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId != -1) {
            loadOrderDetails(orderId);
        } else {
            finish();
        }
    }

    private void loadOrderDetails(int orderId) {
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
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    Bitmap image = (imageBytes != null && imageBytes.length > 0) ? BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length) : null;

                    if (!orderMap.containsKey(currentOrderId)) {
                        orderMap.put(currentOrderId, new Order(currentOrderId, status, new ArrayList<>()));
                    }
                    // Thêm tất cả sản phẩm vào danh sách products của order
                    if (productName != null && image != null) {
                        orderMap.get(currentOrderId).getProducts().add(new Product(currentOrderId, productName, price, image, quantity));
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        Order order = orderMap.get(orderId);
        if (order != null) {
            TextView orderIdTextView = findViewById(R.id.tvOrderId);
            LinearLayout productsContainer = findViewById(R.id.productsContainer);
            TextView totalItemsTextView = findViewById(R.id.tvTotalItems);
            TextView totalPriceTextView = findViewById(R.id.tvTotalPrice);
            TextView shippingInfoTextView = findViewById(R.id.tvShippingInfo);
            TextView paymentInfoTextView = findViewById(R.id.tvPaymentInfo);
            Button cancelOrderButton = findViewById(R.id.btnCancelOrder);
            Button rateButton = findViewById(R.id.btnRate);
            Button buyAgainButton = findViewById(R.id.btnReorder);
            Button returnOrderButton = findViewById(R.id.btnReturnOrder);

            orderIdTextView.setText("#" + order.getId() + " (" + order.getStatus() + ")");

            // Hiển thị tất cả sản phẩm
            productsContainer.removeAllViews(); // Xóa các view cũ để tránh trùng lặp
            for (Product product : order.getProducts()) {
                View productView = getLayoutInflater().inflate(R.layout.item_product_detail, productsContainer, false);
                TextView productNameTextView = productView.findViewById(R.id.tvProductName);
                TextView productPriceTextView = productView.findViewById(R.id.tvProductPrice);
                ImageView productImageView = productView.findViewById(R.id.imgProduct);

                productNameTextView.setText(product.getName());
                productPriceTextView.setText(product.getQuantity() + " x " + vietnameseFormat.format(product.getPrice()) + " đ");
                if (product.getImage() != null) {
                    productImageView.setImageBitmap(product.getImage());
                }

                productsContainer.addView(productView);
            }

            int totalProducts = order.getProducts().stream().mapToInt(Product::getQuantity).sum();
            double totalPrice = order.getProducts().stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
            totalItemsTextView.setText("Tổng tiền (" + totalProducts + " sản phẩm)");
            totalPriceTextView.setText(vietnameseFormat.format(totalPrice) + " đ");

            shippingInfoTextView.setText("Thông tin giao hàng: Địa chỉ: 123 Đường ABC, Quận 1, TP.HCM\nSĐT: 0909 123 456");
            paymentInfoTextView.setText("Thông tin thanh toán: Thanh toán khi nhận hàng\nTổng: " + vietnameseFormat.format(totalPrice) + " đ");

            cancelOrderButton.setVisibility(View.GONE);
            rateButton.setVisibility(View.GONE);
            buyAgainButton.setVisibility(View.GONE);
            returnOrderButton.setVisibility(View.GONE);

            switch (order.getStatus()) {
                case "Chờ xác nhận":
                    cancelOrderButton.setVisibility(View.VISIBLE);
                    break;
                case "Thành công":
                    rateButton.setVisibility(View.VISIBLE);
                    buyAgainButton.setVisibility(View.VISIBLE);
                    returnOrderButton.setVisibility(View.VISIBLE);
                    break;
                case "Đã trả":
                case "Đã hủy":
                    buyAgainButton.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            finish(); // Thoát nếu không tìm thấy order
        }
    }
}