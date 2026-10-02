package com.survivalcoding.day01_class_instance;

public class PoisonSlime extends Slime {
    public static final int POISON_SLIME_DEFAULT_HP = 10;
    public static final int POISON_SLIME_DEFAULT_POISON_COUNT = 5;

    private int poisonCount = POISON_SLIME_DEFAULT_POISON_COUNT;

    public PoisonSlime(String suffix) {
        this(suffix, POISON_SLIME_DEFAULT_HP);
    }

    public PoisonSlime(String suffix, int hp) {
        super(suffix, hp);
    }

    public int getPoisonCount() {
        return poisonCount;
    }

    @Override
    public void attack(Hero hero) {
        super.attack(hero); // 10 만큼

        // poisonCount != 0
        if (poisonCount > 0) {
            // 화면에 “추가로, 독 포자를 살포했다!” 를 표시
            System.out.println("추가로, 독 포자를 살포했다!");


            /// AI야 도와줘
            // 독 데미지는 용사의 HP / 5 이며 소수점 이하는 버린다.
            final int poisonDamage = hero.getHp() / 5;

            // 독 데미지만큼 용사의 HP를 감소시키고
            hero.setHp(hero.getHp() - poisonDamage);

            // “~포인트 데미지"라고 표시한다
            System.out.println(poisonDamage + "포인트 데미지");
            ///

            // poisonCount 를 1 감소 시킨다
            poisonCount--;
        }
    }

    //////////////////////////////////////////
    public static void main(String[] args) {
        Hero hero = new Hero("홍길동");

        PoisonSlime poisonSlime = new PoisonSlime("A");
        poisonSlime.attack(hero);
    }
    //////////////////////////////////////////
}
