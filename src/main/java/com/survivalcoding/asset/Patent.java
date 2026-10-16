package com.survivalcoding.asset;

public class Patent extends IntangibleAsset {
    private String PatentNumber;

    public Patent(String name, int price, String right, String PatentNumber) {
        super(name, price, right);
        this.PatentNumber = PatentNumber;
    }

    public String getPatentNumber() {
        return PatentNumber;
    }

    public void setPatentNumber(String patentNumber) {
        PatentNumber = patentNumber;
    }
}

