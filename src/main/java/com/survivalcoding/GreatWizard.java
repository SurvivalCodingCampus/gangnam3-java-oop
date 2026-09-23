package com.survivalcoding;

public class GreatWizard extends Wizard {
    private int INIT_MP = 150;

    GreatWizard() {
        setMp(INIT_MP);
    }

    @Override//오버라이드할떄 애가 덮어써서 디버깅 안함
    //객체는 이름표 기본타입은 그냥 깡으로 붙인다.
    public void heal(Hero hero) {
        if (getMp() >= 5) {
            hero.setHp(hero.getHp() + 25);
            System.out.println("힐을 시전했습니다" + " 대상 HP:" + hero.getHp());
        } else {
            System.out.println("mp가 부족합니다");
        }
    }


    public void superHeal(Hero hero) {
        if (getMp() >= 50) {
            hero.setHp(hero.getmaxHp());
            setMp(getMp() - 50);
            //{sout + enter} system out println shortcut
            System.out.println("대상 HP:" + hero.getHp());
        } else {
            System.out.println("mp가 부족합니다");
        }


    }
}
