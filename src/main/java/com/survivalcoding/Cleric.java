<<<<<<< HEAD
package com.survivalcoding;

public class Cleric {

    public static final int MAX_HP = 50;
    public static final int MAX_MP = 10;

    public String name;
    public int hp = 50;
    public int mp = 10;

    // 이름, HP, MP를 모두 지정
    public Cleric(final String name, final int hp, final int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 이름과 HP만 지정
    // MP는 MAX_MP로 초기화
    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    // 이름만 지정
    // HP와 MP는 각각 최대치로 초기화
    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    // 자기 자신을 회복
    public void selfAid() {
        mp -= 5;
        hp = MAX_HP;
    }

    // 기도
    public int pray(int second) {
        int recovery = second + (int) (Math.random() * 3);

        int beforeMp = mp;

        mp += recovery;

        if (mp > MAX_MP) {
            mp = MAX_MP;
        }

        return mp - beforeMp;
    }
}
=======
package com.survivalcoding;

public class Cleric {

    public static void main(String[] args) {
        Cleric cleric = new Cleric("홍길동");
        System.out.println(cleric.name);
        System.out.println(cleric.hp);
        System.out.println(cleric.mp);



    final int MAX_HP = 50;
    final int MAX_MP = 10;
    final int SELF_AID_COST = 5;

    String name;
    int hp = MAX_HP;
    int mp = MAX_MP;

    public Cleric(String name) {
        this.name = name;
    }
}


    public void selfAid() {
        // 회복 행동을 작성합니다.
        if (mp <5) {
            return;
        }
        // 원래 해야할 일
        hp = 100;
        mp = 100;
    }
    public int pray(int seconds) {
        return 0;
    }
    public Cleric(String name) {
        this.name = name;
    }
    public String getName() {
        return  name + "킹왕짱";
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getHp() {
        return  hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getMp() {
        return  mp;
    }
    public void setMp(int mp) {
        this.mp = mp;
    }
    public static void something(String name, int hp, int mp) {

    }
    private  static void something(int hp, int mp) {
    }
    private   static void something() {
    }

    public static void main() {

    }


}
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
