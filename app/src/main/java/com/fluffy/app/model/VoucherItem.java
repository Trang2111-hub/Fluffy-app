package com.fluffy.app.model;

public class VoucherItem {
    private String title;
    private String discountText;
    private String minOrderText;
    private String validUntil;
    private boolean isNew;

    public VoucherItem(String title, String discountText, String minOrderText, String validUntil, boolean isNew) {
        this.title = title;
        this.discountText = discountText;
        this.minOrderText = minOrderText;
        this.validUntil = validUntil;
        this.isNew = isNew;
    }

    public String getTitle() { return title; }
    public String getDiscountText() { return discountText; }
    public String getMinOrderText() { return minOrderText; }
    public String getValidUntil() { return validUntil; }
    public boolean isNew() { return isNew; }
}
