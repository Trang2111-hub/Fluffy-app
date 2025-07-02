package com.fluffy.app.model;

public class Product {
    private int productId;
    private String productName;
    private double price;
    private String description;
    private byte[] image;

    public Product(String productName, double price, String description, byte[] image) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
}