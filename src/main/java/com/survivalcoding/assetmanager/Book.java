package com.survivalcoding.assetmanager;

public class Book extends TangibleAsset {
    private String isbn;
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
