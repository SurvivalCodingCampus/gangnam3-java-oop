package com.survivalcoding.day08.asset;

public abstract class TangibleAsset extends Asset
        implements Thing {

    // 상표권, 저작권, 라이선스 등
    private String category;
    private double weight;
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
