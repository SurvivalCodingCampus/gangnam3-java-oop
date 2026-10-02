package com.survivalcoding.asset;

import com.survivalcoding.day01_class_instance.Hero;

public abstract class AssetMain {
    public static void main(String[] args) {
        // book
        TangibleAsset tangibleAsset = new Book("삼성", 100, "빨강", "123123", 3.0);

        // 안전
        if (tangibleAsset instanceof Computer) {
            Computer book = (Computer) tangibleAsset;
            System.out.println(book.getName());
        } else if (tangibleAsset instanceof Book) {
            Book book = (Book) tangibleAsset;
            System.out.println(book.getName());
        }

        System.out.println(tangibleAsset);

        Hero hero = new Hero("홍길동");
        System.out.println(hero);

        Hero hero2 = new Hero("한석봉");
        System.out.println(hero2);

    }
}
