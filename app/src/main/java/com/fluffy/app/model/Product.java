package com.fluffy.app.model;

import android.graphics.Bitmap;

import java.util.List;

public class Product {
    private int productId; // Từ JSON
    private int orderId; // Từ bạn
    private String name;
    private String discountPrice; // Từ JSON
    private String originalPrice; // Từ JSON
    private String imageUrl; // Từ JSON, thay Bitmap
    private double price; // Từ bạn
    private Bitmap image; // Từ bạn, nếu cần lưu cục bộ
    private int quantity; // Từ bạn
    private String categoryInfo; // Từ bạn
    private double rating; // Từ JSON
    private List<String> colors; // Từ JSON
    private List<String> sizes; // Từ JSON
    private String description; // Từ JSON
    private String collection; // Từ JSON

    // Constructor đầy đủ
    public Product(int productId, int orderId, String name, String discountPrice, String originalPrice,
                   String imageUrl, double price, Bitmap image, int quantity, String categoryInfo,
                   double rating, List<String> colors, List<String> sizes, String description, String collection) {
        this.productId = productId;
        this.orderId = orderId;
        this.name = name;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.categoryInfo = categoryInfo != null ? categoryInfo : "";
        this.rating = rating;
        this.colors = colors;
        this.sizes = sizes;
        this.description = description;
        this.collection = collection;
    }


    // Getters và Setters
    public int getProductId() { return productId; }
    public int getOrderId() { return orderId; }
    public String getName() { return name; }
    public String getDiscountPrice() { return discountPrice; }
    public String getOriginalPrice() { return originalPrice; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public Bitmap getImage() { return image; }
    public int getQuantity() { return quantity; }
    public String getCategoryInfo() { return categoryInfo; }
    public void setCategoryInfo(String categoryInfo) { this.categoryInfo = categoryInfo != null ? categoryInfo : ""; }
    public double getRating() { return rating; }
    public List<String> getColors() { return colors; }
    public List<String> getSizes() { return sizes; }
    public String getDescription() { return description; }
    public String getCollection() { return collection; }
}