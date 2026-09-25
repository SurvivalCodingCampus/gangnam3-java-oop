package com.survivalcoding.day01_class_instance;

public class PoisonSlime extends Slime {

    // constant
    private static final int POISON_COUNT = 5;

    // field
    private int poisonCount;

    // constructor
    public PoisonSlime(String suffix) {
        this(suffix, 10);
    }

    public PoisonSlime(String suffix, int hp) {
        super(suffix, hp);
        this.poisonCount = POISON_COUNT;
    }

    // method
    @Override
    public void attack(Hero hero) {
        super.attack(hero);

        if (hero.getHp() == 0) {
            return;
        }

        poisonAttack(hero);
    }

    @Override
    public void run() {
        System.out.println(this.getSuffix() + "는 도망쳤다!");
        System.out.println("최종 HP는 " + this.getHp() + "입니다");
    }

    private void poisonAttack(Hero hero) {
        if (this.poisonCount == 0) {
            System.out.println("독 포자가 더 이상 남아있지 않다");
            return;
        }
        System.out.println("추가로, 독 포자를 살포했다!");

        int ratio = 5;
        int damage = hero.getHp() / ratio;

        hero.takeDamage(damage);
        System.out.println(damage + "포인트 데미지");

        this.setPoisonCount(this.poisonCount - 1);
        System.out.println("남은 독 포자 수: " + this.poisonCount + "\n================================");
    }

    // getter
    public int getPoisonCount() {
        return poisonCount;
    }

    // setter
    public void setPoisonCount(int posionCount) {
        this.poisonCount = Math.max(0, posionCount);
    }
}
