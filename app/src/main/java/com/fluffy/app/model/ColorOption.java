package com.fluffy.app.model;
public class ColorOption {
    private int imageResId; // drawable resource ID
    private String name;

    public ColorOption( String name , int imageResId) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }
}
