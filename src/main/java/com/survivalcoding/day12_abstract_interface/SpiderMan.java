package com.survivalcoding.day12_abstract_interface;

/**
 * 스파이더맨 — 하나의 클래스가 복수의 역할을 갖는 예제.
 * <p>
 * 강의 자료의 "스파이더맨은 영웅이면서 시민이다" 에 해당한다.
 * <pre>
 * public class SpiderMan implements SuperFlyable, Attackable {
 * </pre>
 * <ul>
 *     <li>{@code SuperFlyable} → 영웅으로서의 비행 능력</li>
 *     <li>{@code Attackable} → 전투 능력</li>
 * </ul>
 *
 * <h2>왜 클래스를 상속하지 않았나</h2>
 * "영웅" 클래스와 "시민" 클래스를 상속받는다면 클래스가 두 개 필요하다.
 * 하지만 스파이더맨은 둘 다이면서 그 외에 다른 것도 될 수 있다.
 * {@code class SpiderMan extends SuperHero implements Flyable} 라고 하면
 * "다른 Superman 과 다른 종류의 Superman"이 되어 버린다.
 * <p>
 * {@code implements} 는 그런 계층을 만들지 않는다. 필요한 행동을 나열하기만 한다.
 * 그리고 인터페이스는 여러 개를 implements 할 수 있기 때문에,
 * "영웅 + 시민 + 날 수 있음"처럼 능력을 얼마든지 더할 수 있다.
 *
 * <h2>다중 구현이 가능한 이유</h2>
 * 클래스는 여러 상을 하나만 extends 할 수 있다 — 부모의 필드와 메서드가 겹칠 수 있기 때문이다.
 * 반면 인터페이스에는 <b>구현이 하나도 없다</b> 그래서 충돌할 일이 없다.
 * 그래서 인터페이스는 여러 개를 implements 해도 문제가 없다.
 *
 * @see SuperFlyable
 * @see Attackable
 */
public class SpiderMan implements SuperFlyable, Attackable {

    /** 스파이더맨의 이름. */
    private final String name;

    /** 현재 날고 있는지. */
    private boolean flying;

    /**
     * 이름만 받는 생성자.
     *
     * @param name 스파이더맨의 이름
     */
    public SpiderMan(String name) {
        this.name = name;
    }

    /**
     * 스파이더맨의 이름을 돌려준다.
     *
     * @return 스파이더맨의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 현재 날고 있는지 확인한다.
     *
     * @return 날고 있으면 {@code true}
     */
    public boolean isFlying() {
        return flying;
    }

    /**
     * 웹을 쏘아 날아 오른다.
     * <p>
     * {@code SuperFlyable} 이 {@code Flyable} 을 상속했으므로,
     * 이 메서드는 {@link Flyable#fly()} 도 구현한 셈이다.
     */
    @Override
    public void fly() {
        flying = true;
        System.out.println(name + "이(가) 웹을 쏘아 날아올랐습니다!");
    }

    /**
     * 착륙한다. {@link Flyable} 에는 없고 {@link SuperFlyable} 에만 있는 메서드.
     */
    @Override
    public void land() {
        flying = false;
        System.out.println(name + "이(가) 착륙했습니다.");
    }

    /**
     * 공격한다. {@code Attackable} 을 구현하기 위해 반드시 있어야 하는 메서드.
     * <p>
     * 이것이 없으면 컴파일 에러가 난다:
     * {@code SpiderMan is not abstract and does not override attack() in Attackable}
     */
    @Override
    public void attack() {
        System.out.println(name + "이(가) 거미줄을 휘둘러 적을 공격합니다!");
    }
}