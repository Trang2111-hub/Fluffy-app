package com.fluffy.app.model;

import android.graphics.Bitmap;

public class Order {
    private int orderId;
    private String status;
    private int quantity;
    private double price;
    private String productName;
    private Bitmap productImage;

    public Order(int orderId, String status, String productName, double price, int quantity, Bitmap productImage) {
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Bitmap getProductImage() { return productImage; }
}