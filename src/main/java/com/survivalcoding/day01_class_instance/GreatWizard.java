package com.survivalcoding.day01_class_instance;

public class GreatWizard extends Wizard {

    // constant
    private static final int DEFAULT_MP = 150;

    // constructor
    public GreatWizard() {
        super("대마법사", 100, DEFAULT_MP, new Wand("대나무지팡이", 20.0));
    }

    // method
    @Override
    public void heal(Hero hero) {
        int mpCost = 5;

        if (this.getMp() < 5) {
            System.out.println("마나가 부족합니다");
            return;
        }

        int recovPoint = 25;

        hero.setHp(hero.getHp() + recovPoint);
        this.setMp(this.getMp() - mpCost);

        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }

    public void superHeal(Hero hero) {
        int mpCost = 50;

        if (this.getMp() < mpCost) {
            System.out.println("마나가 부족합니다");
            return;
        }

        hero.setHp(Hero.DEFAULT_HP);
        this.setMp(this.getMp() - mpCost);

        System.out.println("슈퍼 힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }
}
