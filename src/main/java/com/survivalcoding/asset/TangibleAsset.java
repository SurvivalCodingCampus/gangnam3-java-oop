package com.survivalcoding.asset;

public abstract class TangibleAsset extends Asset implements Thing {
    private double weight;
    private String color;

    public TangibleAsset(String name, int price, String color, double weight) {
        super(name, price);
        this.color = color;
        this.weight = weight;
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
