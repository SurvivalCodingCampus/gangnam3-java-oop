package com;

public class Main {

    public static void main(String[] args) {

        Cleric cleric = new Cleric("세라핌");

        System.out.println("이름 : " + cleric.name);
        System.out.println("HP : " + cleric.hp);
        System.out.println("MP : " + cleric.mp);

        cleric.hp = 20;

        cleric.selfAid();

        System.out.println("HP : " + cleric.hp);
        System.out.println("MP : " + cleric.mp);

        int recovery = cleric.pray(3);

        System.out.println("회복된 MP : " + recovery);
        System.out.println("현재 MP : " + cleric.mp);
    }
}