package com.fluffy.app.model;

public class Feedback {
    private int productId;
    private int customerId;
    private String content;
    private String date;
    private String typeProduct;
    private int rating;
    private String userName;
    public Feedback(int productId, String userName,int customerId, String content, String date, String typeProduct, int rating) {
        this.productId = productId;
        this.customerId = customerId;
        this.content = content;
        this.date = date;
        this.typeProduct = typeProduct;
        this.rating = rating;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getProductId() { return productId; }
    public int getCustomerId() { return customerId; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public String getTypeProduct() { return typeProduct; }
    public int getRating() { return rating; }
}

