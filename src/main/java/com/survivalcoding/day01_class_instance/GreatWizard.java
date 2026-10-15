package com.survivalcoding.day01_class_instance;

public class GreatWizard extends Wizard {
    public final static int DEFAULT_MP = 150;

    public GreatWizard(String name, int hp) {
        super(name, hp, DEFAULT_MP);
    }

    @Override
    public void heal(Hero hero) {
        super.heal(hero);

        System.out.println(getMp());

        /// AI야 도와줘
    }

    public void superHeal(Hero hero) {
        hero.setHp(Hero.MAX_HP);

        mp -= 50;
    }
}
