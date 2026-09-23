package com.survivalcoding.day04.exam;

public class PoisonSlime extends Slime {
    private static final int POISON_RATE = 5;
    private static final int MAX_POISON_COUNT = 5;

    private int poisonCount;

    public PoisonSlime(final String suffix) {
        super(suffix);
        setPoisonCount(MAX_POISON_COUNT);
    }

    @Override
    public void attack(final Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("hero 널 들어옴");
        }

        super.attack(hero);

        if (poisonCount <= 0) {
            return;
        }

        poisonCount--;

        System.out.println("추가로, 독 포자를 살포했다!");

        int curHeroHp = hero.getHp();
        int poisonDamage = curHeroHp / POISON_RATE;

        hero.takeDamage(poisonDamage);

        System.out.println(poisonDamage + "포인트 데미지");
    }

    public void setPoisonCount(int poisonCount) {

        // 0 들어오면 / by zero 예외
        // 1이면 바로 죽으니깐
        if (poisonCount < 2) {
            throw new IllegalArgumentException("poisonCount는 2이상을 입력");
        }

        this.poisonCount = poisonCount;
    }
}
