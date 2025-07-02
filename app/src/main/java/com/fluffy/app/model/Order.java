package com.fluffy.app.model;

public class Order {

    private String orderId; // Mã đơn hàng
    private String status; // Trạng thái đơn hàng (Chờ xác nhận, Đang chuẩn bị hàng, ...)
    private String productName; // Tên sản phẩm
    private String productInfo; // Thông tin về sản phẩm (màu sắc, kích thước...)
    private int quantity; // Số lượng sản phẩm
    private double price; // Giá sản phẩm
    private double totalAmount; // Tổng tiền
    private boolean isCancelable; // Kiểm tra xem đơn hàng có thể hủy hay không

    // Constructor
    public Order(String orderId, String status, String productName, String productInfo, int quantity, double price, boolean isCancelable) {
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.productInfo = productInfo;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = quantity * price;
        this.isCancelable = isCancelable;
    }

    // Getter and Setter
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalAmount = this.quantity * this.price; // Cập nhật tổng tiền khi thay đổi số lượng
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.totalAmount = this.quantity * this.price; // Cập nhật tổng tiền khi thay đổi giá
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }
}
