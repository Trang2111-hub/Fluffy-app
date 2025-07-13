package com.fluffy.app.model;

public class CartItem {
    private String name;
    private String variant;
    private int price;
    private int oldPrice;
    private int quantity;
    private boolean checked;

    public CartItem(String name, String variant, int price, int oldPrice, int quantity, boolean checked) {
        this.name = name;
        this.variant = variant;
        this.price = price;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
        this.checked = checked;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getVariant() { return variant; }
    public int getPrice() { return price; }
    public int getOldPrice() { return oldPrice; }
    public int getQuantity() { return quantity; }
    public boolean isChecked() { return checked; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setChecked(boolean checked) { this.checked = checked; }
}

