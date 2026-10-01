package com.survivalcoding.day12_abstract_interface;

/**
 * 마법을 쓸 수 있는 존재.
 * <p>
 * 이 인터페이스는 "일반전투와 마법을 <b>둘 다</b> 할 수 있는 캐릭터"를 만들고 싶을 때 쓴다.
 * {@link Attackable} 만 구현하면 근접 전투만 가능한 캐릭터가 되고,
 * 이것까지 implements 하면 전투와 마법을 함께 쓰는 캐릭터가 된다.
 * <p>
 * <b>상속이 아닌 인터페이스 조합을 써야 하는 이유</b> — "전투형 마법사"를 클래스로 만들면
 * {@code class BattleMage extends Wizard} 라는 계층이 계속 늘어납니다.
 * 하지만 실제로는 "마법을 쓸 수 있는"지만 추가된 것이고, {@code Wizard} 의 모든 행동을
 * 물려받을 필요는 없습니다. 행동을 조합해서 정의하는 것이 인터페이스의 역할입니다.
 *
 * @see Attackable
 */
public interface MagicCaster {

    /**
     * 마법을 사용한다.
     * <p>
     * 반환 타입을 두지 않은("void") 이유는, 마법 사용 결과가 무엇이든 호출한 쪽은
     * "사용했다/못 했다"만 알면 되기 때문이다. 필요하다면 나중에 반환 타입을 붙이면 된다.
     */
    void castMagic();

    /**
     * 마력(MP) 관련 기본 규격을 선언해 두는 자리.
     * <p>
     * {@code public static final} 이 자동으로 붙으므로 모든 구현 클래스가 이 값을 공유한다.
     * 구현마다 다른 값을 원한다면 인터페이스가 아니라 각 클래스의 필드로 둬야 한다.
     */
    int BASE_MP = 50;
}