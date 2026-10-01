package com.survivalcoding.assetmanager;

public class Book extends TangibleAsset {
    private String isbn;
    
    public Book(String name) {
        super(name);
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
