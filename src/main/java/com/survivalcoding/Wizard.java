package com.survivalcoding;

/**
 * 마법사(Wizard)를 나타내는 클래스.
 * <p>
 * 연습문제 4의 대상이다.
 * 연습문제 5({@link GreatWizard})에서 이 클래스를 상속받아
 * {@link #heal(Hero)} 를 재정의(오버라이드)한다.
 */
public class Wizard {

    /**
     * 마나가 부족할 때 출력하는 메시지.
     * <p>
     * 자식 클래스인 GreatWizard 도 같은 메시지를 사용하므로
     * {@code protected} 로 선언해 상속받는 클래스에서 함께 쓴다.
     * (private 로 선언했다면 자식에서 접근할 수 없어 같은 문자열을 따로 정의해야 한다)
     */
    protected static final String NOT_ENOUGH_MP_MESSAGE = "마나가 부족합니다";

    // ==================== 필드 ====================

    /** 마법사의 이름. */
    private String name;

    /** 마법사의 현재 HP. */
    private int hp;

    /** 마법사의 현재 MP. 연습문제 4의 요구사항에 따라 초기값은 100 이다. */
    private int mp = 100;

    /** 마법사가 사용하는 지팡이. */
    private Wand wand;

    /**
     * 기본 생성자.
     * <p>
     * 필드 초기값(name = null, hp = 0, mp = 100, wand = null)만 가진 인스턴스를 만든다.
     */
    public Wizard() {
    }

    /**
     * 이름, HP, MP를 모두 지정하는 생성자.
     * <p>
     * GreatWizard 가 {@code super(name, hp, mp)} 로 이 생성자를 호출한다.
     *
     * @param name 마법사의 이름
     * @param hp   마법사의 HP
     * @param mp   마법사의 MP
     */
    public Wizard(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // ==================== 이름 ====================

    /**
     * @return 마법사의 이름
     */
    public String getName() {
        return this.name;
    }

    /**
     * 마법사의 이름을 설정한다.
     *
     * @param name 마법사의 이름. null 이거나 3글자 미만이면 예외가 발생한다.
     * @throws IllegalArgumentException 이름이 null 이거나 길이가 3 미만인 경우
     */
    public void setName(String name) {

        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "마법사의 이름은 null일 수 없으며 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // ==================== HP ====================

    /**
     * @return 마법사의 현재 HP
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * 마법사의 HP를 설정한다. 음수가 들어오면 0 으로 보정한다.
     *
     * @param hp 설정할 HP
     */
    public void setHp(int hp) {

        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    // ==================== MP ====================

    /**
     * @return 마법사의 현재 MP
     */
    public int getMp() {
        return this.mp;
    }

    /**
     * 마법사의 MP를 설정한다.
     * <p>
     * HP와 달리 MP는 음수가 되면 에러로 처리한다.
     * HP는 0 이라는 자연스러운 하한이 있지만, MP는 "0 이다"와 "잘못된 값이다"를
     * 구분할 필요가 없기 때문이다. 잘못된 값은 조용히 고치는 것보다 빨리 드러내는 편이 낫다.
     *
     * @param mp 설정할 MP
     * @throws IllegalArgumentException MP가 음수인 경우
     */
    public void setMp(int mp) {
        if (mp < 0) {
            throw new IllegalArgumentException("MP는 0 이상이어야 합니다.");
        }

        this.mp = mp;
    }

    // ==================== 지팡이 ====================

    /**
     * @return 마법사가 사용하는 지팡이
     */
    public Wand getWand() {
        return this.wand;
    }

    /**
     * 마법사가 사용할 지팡이를 설정한다.
     *
     * @param wand 지팡이. null 일 수 없다.
     * @throws IllegalArgumentException 지팡이가 null 인 경우
     */
    public void setWand(Wand wand) {

        if (wand == null) {
            throw new IllegalArgumentException(
                    "마법사의 지팡이는 null일 수 없습니다."
            );
        }

        this.wand = wand;
    }

    // ==================== 힐 ====================

    /**
     * 대상 용사의 HP를 20 회복시키고 자신의 MP를 10 소모한다. (연습문제 4)
     * <p>
     * MP가 10 보다 적으면 회복하지 않고 {@link #NOT_ENOUGH_MP_MESSAGE} 만 출력한 뒤
     * 즉시 반환한다. 모든 MP 를 소모한 뒤에도 메서드는 정상적으로 끝나야 하기 때문에
     * 예외를 던지지 않는다.
     *
     * @param hero 힐을 받을 대상 용사
     */
    public void heal(Hero hero) {

        // 1. MP가 10보다 적으면 힐을 사용할 수 없다.
        if (this.mp < 10) {
            System.out.println(NOT_ENOUGH_MP_MESSAGE);
            return;
        }

        // 2. 대상 HP를 20 회복
        hero.setHp(hero.getHp() + 20);

        // 3. 자신의 MP 10 소모
        this.mp -= 10;

        // 4. 힐 성공 메시지
        System.out.println(
                "힐을 시전했습니다. 대상 HP: " + hero.getHp()
        );
    }
}
