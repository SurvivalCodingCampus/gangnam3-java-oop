package com.survivalcoding;

<<<<<<< HEAD
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
=======
import com.survivalcoding.day01_class_instance.Hero;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int i = 10;
        String name = "홍길동";

<<<<<<< HEAD
        Hero hero = new Hero();
        Hero hero2 = new Hero();
>>>>>>> 67a90eb (feat: 2026.09.24 박강원_Slime.java)
=======
        SuperHero superhero = new SuperHero("한석봉", 50);
        superhero.run();

    }

    // 용사의 hp를 10 증가.
    public  static void something(String name, int hp) {

>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
    }
}