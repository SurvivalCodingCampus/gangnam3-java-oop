package com.survivalcoding;

/**
 * 괴물버섯(Kinoko)을 나타내는 클래스.
 * <p>
 * {@link Hero#attack(Kinoko)} 에서 반격의 상대 역할로 쓰인다.
 * 몬스터들이 A, B, C … 로 구분되어야 해서 이름 대신 {@code suffix}(별명)를 둔다.
 */
public class Kinoko {

    /**
     * 버섯을 구분하는 별명. 예: "A", "B".
     * <p>
     * 몬스터 종류가 늘어날 때마다 클래스를 새로 만들지 않고
     * 이 값만 다르게 해서 하나의 클래스로 대응하는 방식이다.
     */
    public String suffix;
}
