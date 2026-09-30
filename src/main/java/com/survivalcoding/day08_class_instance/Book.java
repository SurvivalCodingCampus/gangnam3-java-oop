package com.survivalcoding.day08_class_instance;

public abstract class Book {
    String name;
    int price;
    String color;
    String isbn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Book(String name, int price, String color, String isbn) {
        this.name = name;
        this.price = price;
        this.color = color;
        this.isbn = isbn;
    }
}
