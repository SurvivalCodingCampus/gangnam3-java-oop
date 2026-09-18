package com.survivalcoding;

public class Main {
    public static void main(String[] args) {

        // 가상 세계에 용사를 생성
        Hero hero = new Hero();

        // 생성된 용사에게 최초의 HP와 이름을 설정
        hero.setName("준석");
        hero.setHp(100);

        System.out.println(
                "용사님의 이름은 " + hero.getName()
                        + "이고, hp는 " + hero.getHp() + "입니다"
        );

        // 가상 세계에 버섯을 생성
        Kinoko kinoko = new Kinoko();
        kinoko.suffix = "A";

        // 슬라임 A 생성
        Slime slime1 = new Slime();
        slime1.hp = 50;
        slime1.suffix = "A";

        // 슬라임 B 생성
        Slime slime2 = new Slime();
        slime2.hp = 48;
        slime2.suffix = "B";

        // 용사에게 '5초 앉기', '넘어지기', '25초 앉기', '도망'을 지시
        hero.sit(5);
        hero.slip();
        hero.sit(25);
        hero.run();

        // 모험의 시작
        hero.slip();
    }
}