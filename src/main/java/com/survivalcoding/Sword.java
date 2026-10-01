package com.survivalcoding;

/**
 * 검(Sword)을 나타내는 클래스.
 * <p>
 * {@link Hero} 가 사용하는 장비이며, {@link Wand} 와 함께
 * "장비"라는 공통점을 가진 클래스의 예시다.
 * <p>
 * 이 클래스는 연습에서 getter/setter 를 붙이지 않고
 * 필드를 public 으로 공개한 초기 형태 그대로 남아 있다.
 * {@link Wand} 와 비교해 보면 캡슐화의 필요성을 이해하기 좋다.
 */
public class Sword {

    /** 검의 이름. */
    public String name;

    /** 검의 공격력(데미지). */
    public int damage;
}
