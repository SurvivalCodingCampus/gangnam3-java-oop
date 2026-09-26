package com.survivalcoding.day04.exam;

public class SuperHero extends Hero {
    static final int BONUS_DAMAGE = 5;

    private boolean isFlying;

    private SuperHero(Builder builder) {
        super(builder);
        isFlying = builder.isFlying;
    }

    // region Func

    @Override
    public void run() {
        System.out.println("멋지게 퇴각했다");
    }

    @Override
    public void attack(final Slime slime) {
        super.attack(slime);

        if (slime.isDead()) {
            return;
        }

        if (isFlying) {
            slime.takeDamage(BONUS_DAMAGE);
            System.out.println(BONUS_DAMAGE + "포인트의 추가 피해를 입혔다");
        }
    }

    public void land() {
        isFlying = false;
    }

    public void fly() {
        isFlying = true;
    }

    // endregion

    // region Getter

    public boolean isFlying() {
        return isFlying;
    }

    // endregion

    public static class Builder extends Hero.Builder {
        private boolean isFlying = false;

        @Override
        public Builder name(String name) {
            super.name(name);
            return this;
        }

        @Override
        public Builder hp(int hp) {
            super.hp(hp);
            return this;
        }

        @Override
        public Builder sword(Sword sword) {
            super.sword(sword);
            return this;
        }

        @Override
        public Builder power(int power) {
            super.power(power);
            return this;
        }

        public Builder isFlying(boolean isFlying) {
            this.isFlying = isFlying;
            return this;
        }

        @Override
        public SuperHero build() {
            return new SuperHero(this);
        }
    }
}