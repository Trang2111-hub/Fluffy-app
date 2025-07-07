package com.fluffy.app.model;

public class AddressItem {
    private String name;
    private String phone;
    private String address;
    private boolean isDefault;

    public AddressItem(String name, String phone, String address, boolean isDefault) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isDefault() { return isDefault; }

    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}
