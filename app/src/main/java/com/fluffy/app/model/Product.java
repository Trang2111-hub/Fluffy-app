package com.fluffy.app.model;

import android.graphics.Bitmap;

public class Product {
    private int orderId;
    private String name;
    private double price;
    private Bitmap image;
    private int quantity;
    private String categoryInfo;

    public Product(int orderId, String name, double price, Bitmap image, int quantity) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.categoryInfo = "";
    }

    public Product(int orderId, String name, double price, Bitmap image, int quantity, String categoryInfo) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.categoryInfo = categoryInfo != null ? categoryInfo : "";
    }

    // Getters và Setters
    public int getOrderId() { return orderId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Bitmap getImage() { return image; }
    public int getQuantity() { return quantity; }
    public String getCategoryInfo() { return categoryInfo; }
    public void setCategoryInfo(String categoryInfo) { this.categoryInfo = categoryInfo != null ? categoryInfo : ""; }
}