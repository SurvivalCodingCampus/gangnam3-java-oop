package com.survivalcoding;

public class PoisonSlime extends Slime {

    // 독 공격 가능 횟수
    private int poisonCount = 5;

    // 생성자
    public PoisonSlime(String name) {
        super(name);
    }

    // 독 슬라임 공격
    @Override
    public void attack(Hero hero) {

        // 1. 보통 슬라임과 같은 공격
        super.attack(hero);

        // 2. 독 공격 횟수가 남아 있는 경우
        if (poisonCount > 0) {

            // 3. 독 포자 살포
            System.out.println("추가로, 독 포자를 살포했다!");

            // 4. 독 데미지 = 용사 HP / 5
            int poisonDamage = hero.getHp() / 5;

            // 용사의 HP 감소
            hero.setHp(hero.getHp() - poisonDamage);

            // 데미지 표시
            System.out.println(poisonDamage + "포인트 데미지");

            // 5. 독 공격 횟수 감소
            poisonCount--;
        }
    }
}