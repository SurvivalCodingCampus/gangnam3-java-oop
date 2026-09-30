package com.survivalcoding.day12_abstract_interface;

/**
 * {@link Attackable} 을 구현한 좀비 —— 인터페이스 구현의 가장 단순한 형태.
 * <p>
 * {@link Monster} 와 같은 {@code Character} 상속 계층에는 속하지 않는다.
 * 좀비는 "캐릭터이다"(is-a)가 아니라 "공격할 수 있다"(Can-Do)로 분류한 것이다.
 * <p>
 * 이 차이는 단순한 표현 문제가 아니다. {@code Monster} 처럼 계층에 넣으면
 * 모든 후손이 좀비의 HP 규칙을 물려받아야 하지만, 인터페이스로 구현하면
 * 좀비만의 규칙을 다른 계층의 객체와도 자유롭게 조합할 수 있다.
 *
 * @see Attackable
 */
public class Zombie implements Attackable {

    /** 좀비의 이름. */
    private final String name;

    /** 좀비가 한 번 때릴 때 주는 데미지. */
    private final int damage;

    /**
     * 이름과 데미지를 받는 생성자.
     *
     * @param name    좀비의 이름
     * @param damage  한 번 공격할 때의 데미지
     */
    public Zombie(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    /**
     * 좀비의 이름을 돌려준다.
     *
     * @return 좀비의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 공격한다 —— {@link Attackable#attack()} 의 구현.
     * <p>
     * <b>{@code public} 을 생략해도 된다.</b> 인터페이스의 메서드는 자동으로
     * {@code public} 이므로 구현할 때도 public 이고, 생략하면 자동으로 public 이 된다.
     * <p>
     * {@link Attackable#MAX_ATTACK_PER_TURN} 을 여기서 쓰는 것도 가능하다.
     * 인터페이스에 선언된 상수는 구현 클래스가 그대로 물려받아 쓸 수 있다.
     */
    @Override
    public void attack() {
        System.out.println(name + "이(가) " + damage + "의 데미지로 물어뜯습니다.");
        System.out.println("(인터페이스 상수: 한 턴 최대 " + MAX_ATTACK_PER_TURN + "회)");
    }
}