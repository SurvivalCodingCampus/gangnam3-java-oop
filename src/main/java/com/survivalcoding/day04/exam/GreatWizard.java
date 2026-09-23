package com.survivalcoding.day04.exam;

public class GreatWizard extends Wizard {
    private static final String SUPER_HEAL_SKILL_NAME = "슈퍼 힐";
    private static final int MAX_MP = 150;
    private static final int COST_FOR_HEAL = 5;
    private static final int COST_FOR_SUPER_HEAL = 50;
    private static final int HEAL_HP_AMOUNT = 25;

    public GreatWizard() {
        setMp(MAX_MP);
    }

    public void heal(final Hero hero) {
        super.heal(hero, HEAL_HP_AMOUNT, COST_FOR_HEAL, Wizard.HEAL_SKILL_NAME);
    }

    public void superHeal(final Hero hero) {
        super.heal(hero, Hero.MAX_HP, COST_FOR_SUPER_HEAL, SUPER_HEAL_SKILL_NAME);
    }
}
