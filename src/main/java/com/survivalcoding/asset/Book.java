package com.survivalcoding.asset;

public class Book extends TangibleAsset {
    private String isbn;

    public Book(String name, int price, String color, String isbn, double weight) {
        super(name, price, color, weight);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
