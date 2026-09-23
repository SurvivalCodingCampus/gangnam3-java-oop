package com.survivalcoding.day04.exam;

public class PoisonSlime extends Slime {

    private int poisonCount;

    public PoisonSlime(final String suffix) {
        super(suffix);
        poisonCount = 5;
    }

    @Override
    void attack(final Hero hero) {
        super.attack(hero);

        if (poisonCount <= 0) {
            return;
        }

        System.out.println("추가로, 독 포자를 살포했다!");

        int curHeroHp = hero.getHp();
        int poisonDamage = curHeroHp / 5;
        hero.setHp(curHeroHp - poisonDamage);

        System.out.println(poisonDamage + "포인트 데미지");

        poisonCount--;
    }
}
