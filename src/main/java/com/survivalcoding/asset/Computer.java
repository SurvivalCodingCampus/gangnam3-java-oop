package com.survivalcoding.asset;

public class Computer extends TangibleAsset {
    String makerName;

    public Computer(String makerName) {
        this.makerName = makerName;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

}
