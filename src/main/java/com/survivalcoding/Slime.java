package com.survivalcoding;

/**
 * 슬라임(Slime)을 나타내는 클래스.
 * <p>
 * {@link PoisonSlime} 의 부모 클래스가 된다.
 * 즉, 슬라임이 먼저 있고 그 특수한 형태인 "독 슬라임"이 나중에 생긴 구조다.
 * (11장 - 상속의 기본 개념)
 */
public class Slime {

    /** 슬라임의 최대 HP. */
    public static final int MAX_HP = 100;

    /** 슬라임의 현재 HP. 별도로 지정하지 않으면 최대치로 시작한다. */
    public int hp = MAX_HP;

    /** 슬라임의 이름 뒤에 붙을 별명(예: A, B). 여러 몬스터를 구분하는 용도. */
    public String suffix;

    /** 슬라임의 레벨. */
    public final int level = 10;

    /** 슬라임의 이름. */
    private String name;

    /**
     * 기본 생성자.
     * <p>
     * 이름 없이 슬라임을 만든다. 이 경우 attack() 출력에서 이름 부분이 null 로 보인다.
     */
    public Slime() {
    }

    /**
     * 이름만 지정하는 생성자.
     *
     * @param name 슬라임의 이름
     */
    public Slime(String name) {
        this.name = name;
    }

    /**
     * 도망친다.
     * <p>
     * 별명(suffix)을 함께 출력해 어떤 슬라임인지 구분한다.
     */
    void run() {
        System.out.println("슬라임 " + suffix + "가 도망갔다");
    }

    /**
     * 데미지를 받고 HP가 그만큼 감소한다.
     * <p>
     * HP가 음수가 될 수는 없으므로 0 아래로 내려가지 않도록 보정한다.
     *
     * @param damage 받은 데미지
     */
    void takeDamage(int damage) {
        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    // ==================== Getter ====================

    /**
     * @return 슬라임의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * @return 슬라임의 현재 HP
     */
    public int getHp() {
        return hp;
    }

    // ==================== Setter ====================

    /**
     * 슬라임의 HP를 설정한다.
     *
     * @param hp 설정할 HP
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    // ==================== 공격 ====================

    /**
     * 대상 용사를 일반 공격한다. 10포인트의 데미지를 준다.
     * <p>
     * {@link PoisonSlime} 가 이 메서드를 {@code super.attack(hero)} 로 호출해
     * "보통 슬라임과 같은 공격"을 먼저 수행한 뒤,
     * 자신만의 독 공격을 덧붙인다.
     * <p>
     * HP 감소는 {@link Hero#setHp(int)} 에 위임한다.
     * 0 이라는 하한 처리가 거기에 이미 있으므로 여기서는 중복 검사하지 않는다.
     *
     * @param hero 공격 대상 용사
     */
    public void attack(Hero hero) {
        System.out.println(name + "이 공격했다!");
        hero.setHp(hero.getHp() - 10);
        System.out.println("10포인트 데미지");
    }
}
