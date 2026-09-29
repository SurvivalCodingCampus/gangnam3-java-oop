package com.survivalcoding.day01_class_instance;

public class GreatWizard extends Wizard {
    private int mp=150;
    private Wand wand;

    @Override
    void heal(Hero hero) {

        if (getMp() < 5) {
            throw new IllegalStateException("마나가 부족합니다");
        }

        int basePoint = 25;     // 기본회복 포인트
        int recoverPoint = (int) (basePoint * this.wand.power);       // 지팡이에 의한 증폭

        hero.setHp(hero.getHp() + recoverPoint);                      // 용사의 HP를 회복
        setMp(getMp() - 5);                                        // 마나 소모
        System.out.println("힐을 시전했습니다. 대상 HP: " + hero.getHp());     // 현재 값을 읽는 것이므로 getter 사용

    }

    void superHeal(Hero hero) {

        if (getMp() < 50) {
            throw new IllegalStateException("마나가 부족합니다");
        }

        hero.setHp(hero.getMaxHp());
        setMp(getMp() - 50);
        System.out.println("슈퍼힐을 시전했습니다. 대상 HP: " + hero.getHp());
    }
}
