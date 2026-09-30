package com.survivalcoding;

/**
 * 연습문제 5 - 대마법사(GreatWizard)
 * <p>
 * {@link Wizard} 를 상속받아 마나를 150 을 기본으로 가지며,
 * 더 강하고 효율적인 힐을 사용할 수 있다.
 * <p>
 * 이 클래스가 연습문제 5의 답이다. 다음 세 가지를 연습한다.
 * <ul>
 *     <li>{@code extends} 로 부모 클래스의 기능(name, hp, mp, wand, heal)을 물려받기</li>
 *     <li>{@code super(...)} 로 부모 클래스의 생성자를 호출해 초기 상태 구성하기</li>
 *     <li>{@code @Override} 로 부모의 {@link #heal(Hero)} 를 다시 정의(오버라이드)하기</li>
 * </ul>
 * <p>
 * 올바른 상속인지는 "is-a 원칙"으로 판단한다.
 * GreatWizard is a Wizard → 대마법사는 마법사의 한 종류이므로 상속해도 된다.
 * <p>
 * 부모의 private 필드(mp)에 직접 접근할 수는 없으므로,
 * 상속받은 {@code getMp()} / {@code setMp()} 를 통해 접근한다.
 * 캡슐화를 깨지 않고 상속하는 방법이다.
 */
public class GreatWizard extends Wizard {

    /** 대마법사의 기본 HP. */
    private static final int DEFAULT_HP = 100;

    /** 연습문제 5 : mp 의 초기값은 150 이다. */
    private static final int DEFAULT_MP = 150;

    /** heal() 재정의 시 대상 HP를 회복시키는 양. */
    private static final int HEAL_AMOUNT = 25;

    /** heal() 재정의 시 소모하는 MP. */
    private static final int HEAL_COST = 5;

    /** superHeal() 시 소모하는 MP. */
    private static final int SUPER_HEAL_COST = 50;

    /** 슈퍼 힐 성공 시 출력할 메시지의 앞부분. */
    private static final String SUPER_HEAL_MESSAGE =
            "슈퍼 힐을 시전했습니다. 대상 HP: ";

    /**
     * 이름만 지정하는 생성자.
     * <p>
     * HP 는 {@link #DEFAULT_HP}, MP 는 {@link #DEFAULT_MP}(150) 로 초기화된다.
     *
     * @param name 대마법사의 이름
     */
    public GreatWizard(String name) {
        this(name, DEFAULT_HP, DEFAULT_MP);
    }

    /**
     * 이름, HP, MP를 모두 지정하는 생성자.
     * <p>
     * {@code super(name, hp, mp)} 로 부모 클래스의 생성자를 호출한다.
     * 상속받은 필드가 아니라 <b>부모가 소유한 필드</b>이므로
     * super 로 넘겨 부모가 직접 채우게 한다.
     *
     * @param name 대마법사의 이름
     * @param hp   대마법사의 HP
     * @param mp   대마법사의 MP
     */
    public GreatWizard(String name, int hp, int mp) {
        super(name, hp, mp);
    }

    /**
     * {@link Wizard#heal(Hero)} 를 재정의(override)한다.
     * <p>
     * 연습문제 5의 요구사항: 대상의 HP를 25 회복시키고 자신의 MP를 5 소모한다.
     * 부모의 heal 과 달리 더 회복량에 더 적은 마나를 쓴다.
     * <p>
     * 호출하는 쪽은 {@code Wizard} 타입이어도, 실제 실행되는 것은 이 메서드다.
     * 이것이 다형성(polymorphism)이다.
     *
     * @param hero 힐을 받을 대상 용사
     */
    @Override
    public void heal(Hero hero) {

        // 1. 마나가 5보다 적으면 회복하지 않고 종료한다.
        if (getMp() < HEAL_COST) {
            System.out.println(NOT_ENOUGH_MP_MESSAGE);
            return;
        }

        // 2. 대상 HP를 25 회복시키고, 자신의 MP를 5 소모한다.
        hero.setHp(hero.getHp() + HEAL_AMOUNT);
        setMp(getMp() - HEAL_COST);

        // 3. 성공 메시지
        System.out.println(SUPER_HEAL_MESSAGE + hero.getHp());
    }

    /**
     * 대상 용사의 HP를 최대로 회복시키고 자신의 MP를 50 소모한다.
     * <p>
     * {@link Wizard} 에는 없는, GreatWizard 만의 새로운 기능이므로
     * {@code @Override} 를 붙이지 않는다. (새 메서드이므로 재정의가 아니다)
     *
     * @param hero 완전 회복시킬 대상 용사
     */
    public void superHeal(Hero hero) {

        // 1. 마나가 50보다 적으면 회복하지 않고 종료한다.
        if (getMp() < SUPER_HEAL_COST) {
            System.out.println(NOT_ENOUGH_MP_MESSAGE);
            return;
        }

        // 2. 대상 HP를 용사의 최대치로 완전 회복시킨다.
        hero.setHp(Hero.MAX_HP);
        setMp(getMp() - SUPER_HEAL_COST);

        // 3. 성공 메시지
        System.out.println(SUPER_HEAL_MESSAGE + hero.getHp());
    }
}
