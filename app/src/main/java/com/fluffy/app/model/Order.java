package com.fluffy.app.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String status;
    private List<Product> products;

    public Order(int id, String status, List<Product> products) {
        this.id = id;
        this.status = status;
        this.products = products;
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public List<Product> getProducts() { return products; }
}