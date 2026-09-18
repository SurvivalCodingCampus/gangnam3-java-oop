package com.survivalcoding;

//
public class wizard {
    private int hp;
    private int mp;
    private String name;
    private wand wand;

    //private 하면 같은 클래스에서 접근 가능{인스턴스는 다른애기}
    //get은 인자 X 일반함수 앞에 get을 쓰면 오류간다.
    void heal(Cleric cleric) {
//private method 안에 private 못 만듬
//private int basepoint = 10;
        int basePoint = 10;
        int recovPoint = (int) (basePoint * this.wand.getpower());
        Cleric.setHp(Cleric.getHp() + recovPoint);
    }
}
