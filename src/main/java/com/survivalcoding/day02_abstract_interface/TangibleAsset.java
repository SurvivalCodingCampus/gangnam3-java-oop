package com.survivalcoding.day02_abstract_interface;

public abstract class TangibleAsset extends Asset implements Thing {

    // field
    private String color;
    private double weight;

    // constructor
    public TangibleAsset() {
        this("무채색", MAX_WEIGHT / 2);
    }

    public TangibleAsset(double weight) {
        this("무채색", weight);
    }

    public TangibleAsset(String color, double weight) {
        validateWeight(weight);
        this.color = color;
        this.weight = weight;
    }

    // method
    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        validateWeight(weight);
        this.weight = weight;
    }

    private static void validateWeight(double weight) {
        if (Double.isNaN(weight) || weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException("설정할 무게는 " + MIN_WEIGHT + " 이상 " + MAX_WEIGHT + " 이하입니다");
        }
    }

    // getter
    public String getColor() {
        return color;
    }

    // setter
    public void setColor(String color) {
        this.color = color;
    }
}
