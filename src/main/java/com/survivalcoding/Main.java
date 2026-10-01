package com.survivalcoding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.survivalcoding.day01_class_instance.Cleric;
import com.survivalcoding.day01_class_instance.GreatWizard;
import com.survivalcoding.day01_class_instance.Hero;
import com.survivalcoding.day01_class_instance.Kinoko;
import com.survivalcoding.day01_class_instance.Person;
import com.survivalcoding.day01_class_instance.PoisonSlime;
import com.survivalcoding.day01_class_instance.Slime;
import com.survivalcoding.day01_class_instance.SuperHero;
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

        Wand wand = new Wand("나무지팡이", 10.0);
        Wizard wizard = new Wizard("제이나", 50, wand);

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

        SuperHero superHero = new SuperHero("홍길동", sword, true);
        Kinoko kinoko = new Kinoko("괴물 버섯", 10);
        PoisonSlime poisonSlime = new PoisonSlime("A");

        superHero.run();
        superHero.attack(kinoko);

        poisonSlime.run();

        for (int i = 0; i < 6; i++) {
            poisonSlime.attack(superHero);
        }

        System.out.println("현재 용사 HP: " + superHero.getHp());

        GreatWizard greatWizard = new GreatWizard();

        greatWizard.heal(superHero);
        greatWizard.superHeal(superHero);

        // 배열 생성
        // String[] names = new String[3];

        // 3인 추가
        // names[0] = "홍길동";
        // names[1] = "한석봉";
        // names[2] = "신사임당";

        // System.out.println(names[1]);

        // ArrayList 생성
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();

        // 3인 추가
        names.add("홍길동");
        names.add("한석봉");
        names.add("신사임당");

        numbers.add(10);
        numbers.add(20);
        numbers.add(30);

        System.out.println(names.get(1));
        System.out.println(numbers.get(1));

        // for (int i = 0; i < names.size(); i++) {
        // System.out.println(names.get(i));
        // }

        for (String n : names) {
            System.out.println(n);
        }

        Iterator<String> iterator = names.iterator();

        while (iterator.hasNext()) {
            String it = iterator.next();
            System.out.println(it);
        }

        Set<String> colors = new HashSet<>();

        colors.add("red");
        colors.add("green");
        colors.add("blue");

        colors.add("red");

        System.out.println(colors.size());

        Map<String, Integer> cities = new HashMap<>();

        cities.put("서울시", 977);
        cities.put("수원시", 124);
        cities.put("부산시", 342);

        int seoul = cities.get("서울시");

        System.out.println("서울시 인구는 " + seoul + "만");
        cities.remove("서울시");
        cities.put("수원시", 130);
        System.out.println("수원시 인구는 " + cities.get("수원시") + "만");

        for (String key : cities.keySet()) {
            int value = cities.get(key);
            System.out.println(key + " 인구는 " + value + "만");
        }

        Hero hero3 = new Hero("김영웅3");
        hero3.setName("홍길동");

        List<Hero> heroList = new ArrayList<>();

        heroList.add(hero3);
        hero3.setName("한석봉");

        System.out.println(heroList.get(0).getName());
    }
}