package com.survivalcoding.assetmanager;

public abstract class Asset {
    protected String name;
    protected int price;
    
    public Asset(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}