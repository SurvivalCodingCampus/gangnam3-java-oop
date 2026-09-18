package com.survivalcoding.day04_calss_instance;

public class King {
    public void talk(Hero hero) {
        System.out.printf("왕 : 우리 성에 어서 오시오 용사 %n이여", hero.getName());
        System.out.println("왕 : 긴 여행에 피로하겠군");
        System.out.println("왕 : 우선 성 아랫 마을을 보고 와도 좋소. 그럼 또 봅시다");
    }
    
    public void callHero(Hero hero) {
        System.out.println("왕 : 용사님, 저희 왕국에 와주셔서 감사합니다");
        System.out.printf("왕 : 용사님의 이름은 %s이고, hp는 %d입니다%n", hero.getName(), hero.getHp());
        hero.bye();
    }
}
