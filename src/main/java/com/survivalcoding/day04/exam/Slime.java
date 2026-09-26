package com.survivalcoding.day04.exam;

public class Slime {
    static final int MAX_HP = 20;
    private static final int DEFAULT_POWER = 10;
    private static final String DEFAULT_SUFFIX = "A";

    private String suffix;
    private int hp;
    private int power;
    private boolean isDead;

    protected Slime(Builder builder) {
        if (!Utils.isValidName(builder.suffix)) {
            throw new IllegalArgumentException("이름에 널, 공란 불가");
        }

        if (builder.hp < 0) {
            throw new IllegalArgumentException("0보다 커야함");
        }

        if (builder.power <= 0) {
            throw new IllegalArgumentException("1보다 커야함");
        }

        suffix = builder.suffix;
        hp = builder.hp;
        power = builder.power;
        isDead = false;
    }

    // region Func

    public void attack(final Hero hero) {
        attack(hero, power);
    }

    private void attack(final Hero hero, final int damage) {
        if (hero == null) {
            throw new IllegalArgumentException("히어로 널");
        }

        if (damage < 1) {
            throw new IllegalArgumentException("데미지는 1이상이어야 함");
        }

        if (hero.isDead()) {
            return;
        }

        System.out.println("슬라임 " + suffix + "이/가 공격했다");
        System.out.println(damage + "의 데미지");
        hero.takeDamage(damage);
    }

    public void takeDamage(final int damage) {
        if (isDead) {
            System.out.println("이미 죽음");
            return;
        }

        if (damage < 1) {
            throw new IllegalArgumentException("1보다 작은값은 불가");
        }

        hp -= damage;

        if (hp <= 0) {
            hp = 0;
            die();
        }
    }

    private void die() {
        isDead = true;
        System.out.println("슬라임" + suffix + "는 죽었다");
    }

    // endregion Func

    // region Getter

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isDead() {
        return isDead;
    }

    // endregion

    public static class Builder {
        protected String suffix = DEFAULT_SUFFIX;
        protected int hp = MAX_HP;
        protected int power = DEFAULT_POWER;

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder power(int power) {
            this.power = power;
            return this;
        }

        public Slime build() {
            return new Slime(this);
        }
    }
}