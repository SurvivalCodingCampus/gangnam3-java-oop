package com.survivalcoding.day01_class_instance;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int i = 10;
        String name = "홍길동";

        int min = Math.min(10, 5);

        Hero hero = new Hero();
        hero.name = "오준석";

        System.out.println(hero.hp);

        Hero hero2 = new Hero("오준석");
        Hero hero3 = new Hero(100);
    }
}