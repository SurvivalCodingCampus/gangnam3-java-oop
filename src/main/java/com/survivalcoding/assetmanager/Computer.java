package com.survivalcoding.assetmanager;

public class Computer extends TangibleAsset {
    private String makerName;
    
    public Computer(String name) {
        super(name);
    }
    
    public String getMakerName() {
        return makerName;
    }
    
    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
}
