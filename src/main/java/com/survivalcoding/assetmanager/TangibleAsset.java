package com.survivalcoding.assetmanager;

public abstract class TangibleAsset extends Asset implements Thing {
    int price;
    String color;
    double weight;
    
    @Override
    public double getWeight() {
        return weight;
    }
    
    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
