package com.survivalcoding.day01_class_instance;

import java.util.Random;

public class Hero {

    // constant
    public static final int DEFAULT_HP = 100;

    // field
    private String name;
    private int hp;
    private Sword sword;

    public static int money = 100;

    // constructor
    public Hero(String name) {
        this(name, new Sword("불의 검", 10));
        System.out.println("Hero 생성자 호출");
    }

    public Hero(String name, Sword sword) {
        setName(name);
        setHp(DEFAULT_HP);
        setSword(sword);
    }

    // method
    public static void setRandomMoney() {
        money = new Random().nextInt(1000);
    }

    public void bye() {
        System.out.println("용자는 이별을 고했다");
    }

    private void die() {
        System.out.println(this.name + "는 죽었다");
        System.out.println("Game Over");
    }

    public void attack(Kinoko enemy) {
        System.out.println(this.name + "의 공격!");
        System.out.println("괴물 버섯" + enemy.getSuffix() + "로부터 2포인트의 반격을 받았다");

        int returned = 2;
        this.takeDamage(returned);
    };

    public void run() {
        System.out.println(this.name + "는 도망쳤다!");
        System.out.println("최종 HP는 " + this.hp + "입니다");
    };

    public void sit(int sec) {
        this.setHp(this.hp + sec);
        System.out.println(this.name + "는 " + sec + "초 앉았다");
        System.out.println("HP가 " + sec + "포인트 회복되었다");
    };

    public void slip() {
        int damage = 5;
        takeDamage(damage);
        System.out.println(this.name + "는 넘어졌다!");
        System.out.println("5의 데미지!");
    };

    public void sleep() {
        setHp(DEFAULT_HP);
        System.out.println(this.name + "는 잠을 자고 회복했다!");
    };

    public void takeDamage(int damage) {
        int beforeHp = this.hp;
        setHp(this.hp - damage);
        if (beforeHp > 0 && this.hp == 0) {
            die();
        }
    }

    // getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public Sword getSword() {
        return sword;
    }

    // setter
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name(이름)은 null이 아니어야 합니다");
        }

        if (name.length() <= 1) {
            throw new IllegalArgumentException("name(이름)은 최소 2글자 이상을 입력해 주세요");
        }

        if (name.length() >= 8) {
            throw new IllegalArgumentException("name(이름)은 8글자 이상을 넘길 수 없습니다");
        }

        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }
}