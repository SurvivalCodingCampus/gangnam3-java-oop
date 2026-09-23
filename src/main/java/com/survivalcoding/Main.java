package com.survivalcoding;

import com.survivalcoding.day01_class_instance.Cleric;
import com.survivalcoding.day01_class_instance.Hero;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    // 메인 메서드 (시작점)
    public static void main(String[] args) {

        // 용사
        String heroName = "용사";
        int heroHp = 100;

        // 클레릭
        String clericName = "클레릭";
        int clericHp = 150;

//        something(heroName, heroHp, d, dd, d,d,d ,);

        Hero hero = new Hero();
        something2(hero);

//        Cleric cleric = new Cleric();
    }

    // 용사의 hp 를 10 증가
    public static void something(String name, int hp) {
        // 뭔가를
    }

    public static void something2(Hero hero) {
    }

}