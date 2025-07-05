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
                    if (productName != null && image != null) {
                        String categoryInfo = generateCategoryInfo(productName); // Thêm thông tin phân loại
                        orderMap.get(currentOrderId).getProducts().add(new Product(currentOrderId, productName, price, image, quantity, categoryInfo));
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        Order order = orderMap.get(orderId);
        if (order != null) {
            TextView orderIdTextView = findViewById(R.id.tvOrderId);
            LinearLayout productsContainer = findViewById(R.id.productsContainer);
            TextView shippingInfoTextView = findViewById(R.id.tvShippingInfo);
            TextView shippingAddressTextView = findViewById(R.id.shipping_address);
            TextView shippingPhoneTextView = findViewById(R.id.shipping_phone);
            TextView shippingEmailTextView = findViewById(R.id.shipping_email);
            TextView shippingServiceTextView = findViewById(R.id.shipping_service);
            TextView paymentInfoTextView = findViewById(R.id.tvPaymentInfo);
            TextView paymentMethodTextView = findViewById(R.id.payment_method);
            TextView paymentAmountTextView = findViewById(R.id.payment_amount);
            TextView paymentShippingTextView = findViewById(R.id.payment_shipping);
            TextView paymentDiscountTextView = findViewById(R.id.payment_discount);
            TextView paymentTotalTextView = findViewById(R.id.payment_total);
            Button cancelOrderButton = findViewById(R.id.btnCancelOrder);
            Button rateButton = findViewById(R.id.btnRate);
            Button buyAgainButton = findViewById(R.id.btnReorder);
            Button returnOrderButton = findViewById(R.id.btnReturnOrder);

            orderIdTextView.setText("Đơn hàng #" + order.getId() + " (" + order.getStatus() + ")");

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
                if (product.getImage() != null) {
                    productImageView.setImageBitmap(product.getImage());
                }

                productsContainer.addView(productView);
            }

            int totalProducts = order.getProducts().stream().mapToInt(Product::getQuantity).sum();
            double totalItemPrice = order.getProducts().stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
            double shippingFee = 0.0;
            double discount = 0.0;
            double totalPrice = totalItemPrice + shippingFee - discount;


            shippingInfoTextView.setText("Thông tin giao hàng");
            shippingAddressTextView.setText("Địa chỉ nhận hàng: 585 Trường Chinh phường Tân Thới Nhất Quận 12");
            shippingPhoneTextView.setText("Số điện thoại: 0813849476");
            shippingEmailTextView.setText("Email: khoatrinhtiendat@gmail.com");
            shippingServiceTextView.setText("Dịch vụ: không");
            paymentInfoTextView.setText("Thông tin thanh toán");
            paymentMethodTextView.setText("Hình thức thanh toán: Ví điện tử");
            paymentAmountTextView.setText("Tiền hàng: " + vietnameseFormat.format(totalItemPrice) + " đ");
            paymentShippingTextView.setText("Phí vận chuyển: " + vietnameseFormat.format(shippingFee) + " đ");
            paymentDiscountTextView.setText("Giảm giá: " + vietnameseFormat.format(discount) + " đ");
            paymentTotalTextView.setText("Tổng cộng: " + vietnameseFormat.format(totalPrice) + " đ");

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