package com.survivalcoding.day04.exam;

public class PoisonSlime extends Slime {
    private static final int POISON_RATE = 5;
    private static final int MAX_POISON_COUNT = 5;

    private int poisonCount;

    public PoisonSlime(final String suffix) {
        super(suffix);
        poisonCount = MAX_POISON_COUNT;
    }

    @Override
    void attack(final Hero hero) {
        super.attack(hero);

        if (poisonCount <= 0) {
            return;
        }

        System.out.println("추가로, 독 포자를 살포했다!");

        int curHeroHp = hero.getHp();
        int poisonDamage = curHeroHp / POISON_RATE;
        hero.setHp(curHeroHp - poisonDamage);

        System.out.println(poisonDamage + "포인트 데미지");

        poisonCount--;
    }
}
