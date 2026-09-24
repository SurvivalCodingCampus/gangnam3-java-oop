package com.survivalcoding;

public class Wizard {

<<<<<<< HEAD
    private String name;
    private int hp;
    private int mp;
    private Wand wand;

    // 이름 getter
=======
    // 필드
    private String name;
    private int hp;
    private int mp = 100;
    private Wand wand;


    // 이름
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
    public String getName() {
        return this.name;
    }

<<<<<<< HEAD
    // 이름 setter
    public void setName(String name) {

=======
    public void setName(String name) {
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "마법사의 이름은 null일 수 없으며 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

<<<<<<< HEAD
    // HP getter
=======

    // HP
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
    public int getHp() {
        return this.hp;
    }

<<<<<<< HEAD
    // HP setter
    public void setHp(int hp) {

=======
    public void setHp(int hp) {
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

<<<<<<< HEAD
    // MP getter
=======

    // MP
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
    public int getMp() {
        return this.mp;
    }

<<<<<<< HEAD
    // MP setter
    public void setMp(int mp) {

=======
    public void setMp(int mp) {
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
        if (mp < 0) {
            throw new IllegalArgumentException(
                    "MP는 0 이상이어야 합니다."
            );
        }

        this.mp = mp;
    }

<<<<<<< HEAD
    // 지팡이 getter
=======

    // Wand
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
    public Wand getWand() {
        return this.wand;
    }

<<<<<<< HEAD
    // 지팡이 setter
    public void setWand(Wand wand) {

=======
    public void setWand(Wand wand) {
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
        if (wand == null) {
            throw new IllegalArgumentException(
                    "마법사의 지팡이는 null일 수 없습니다."
            );
        }

        this.wand = wand;
    }
<<<<<<< HEAD
}
=======


    // 힐
    public void heal(Hero hero) {

        // MP가 10보다 적으면 힐을 사용할 수 없다.
        if (this.mp < 10) {
            System.out.println("마나가 부족합니다");
            return;
        }

        // 대상 HP를 20 회복
        hero.setHp(hero.getHp() + 20);

        // 자신의 MP 10 소모
        this.mp -= 10;

        // 힐 성공 메시지
        System.out.println(
                "힐을 시전했습니다. 대상 HP: " + hero.getHp()
        );
    }
}
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
