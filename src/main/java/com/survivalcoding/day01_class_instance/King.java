package com.survivalcoding.day01_class_instance;

public class King {

    public void talk(Hero hero) {
        // System.out.println("왕 : 우리 성에 오서오시오. 용사 " + hero.name + "이여");
        System.out.println("왕 :  우리 성에 오서오시오. 용사 " + hero.getName() + "이여");

        System.out.println("왕 : 긴 여행에 피로하겠군");
        System.out.println("왕 : 우선 성 아랫 마을을 보고 와도 좋소. " + "그럼 또 봅시다");

        // hero.die();
        hero.bye();
    }

    public void callHero(Hero hero) {
        System.out.println("용사님, 저희 왕국에 와주셔서 감사합니다");
        System.out.println("용사님의 이름은 " + hero.getName() + "이고, hp는 " + hero.getHp() + "입니다");

        // hero.die();
        hero.bye();
    }
}