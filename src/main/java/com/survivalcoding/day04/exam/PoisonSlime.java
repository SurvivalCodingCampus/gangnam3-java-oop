package com.survivalcoding.day04.exam;

public class PoisonSlime extends Slime {

    // 0 들어오면 / by zero 예외
    // 1이면 바로 죽으니깐 2이상 입력
    static final int POISON_RATE = 5;
    static final int MAX_POISON_COUNT = 5;

    private int poisonCount;

    public PoisonSlime(final String suffix) {
        this(suffix, MAX_POISON_COUNT);
    }

    public PoisonSlime(final String suffix, final int poisonCount) {
        super(suffix);
        setPoisonCount(poisonCount);
    }

    @Override
    public void attack(final Hero hero) {
        super.attack(hero);

        if (hero.isDead()) {
            return;
        }

        if (poisonCount <= 0) {
            return;
        }

        poisonCount--;

        System.out.println("추가로, 독 포자를 살포했다!");

        int curHeroHp = hero.getHp();
        int poisonDamage = Math.max(1, curHeroHp / POISON_RATE);
        hero.takeDamage(poisonDamage);
        System.out.println(poisonDamage + "포인트 데미지");
    }

    public void setPoisonCount(int poisonCount) {

        if (poisonCount < 0) {
            throw new IllegalArgumentException("0보다 작습니다");
        }

        this.poisonCount = poisonCount;
    }

    public int getPoisonCount() {
        return poisonCount;
    }
}
