package com.survivalcoding;

import java.time.LocalDate;

import com.survivalcoding.day01_class_instance.Cleric;
import com.survivalcoding.day01_class_instance.Hero;
import com.survivalcoding.day01_class_instance.Person;
import com.survivalcoding.day01_class_instance.Slime;
import com.survivalcoding.day01_class_instance.Sword;
import com.survivalcoding.day01_class_instance.Wand;
import com.survivalcoding.day01_class_instance.Wizard;

public class Main {
    public static void main(String[] args) {
        // 가상 세계에 용사를 생성
        Hero hero = new Hero("김영웅");

        // 가상 세계에 슬라임을 생성
        Slime slime1 = new Slime("슬라임A", 50);
        Slime slime2 = new Slime("슬라임B", 38);

        // 생성된 용사에게 최초의 HP 와 이름을 설정
        hero.setName("Hero");
        hero.setHp(100);
        System.out.println("용사 " + hero.getName() + " 를 생성했습니다!");

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

        System.out.println(cleric.getMp());
        System.out.println("회복량: " + cleric.pray(5));
        System.out.println(cleric.getMp());

        Sword sword = new Sword("불의 검", 10);

        hero.setName("김영웅");
        hero.setHp(100);
        hero.setSword(sword);

        System.out.println("현재의 무기는 " + hero.getSword().getName());

        Hero hero1 = new Hero("김영웅1");

        hero1.setName("스랄");
        hero1.setHp(100);

        Hero hero2 = new Hero("김영웅2");

        hero2.setName("아서스");

        Wand wand = new Wand("나무", 10.0);
        Wizard wizard = new Wizard("제이나", 50, 10, wand);

        wizard.heal(hero1);
        wizard.heal(hero2);
        wizard.heal(hero2);

        System.out.println(Hero.money);
        Hero.money = 200;
        System.out.println(Hero.money);

        Hero.setRandomMoney();
        System.out.println(Hero.money);

        String name = " ";
        System.out.println(name.isEmpty());

        Person person = new Person("김준기", 1996);

        System.out.println(person.getName());
        System.out.println(person.getBirthYear());
        System.out.println(person.getAge(LocalDate.now()));
    }
}