package com.survivalcoding;

//
public class wizard {
    public int MIN_NAME_LENGTH = 3;
    private int hp;
    private int mp;
    private String name;
    private wand wand;

    public int getmp() {
        return mp;
    }

    ;

    public void setmp(int mp1) {
        mp1 = getmp();

    }

    public int gethp() {
        return hp;
    }

    ;

    public void sethp(int hp1) {
        hp = hp1;
    }

    ;

    public String getName() {
        if (name.length() <= 3) {
            throw new IllegalArgumentException("글자수가 3개 이하입니다");
        } else if (name == null) {
            throw new IllegalArgumentException("글자가 null 입니다");
        } else {
            return name;
        }
    }

    public void checkHp() {
        if (gethp() < 0) {
            sethp(0);
        }
    }

    public void checkMP() {
        if (getmp() < 0) {
            throw new IllegalArgumentException("유효하지 않는 마나 범위입니다");
        }
    }

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
