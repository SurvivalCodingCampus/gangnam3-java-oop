package com.survivalcoding.day01_class_instance;

class Main {
    public static void main(String[] args) {
        // 메인 메서드 = 실행 버튼

        Hero hero = new Hero();

        Slime slime = new Slime();
        slime.setHp();

        PoisonSlime poisonSlime = new PoisonSlime("A");

        SuperHero superHero = new SuperHero();
        superHero.attack();

        Wizard wizard = new Wizard();

        GreatWizard greatWizard = new GreatWizard();

        Wand wand = new Wand();

    }
}