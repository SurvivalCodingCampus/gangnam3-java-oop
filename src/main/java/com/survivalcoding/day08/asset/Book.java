package com.survivalcoding.day08.asset;

public class Book extends TangibleAsset {

    private String isbn;

    public Book(String name, String price, String category, double weight, String color, String isbn) {
        super(name, price, category, weight, color);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
