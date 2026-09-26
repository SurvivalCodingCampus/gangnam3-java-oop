package com.survivalcoding.day04.exam;

public class PoisonSlime extends Slime {

    private static final int POISON_RATE = 5;
    private static final int MAX_POISON_COUNT = 5;
    private int poisonCount;

    // Builder를 통해서만 객체 생성 가능
    private PoisonSlime(Builder builder) {
        super(builder); // 부모 Slime의 빌더 전용 생성자 호출
        setPoisonCount(builder.poisonCount);
    }

    // region Func

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

    // endregion Func

    // region Getter Setter

    public int getPoisonCount() {
        return poisonCount;
    }

    private void setPoisonCount(int poisonCount) {
        if (poisonCount < 0) {
            throw new IllegalArgumentException("0보다 작습니다");
        }

        this.poisonCount = poisonCount;
    }

    // endregion

    public static class Builder extends Slime.Builder {
        private int poisonCount = MAX_POISON_COUNT;

        @Override
        public Builder suffix(String suffix) {
            super.suffix(suffix);
            return this;
        }

        @Override
        public Builder hp(int hp) {
            super.hp(hp);
            return this;
        }

        @Override
        public Builder power(int power) {
            super.power(power);
            return this;
        }

        public Builder poisonCount(int poisonCount) {
            this.poisonCount = poisonCount;
            return this;
        }

        @Override
        public PoisonSlime build() {
            return new PoisonSlime(this);
        }
    }
}