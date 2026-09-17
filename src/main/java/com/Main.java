package com;

import com.survivalcoding.Cleric;

public class Main {
    public static void main(String[] args) {
        // 성직자 생성
        Cleric cleric = new Cleric("세라핌");

        //전투로 HP가 감소했다고 가정
        cleric.hp = 20;


        System.out.println("마법 사용전");
        System.out.println("이름: " + cleric.name);
        System.out.println("HP : " + cleric);
        System.out.println("MP + cleric.mp");

        //셀프 에이드 사용
        cleric.selfAid();

        System.out.println("셀프 에이드 사용 후");
        System.out.println("이름 : " + cleric.name);
        System.out.println("HP : " + cleric.hp);
        System.out.println("MP : " + cleric.mp);
    }
}
