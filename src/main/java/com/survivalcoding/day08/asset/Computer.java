package com.survivalcoding.day08.asset;

public class Computer extends TangibleAsset {

    private String makerName;

    public Computer(String name, String price, String category, double weight, String color, String makerName) {
        super(name, price, category, weight, color);
        this.makerName = makerName;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
}
