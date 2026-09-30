package com.survivalcoding.day12_abstract_interface;

/**
 * 날 수 있는 존재.
 * <p>
 * {@link Attackable} 과 함께 쓰일 때 의미가 분명해진다.
 * 이 둘을 동시에 구현한 클래스는 "영웅이면서 시민" 같은 복수 역할을 갖게 된다.
 * <p>
 * 인터페이스는 <b>분류</b>의 기능을 한다. 나중에 새 캐릭터가 추가돼도
 * "날 수 있는 것"을 모으는 코드는 손댈 필요가 없다.
 *
 * @see Attackable
 */
public interface Flyable {

    /**
     * 날아 오른다.
     * <p>
     * 이 메서드가 있다는 것은 "날 수 있다"는 능력의 존재를 선언하는 것이지,
     * 실제로 어떻게 나는지를 정하는 것이 아니다. 그것은 구현 클래스가 정한다.
     */
    void fly();
}