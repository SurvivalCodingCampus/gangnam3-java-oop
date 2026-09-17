package com.survivalcoding;

import com.survivalcoding.day01_class_instance.Cleric;
import com.survivalcoding.day01_class_instance.Hero;
import com.survivalcoding.day01_class_instance.Slime;
import com.survivalcoding.day01_class_instance.Sword;
import com.survivalcoding.day01_class_instance.Wizard;

public class Main {
    public static void main(String[] args) {
        // 가상 세계에 용사를 생성
        Hero hero = new Hero();

        // 가상 세계에 슬라임을 생성
        Slime slime1 = new Slime();
        Slime slime2 = new Slime();

        // 생성된 용사에게 최초의 HP 와 이름을 설정
        hero.name = "Hero";
        hero.hp = 100;
        System.out.println("용사 " + hero.name + " 를 생성했습니다!");

        // 생성된 슬라임에게 최초의 HP 와 이름을 설정
        slime1.hp = 50;
        slime1.suffix = "A";

        slime2.hp = 48;
        slime2.suffix = "B";

        // 용사에게 '5초 앉기', '넘어지기', '25초 않기', '도망' 을 지시
        hero.sit(5);
        hero.slip();
        hero.sit(25);
        hero.run();

        // 슬라임에게 '도망' 을 지시
        slime1.run();
        slime2.run();

        Cleric cleric = new Cleric("클레릭");
        cleric.selfAid();

        System.out.println(cleric.mp);
        System.out.println("회복량: " + cleric.pray(5));
        System.out.println(cleric.mp);

        Sword sword = new Sword();

        sword.name = "불의 검";
        sword.damage = 10;

        hero.name = "김영웅";
        hero.hp = 100;
        hero.hp = 100;
        hero.sword = sword;

        System.out.println("현재의 무기는 " + hero.sword.name);

        Hero hero1 = new Hero();

        hero1.name = "스랄";
        hero1.hp = 100;

        Hero hero2 = new Hero();

        hero2.name = "아서스";

        Wizard wizard = new Wizard();

        wizard.name = "제이나";
        wizard.hp = 50;

        wizard.heal(hero1);
        wizard.heal(hero2);
        wizard.heal(hero2);

        System.out.println(Hero.money);
        Hero.money = 200;
        System.out.println(Hero.money);

        Hero.setRandomMoney();
        System.out.println(Hero.money);
    }
}