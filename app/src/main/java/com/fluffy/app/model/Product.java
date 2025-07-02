package com.fluffy.app.model;

public class Product {
    private String name;
    private String discountPrice;
    private String originalPrice;
    private String imageUrl;

    // Constructor
    public Product(String name, String discountPrice, String originalPrice, String imageUrl) {
        this.name = name;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
