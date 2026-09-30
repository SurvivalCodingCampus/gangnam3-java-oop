package com.survivalcoding.day08.asset;

public abstract class TangibleAsset extends Asset
        implements Thing {

    protected double weight;
    protected String name;
    protected int price;
    protected String color;

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {

        if (weight <= 0) {
            throw new IllegalArgumentException("무게는 0초과 해야 함");
        }

        this.weight = weight;
    }
}
