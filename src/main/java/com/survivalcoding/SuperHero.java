package com.survivalcoding;

/**
 * 슈퍼히어로(SuperHero)를 나타내는 클래스.
 * <p>
 * {@link Hero} 를 상속받아 기존 기능(공격, 도망, 앉기, 넘어지기 등)을 그대로 물려받고,
 * 날기와 착륙, 그리고 "멋지게 퇴각" 같은 자신만의 동작을 덧붙인다.
 * <p>
 * 이 클래스는 11장 - 상속의 대표 예시다.
 * <ul>
 *     <li>{@code extends Hero} : Hero 의 모든 것을 토대로 확장한다</li>
 *     <li>{@code super(name, hp)} : 부모의 생성자를 호출해 이름과 HP를 물려받는다</li>
 *     <li>{@link #run()} {@code @Override} : 부모의 도망치기를 다시 정의한다</li>
 *     <li>{@link #fly()} / {@link #land()} : 부모에 없던 새 기능은 그냥 추가한다</li>
 * </ul>
 * <p>
 * 올바른 상속인지는 "is-a 원칙"으로 판단한다.
 * SuperHero is a Hero → 슈퍼히어로는 용사의 한 종류이므로 상속해도 된다.
 */
public class SuperHero extends Hero {

    /** 현재 날고 있는지 여부. */
    private boolean isFlying;

    /**
     * 이름과 HP를 지정하는 생성자.
     * <p>
     * {@code super(name, hp)} 로 부모 {@link Hero} 의 생성자를 호출한다.
     * 부모가 소유한 필드(name, hp)는 super 로 넘겨서 부모가 채우게 한다.
     * <p>
     * 주의: 부모에 인자가 없는 생성자(기본 생성자)만 있다면 super() 를 생략해도 되지만,
     * 부모에 인자가 있는 생성자만 있다면 반드시 super(...) 를 명시해야 한다.
     *
     * @param name 슈퍼히어로의 이름
     * @param hp   슈퍼히어로의 HP
     */
    public SuperHero(String name, int hp) {
        super(name, hp);
    }

    // ==================== 비행 상태 ====================

    /**
     * @return 현재 날고 있으면 true
     */
    public boolean isFlying() {
        return isFlying;
    }

    /**
     * 비행 상태를 직접 설정한다.
     * <p>
     * boolean 필드의 getter 는 보통 {@code is} 를 붙여
     * {@code isFlying()} 같은 형태로 이름을 짓는다.
     *
     * @param flying true 면 날고 있는 상태
     */
    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    // ==================== 공격 ====================

    /**
     * 대상 슬라임을 공격한다.
     * <p>
     * 매개변수 타입이 {@link Slime} 이므로, {@link Hero#attack(Kinoko)} 와는
     * 매개변수가 달라 호출 시점에 구분된다. (오버로딩)
     * <p>
     * 참고: 수업 원본 설계대로 HP 10 감소가 대상이 아니라 <b>자기 자신</b>에게 적용된다.
     *
     * @param slime 공격 대상 슬라임
     */
    public void attack(Slime slime) {
        System.out.println(getName() + "이 공격했다");

        // 상속받은 Hero 의 hp 를 사용한다.
        setHp(getHp() - 10);

        if (getHp() <= 0) {
            System.out.println(getName() + "은 쓰러졌다.");
        }
    }

    // ==================== 오버라이드 ====================

    /**
     * 부모 {@link Hero#run()} 을 재정의(override)한다.
     * <p>
     * 서명(반환 타입과 매개변수 목록)이 부모와 같고 {@code @Override} 가 붙어 있다.
     * 호출하는 쪽이 {@code Hero} 타입이어도 실제 실행되는 것은 이 메서드다. (다형성)
     * <p>
     * {@code @Override} 는 컴파일러에게 "부모에 같은 이름의 메서드가 있어요"라고 알려 주는
     * 표식일 뿐이며, 붙여도 붙이지 않아도 동작은 같다.
     * 다만 오타를 잡아 주는 역할을 하므로 되도록 명시한다.
     */
    @Override
    public void run() {System.out.println("멋지게 퇴각했다");
    }

    // ==================== 비행 ====================

    /**
     * 날아올라 비행 상태가 된다.
     * <p>
     * 부모에 없던 새 기능이므로 {@code @Override} 를 붙이지 않는다.
     */
    public void fly() {
        isFlying = true;
        System.out.println(getName() + "이 날아올랐다.");
    }

    /**
     * 착륙하여 비행 상태가 해제된다.
     */
    public void land() {
        isFlying = false;
        System.out.println(getName() + "이 착륙했다.");
    }

    /**
     * 슈퍼히어의 상태를 문자열로 표현한다.
     * <p>
     * {@code @Override} 로 Object 의 toString 을 재정의한 것이다.
     * 이름은 private 필드이므로 상속받은 {@code getName()} / {@code getHp()} 로 접근한다.
     *
     * @return SuperHero{이름, HP, 비행 여부} 형태의 문자열
     */
    @Override
    public String toString() {
        return "SuperHero{" +
                "name=" + getName() +
                ", hp=" + getHp() +
                ", isFlying=" + isFlying +
                '}';
    }
}
