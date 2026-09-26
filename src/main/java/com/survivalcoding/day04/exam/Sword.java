package com.survivalcoding.day04.exam;

public class Sword {
    private static final int DEFAULT_DAMAGE = 5;
    private static final String DEFAULT_NAME = "김영웅검";

    private String name;
    private int damage;

    // Builder를 통해서만 객체 생성 가능
    private Sword(Builder builder) {
        setName(builder.name);
        setDamage(builder.damage);
    }

    // region Getter Setter

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    private void setName(String name) {
        if (!Utils.isValidName(name)) {
            throw new IllegalArgumentException("이름은 널 공란 금지");
        }
        this.name = name;
    }

    private void setDamage(int damage) {
        if (damage <= 0) {
            throw new IllegalArgumentException("데미지는 1이상");
        }
        this.damage = damage;
    }

    // endregion

    public static class Builder {
        private String name = DEFAULT_NAME;
        private int damage = DEFAULT_DAMAGE;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public Sword build() {
            return new Sword(this);
        }
    }
}