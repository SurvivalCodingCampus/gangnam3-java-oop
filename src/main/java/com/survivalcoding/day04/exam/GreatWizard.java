package com.survivalcoding.day04.exam;

public class GreatWizard extends Wizard {
    static final String SUPER_HEAL_SKILL_NAME = "슈퍼 힐";
    static final int COST_FOR_HEAL = 5;
    static final int COST_FOR_SUPER_HEAL = 50;
    static final int HEAL_HP_AMOUNT = 25;

    private static final int MAX_MP = 150;

    private GreatWizard(Builder builder) {
        super(builder);
    }

    // region Func

    @Override
    public void heal(final Hero hero) {
        super.heal(hero, COST_FOR_HEAL, HEAL_HP_AMOUNT, HEAL_SKILL_NAME);
    }

    public void superHeal(final Hero hero) {
        super.heal(hero, COST_FOR_SUPER_HEAL, Hero.MAX_HP, SUPER_HEAL_SKILL_NAME);
    }

    // endregion Func

    public static class Builder extends Wizard.Builder {
        public Builder() {
            mp = MAX_MP;
        }

        @Override
        public Builder hp(int hp) {
            super.hp(hp);
            return this;
        }

        @Override
        public Builder mp(int mp) {
            super.mp(mp);
            return this;
        }

        @Override
        public Builder name(String name) {
            super.name(name);
            return this;
        }

        @Override
        public Builder wand(Wand wand) {
            super.wand(wand);
            return this;
        }

        @Override
        public GreatWizard build() {
            return new GreatWizard(this);
        }
    }
}