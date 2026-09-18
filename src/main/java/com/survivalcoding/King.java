package com.survivalcoding;

public class King {
    void callHero(Hero hero) {
        hero.getName();

        System.out.println("용사님, 저희 왕국에 와주셔서 감사합니다");

        //Getter 메서드를 통해 안전하게 값에 접근
        System.out.println("용사님의 이름은" + hero.getName() + "이고, hp는 " + hero.getHp() + "입니다");
        hero.bye();
        
    }

    void talk(Hero hero) {
        System.out.println("왕 : 우리 성에 어서오시오. 용사" + hero.getName() + "이여");

        System.out.println("왕 : 긴 여행에 피로하겠군");
        System.out.println("왕 : 우선 아랫 마을을 보고 와도 좋소." + "그럼 또 봅시다");
        hero.run();
    }
}
