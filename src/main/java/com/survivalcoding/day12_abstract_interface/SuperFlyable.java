package com.survivalcoding.day12_abstract_interface;

/**
 * {@link Flyable} 을 상속한 인터페이스 —— 인터페이스끼리도 상속이 된다.
 * <p>
 * 슬라이드의 "인터페이스간의 상속 가능" 에 해당한다.
 * <pre>
 * public interface SuperFlyable extends Flyable {
 *     void fly();       // 부모의 메서드
 *     void land();      // 자식이 추가한 메서드
 * }
 * </pre>
 *
 * <h2>키워드가 {@code extends} 인 이유</h2>
 * 인터페이스는 구현이 아니라 "약속을 물려받는" 관계다.
 * 그래서 {@code implements} 가 아니라 {@code extends} 를 쓴다.
 * <p>
 * {@code SuperFlyable} 을 구현하는 클래스는 {@code fly()} 뿐 아니라
 * {@code land()} 까지 <b>둘 다</b> 구현해야 한다. 물려받은 메서드도 구현 책임이 따라온다.
 *
 * <h2>extends 와 implements 정리</h2>
 * <pre>
 * 부모            자식           키워드        다중 상속
 * -------------------------------------------------------
 * class          class          extends       X (안 된다)
 * interface      class          implements    O (구현)
 * interface      interface      extends       O (상속)
 * -------------------------------------------------------
 * </pre>
 * 클래스는 여러 개의 클래스를 상속할 수 없다(이유는 11장에서 다뤘다).
 * 하지만 인터페이스는 여러 개를 implements 할 수 있다.
 * 그래서 "클래스는 하나, 인터페이스는 여러 개"가 되는 구조다.
 *
 * @see Flyable
 * @see Attackable
 */
public interface SuperFlyable extends Flyable {

    /**
     * {@link Flyable#fly()} 를 물려받아 여기서 다시 선언할 수도 있다.
     * <p>
     * 다시 선언해도 완전히 같은 메서드 선언이므로 문법적으로 문제가 없다.
     * {@code @Override} 처럼 남는 표시가 없다 — {@code public abstract} 는 자동이기 때문.
     */
    @Override
    void fly();

    /**
     * 착륙한다. {@link Flyable} 에는 없던, 이 인터페이스만 가진 메서드.
     */
    void land();
}