package com.survivalcoding.day04.exam;

public class GreatWizard extends Wizard {
    static final String SUPER_HEAL_SKILL_NAME = "슈퍼 힐";
    private static final int MAX_MP = 150;
    static final int COST_FOR_HEAL = 5;
    static final int COST_FOR_SUPER_HEAL = 50;
    static final int HEAL_HP_AMOUNT = 25;

    public GreatWizard() {
        setMp(MAX_MP);
    }

}
