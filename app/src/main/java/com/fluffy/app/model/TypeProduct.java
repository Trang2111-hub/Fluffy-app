package com.fluffy.app.model;

public class TypeProduct {
    private int Id;
    private int Img;
    private String NameType;

    public TypeProduct( String nameType, int img) {
        Img = img;
        NameType = nameType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }

    public String getNameType() {
        return NameType;
    }

    public void setNameType(String nameType) {
        NameType = nameType;
    }
}
