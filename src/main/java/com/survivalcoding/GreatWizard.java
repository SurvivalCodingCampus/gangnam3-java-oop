package com.survivalcoding;

public class GreatWizard extends Wizard {
    private int mp = 150;

    @Override
    public void heal(Hero hero) {
        super.heal(hero);
        if (this.getMp() < 5) {
            System.out.println("마나가 부족합니다");
            return;
        }

        hero.setHp(hero.getHp() + 25);
        this.setMp(this.getMp() - 5);

        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }

    public void superheal(Hero hero) {
        if (this.getMp() < 50) {
            System.out.println("마나가 부족합니다");
            return;
        }

        hero.setHp(hero.getHp() + hero.getMAXHP());
        this.setMp(this.getMp() - 50);

        System.out.println("슈퍼힐을 시전했습니다. 대상 HP: " + hero.getMAXHP());
    }
}
