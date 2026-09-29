package com.survivalcoding.day01_class_instance;

public class PoisonSlime extends Slime {

    private int poisonCount = 5;        // 독 공격 가능 횟수 저장, 수정금지이므로 private

    public PoisonSlime(String suffix, int hp) {
        super(suffix, hp);
    }

    public PoisonSlime(String suffix) {
        super(suffix);
    }


    // 메서드 오버라이팅
    @Override
    public void attack(Hero hero) {
        super.attack(hero);     // "보통 슬라임과 같은 공격"
        if (this.poisonCount != 0) {    // poisonCount가 0이 아니면
            System.out.println("추가로, 독 포자를 살포했다!");

            int poisonDamage = hero.getHp() / 5;
            hero.setHp(hero.getHp() - poisonDamage);

            System.out.println(poisonDamage + "포인트 데미지");

            this.poisonCount = this.poisonCount - 1;
        }
    }
}