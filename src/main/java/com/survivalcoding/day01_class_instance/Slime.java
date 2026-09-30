package com.survivalcoding.day01_class_instance;

public class Slime {
    final String suffix;
    private int hp;


    // 생성자 작성

    public Slime() {       // (질문) 기본생성자가 없으면 안되는걸까요... 메인메서드에서 꼬임
        this("지정", 100);
    }

    public Slime(String suffix, int hp) {       //suffix, hp 입력될 때
        this.suffix = suffix;
        this.hp = hp;
    }

    public Slime(String suffix) {       // suffix만 입력될 때
        this(suffix, 100);
    }




    // getter, setter 작성
    public String getSuffix() {
        return suffix;
    }

    public int getHp() {
        return hp;
    }

    public void setHp() {       // HP 기본값 초기화 = 100
        this.hp = 100;
    }

    public void setHp(int hp) {      // HP 입력 시 = 입력값
        this.hp = hp;
    }


    // 메서드 = 공격
    public void attack(Hero hero) {
        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println("10d의 데미지");

        hero.setHp(hero.getHp() - 10);
    }

}
