package com.survivalcoding;

public class Cleric {

    //성직자 클레릭을 생성
    String name;
    //현재 HP
    int hp = 50;
    //최대HP
    final int MAX_HP = 50;

    //현재HP
    int mp = 10;

    //최대MP
    final int MAX_MP = 10;

    //생산자
    public Cleric(String name) {
        this.name = name;
    }

    //셀프 에이드 마법
    void  selfAid() {
        mp -= 5;
        hp = MAX_HP;
    }
    //기도하기
    int pray(int second) {
        //0~2 사이의 랜덤 보정
        int recovery = second + (int)(Math.random() * 3);

        // 실제 회복 전 MP저장
        int beforeMp = mp;

        // MP회복
        mp += recovery;

        //최대MP를 넘지 않도록 제한
        if (mp > MAX_MP) {
            mp = MAX_MP;
        }

        //실제 회복된 MP양을 계산
        int actualRecovery = mp - beforeMp;

        //실제 회복량 반환
        return  actualRecovery;
    }
}
