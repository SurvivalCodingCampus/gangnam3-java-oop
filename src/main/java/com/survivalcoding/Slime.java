<<<<<<< HEAD
package com.survivalcoding;

public class  Slime {
    public int hp;
    public String suffix;
    final int level = 10;

    void run() {
        System.out.println("슬라임 " + suffix + "가 도망갔다");
    }
    void takeDamage(int x){};
}
=======
package com.survivalcoding;

public class Slime {

    private String name;
    private int hp;
    private Hero hero;

    // 생성자
    public Slime(String name) {
        this.name = name;
        this.hp = 100;
    }

    // Getter
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    // Setter
    public void setHp(int hp) {
        this.hp = hp;
    }

    // 보통 공격
    public void attack(Hero hero) {
        this.hero = hero;
        System.out.println(name + "이 공격했다!");
        hero.setHp(hero.getHp() - 10);
        System.out.println("10포인트 데미지");
    }
}
>>>>>>> be77039 (feat : 2026.09.23 박강원_ 8장)
