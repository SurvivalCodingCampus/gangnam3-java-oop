package com.survivalcoding.day02_abstract_interface;

public abstract class Asset {

    // field
    private String name;
    private int price;

    // getter
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
