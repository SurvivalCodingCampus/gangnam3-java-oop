package com.survivalcoding.day08.asset;

public abstract class TangibleAsset extends Asset
        implements Thing {

    // 상표권, 저작권, 라이선스 정
    private String category;
    private double weight;
    private String color;

    public TangibleAsset(String name, String price, String category, double weight, String color) {
        super(name, price);
        this.category = category;
        this.weight = weight;
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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
