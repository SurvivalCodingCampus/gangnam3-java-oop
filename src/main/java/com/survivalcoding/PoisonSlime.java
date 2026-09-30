package com.survivalcoding;

public class PoisonSlime extends Slime {
    private final int INIT_HP = 20;//선언에서는 순서가 상관된다.
    //hp=INIT_HP; 이것은 실행문 class에서는 선행문만 존재한다.
    private final int INIT_POSIONCOUNT = 5;
    private int poisonCount = INIT_POSIONCOUNT;
    private int hp = INIT_HP;
    private String name = "poison";

    PoisonSlime() {
    }

    PoisonSlime(String name, int hp) {
        super(name, hp);
        this.hp = INIT_HP;
        poisonCount = INIT_POSIONCOUNT;
    }

    public void attack(Hero hero) {
        super.attack(hero);
        if (poisonCount > 0) {
            System.out.println("추가로 독 포자를 살포했다");
            hero.setHp(hero.getHp() - (int) (hero.getHp() / 5));
            poisonCount--;
        }

    }
}
