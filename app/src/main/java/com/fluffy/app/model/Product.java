package com.fluffy.app.model;

import android.graphics.Bitmap;
import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private double price;
    private Bitmap image;
    private int quantity;

    public Product(int id, String name, double price, Bitmap image, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Bitmap getImage() { return image; }
    public int getQuantity() { return quantity; }
}