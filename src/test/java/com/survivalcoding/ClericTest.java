package com;

public class Main {

    public static void main(String[] args) {

        // ========================================
        // 1. 이름만 지정하는 생성자 테스트
        // HP와 MP는 최대치로 초기화
        // ========================================
        Cleric cleric1 = new Cleric("아서스");

        System.out.println("===== 1. 이름만 지정 =====");
        System.out.println("이름 : " + cleric1.name);
        System.out.println("HP   : " + cleric1.hp);
        System.out.println("MP   : " + cleric1.mp);

        // ========================================
        // 2. 이름과 HP를 지정하는 생성자 테스트
        // MP는 MAX_MP로 초기화
        // ========================================
        Cleric cleric2 = new Cleric("세라핌", 30);

        System.out.println();
        System.out.println("===== 2. 이름 + HP 지정 =====");
        System.out.println("이름 : " + cleric2.name);
        System.out.println("HP   : " + cleric2.hp);
        System.out.println("MP   : " + cleric2.mp);

        // ========================================
        // 3. 이름, HP, MP를 모두 지정하는 생성자 테스트
        // ========================================
        Cleric cleric3 = new Cleric("우서", 40, 5);

        System.out.println();
        System.out.println("===== 3. 이름 + HP + MP 지정 =====");
        System.out.println("이름 : " + cleric3.name);
        System.out.println("HP   : " + cleric3.hp);
        System.out.println("MP   : " + cleric3.mp);

        // ========================================
        // 4. selfAid() 테스트
        // MP 5를 사용하고 HP를 MAX_HP로 회복
        // ========================================
        System.out.println();
        System.out.println("===== 4. selfAid() 테스트 =====");

        cleric3.hp = 10;
        cleric3.mp = 10;

        System.out.println("사용 전 HP : " + cleric3.hp);
        System.out.println("사용 전 MP : " + cleric3.mp);

        cleric3.selfAid();

        System.out.println("사용 후 HP : " + cleric3.hp);
        System.out.println("사용 후 MP : " + cleric3.mp);

        // ========================================
        // 5. pray() 테스트
        // MP를 회복하지만 MAX_MP를 초과하지 않음
        // ========================================
        System.out.println();
        System.out.println("===== 5. pray() 테스트 =====");

        cleric3.mp = 5;

        System.out.println("기도 전 MP : " + cleric3.mp);

        int recovery = cleric3.pray(3);

        System.out.println("회복량     : " + recovery);
        System.out.println("기도 후 MP : " + cleric3.mp);

        // ========================================
        // 6. MP가 MAX_MP를 초과하지 않는지 테스트
        // ========================================
        System.out.println();
        System.out.println("===== 6. MP 최대치 테스트 =====");

        cleric3.mp = 9;

        System.out.println("기도 전 MP : " + cleric3.mp);

        int recovery2 = cleric3.pray(10);

        System.out.println("회복량     : " + recovery2);
        System.out.println("기도 후 MP : " + cleric3.mp);
        System.out.println("MAX_MP     : " + Cleric.MAX_MP);
    }
}