package com.survivalcoding.asset;

public abstract class TangibleAsset implements Thing {
    private double weight;
    private String name;
    private int price;
    private String color;

    public TangibleAsset(String name, int price, String color, double weight) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
