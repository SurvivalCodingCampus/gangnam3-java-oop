package com.survivalcoding.asset;

public abstract class IntangibleAsset extends Asset {
    private String right;

    public IntangibleAsset(String name, int price, String right) {
        super(name, price);
        this.right = right;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}

