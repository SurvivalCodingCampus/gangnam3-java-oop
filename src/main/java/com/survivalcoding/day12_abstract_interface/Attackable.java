package com.survivalcoding.day12_abstract_interface;

/**
 * 공격할 수 있는 존재 —— 인터페이스(interface)의 기본 예제.
 * <p>
 * <b>Can-Do 원칙</b>: 이름이 "~할 수 있는"(형용사형 + able) 형태여야 한다.
 * 이게 상속(is-a)과 인터페이스(Can-Do)를 구분하는 기준이다.
 * <ul>
 *     <li>{@code Monster} 는 {@code Character} <b>이다</b>(is-a) → 상속이 맞다.</li>
 *     <li>{@code SpiderMan} 은 {@code Attackable} <b>할 수 있다</b>(Can-Do) → 인터페이스가 맞다.</li>
 * </ul>
 * "무엇인지"를 정하는 것이 상속이고, "무엇을 할 수 있는지"를 정하는 것이 인터페이스다.
 *
 * <h2>인터페이스의 규칙</h2>
 * <ol>
 *     <li><b>모든 메서드는 추상 메서드다.</b> (구현이 없는 선언만 존재한다)</li>
 *     <li><b>필드를 가질 수 없다.</b> 상태(데이터)가 아니라 능력(행동)만 표현한다.</li>
 *     <li>{@code public abstract} 는 <b>자동으로 붙는다</b> — 굳이 쓰지 않아도 된다.</li>
 *     <li>구현하려면 {@code implements} 를 쓴다.</li>
 *     <li>구현하는 클래스에서는 반드시 그 메서드를 <b>전부</b> 구현해야 한다.</li>
 * </ol>
 *
 * <h2>이 인터페이스를 구현하면 얻는 것</h2>
 * {@code attack()} 을 반드시 만들어야 하므로, "공격할 수 있다고 선언했는데 공격이 없다"
 * 는 상태가 원천적으로 불가능해진다. 그리고 이 인터페이스 타입의 변수에 담으면
 * {@code attack()} 이 있다고 <b>보장</b>되므로, 호출하는 쪽에서 확인이 필요 없다.
 *
 * @see Flyable
 * @see MagicCaster
 */
public interface Attackable {

    /**
     * 인터페이스 안에서 선언한 필드는 자동으로 {@code public static final} 이 된다.
     * <p>
     * <b>이것이 일반 상수 선언이 아닌 이유</b> — {@code public static final} 을 굳이 적지 않아도
     * 컴파일러가 붙여준다. 즉 "상수" 라는 뜻이 아니라, <b>구현 클래스가 여러 개일 때
     * 모두가 공유하는 규격값</b>이라는 뜻이다. 그래서 필드를 선언할 수 없다.
     * (인스턴스마다 다른 값을 저장해야 하는 상태 데이터는 인터페이스 자리가 아니다)
     *
     * @return 한 번에 공격할 수 있는 최대 횟수
     */
    int MAX_ATTACK_PER_TURN = 3;

    /**
     * 공격한다.
     * <p>
     * {@code public abstract} 를 생략했다. 적어도도 컴파일 결과는 완전히 동일하다.
     */
    void attack();
}