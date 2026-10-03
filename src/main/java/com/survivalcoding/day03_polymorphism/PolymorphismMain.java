package com.survivalcoding.day03_polymorphism;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.survivalcoding.day01_class_instance.Slime;

public class PolymorphismMain {

    public static void main(String[] args) {
        // 타입 Drawable 요소 생성
        final Drawable element = new House("서울시 강남구", 100, Color.WHITE);
        element.draw();

        // Drawable 목록 생성
        final List<Drawable> elements = new ArrayList<>();
        elements.add(new Dog("멍멍이", 3));
        elements.add(new House("서울시 강남구"));
        elements.add(new Tree(5.0));

        final Drawable drawable = new Rectangle(100, 50, Color.BLUE, BorderStyle.DASHED);
        elements.add(drawable);

        for (int i = 0; i < elements.size(); i++) {
            final Drawable d = elements.get(i);
            d.draw();
        }
        // elements.forEach((e) -> e.draw());

        // Character character = new Hero("홍길동", 100);
        // Sword sword = new Hero("name", 100);

        Human human = new Dancer("name", 100);
        human.speak();
        // human.dance();

        Wizard wizard = new Wizard("마법사", 50, new Wand("지팡이", 10));
        Character character2 = wizard;
        Slime slime = new Slime("A", 10);

        wizard.attack(slime);
        wizard.fireball(slime);

        character2.attack(slime);
        // character2.fireball(slime);

        KingSlime kingSlime = new KingSlime();
        Monster monster = new KingSlime();

        kingSlime.run();
        monster.run();

        // Monster monster2 = new KingSlime();
        // KingSlime kingSlime2 = (KingSlime) monster2; // 다운 캐스팅

        Character character3 = new Wizard("마법사", 50, new Wand("지팡이", 10));

        if (character3 instanceof Hero) {
            Hero hero = (Hero) character3;
            System.out.println(hero.getName() + "형변환 가능");
        } else {
            System.out.println("형변환 불가");
        }

        final List<Character> characters = new ArrayList<>();

        characters.add(new Hero("슈퍼맨", 100));
        characters.add(new Hero("배트맨", 200));
        characters.add(new Wizard("해리포터", 50, new Wand("지팡이", 10)));
        characters.add(new Wizard("헤르미온느", 50, new Wand("지팡이", 10)));

        for (Character c : characters) {
            c.setHp(c.getHp() + 50);
        }

        final List<Monster> monsters = new ArrayList<>();
        monsters.add(new KingSlime());
        monsters.add(new Goblin());

        for (Monster m : monsters) {
            m.run();
        }

        X obj = new A();

        obj.a();
        // obj.b();
        // obj.c();

        Y y1 = new A();
        Y y2 = new B();

        y1.a();
        y2.a();

        List<Y> yList = new ArrayList<>();
        yList.add(y1);
        yList.add(y2);

        yList.forEach((y) -> y.b());
    }
}
