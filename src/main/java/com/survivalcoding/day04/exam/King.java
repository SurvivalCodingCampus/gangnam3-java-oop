package com.survivalcoding.day04.exam;

public class King {
    public void callHero(final Hero hero) {
        System.out.println("용사님, 저희 왕국에 와주셔서 감사합니다!");
        System.out.println("용사님의 이름은 " + hero.getName()
                + "이고, hp는 " + hero.getHp() + "입니다");
    }
}
