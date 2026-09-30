package com.survivalcoding;

/**
 * 용사(Hero)를 나타내는 클래스.
 * <p>
 * 이 저장소의 가장 오래된 클래스이며, 다른 클래스들이 상속(extends)하는
 * 부모 클래스 역할을 한다. (SuperHero, 그리고 간접적으로 PoisonSlime)
 * <p>
 * 필드(name, sword, hp)는 모두 private 으로 선언되어 외부에서 직접 접근할 수 없고,
 * 반드시 getter / setter 를 통해서만 접근하도록 캡슐화(encapsulation)했다.
 */
public class Hero {

    /**
     * 용사의 최대 HP.
     * <p>
     * {@link #sleep()} 가 HP 를 이 값으로 회복시키므로, 게임의 회복 상한 기준이 된다.
     * GreatWizard 의 superHeal() 도 이 값을 사용한다.
     */
    public static final int MAX_HP = 100;

    /**
     * 용사가 가진 돈.
     * <p>
     * 인스턴스 필드가 아니라 static(정적) 필드이므로,
     * {@code Hero.money} 처럼 클래스 이름으로 접근하며 모든 용사가 값을 공유한다.
     */
    static int money = 100;

    /** 용사의 이름. */
    private String name;

    /** 용사가 사용하는 검의 이름. */
    private String sword;

    /** 용사의 현재 HP. */
    private int hp;

    /**
     * 기본 생성자.
     * <p>
     * 필드를 모두 0 / null 로 초기화한 빈 인스턴스를 만든다.
     * 이후 setter 로 값을 채워 나가는 방식이다.
     */
    public Hero() {
    }

    /**
     * 이름과 HP를 인자로 받는 생성자.
     * <p>
     * SuperHero 가 {@code super(name, hp)} 로 이 생성자를 호출한다.
     * <p>
     * 생성자는 "초기 상태를 세우는 곳"이므로 검증을 하지 않고 필드에 직접 대입한다.
     * 값에 대한 제약 조건(길이 3자 이상, HP 0 이상 등)은 setName / setHp 가 담당한다.
     * 생성자에서 검증하면 예외가 던져져 객체가 반쪽만 만들어진 상태로 남을 수 있기 때문이다.
     *
     * @param name 용사의 이름
     * @param hp   용사의 HP
     */
    public Hero(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    // ==================== 이름 ====================

    /**
     * @return 용사의 이름
     */
    public String getName() {
        return this.name;
    }

    /**
     * 용사의 이름을 설정한다.
     *
     * @param name 용사의 이름. null 이거나 3글자 미만이면 예외가 발생한다.
     * @throws IllegalArgumentException 이름이 null 이거나 길이가 3 미만인 경우
     */
    public void setName(String name) {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(
                    "이름은 null일 수 없고 3문자 이상이어야 합니다."
            );
        }

        this.name = name;
    }

    // ==================== HP ====================

    /**
     * @return 용사의 현재 HP
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * 용사의 HP를 설정한다.
     * <p>
     * HP가 음수가 될 수는 없으므로, 음수가 들어오면 0 으로 보정(clamp)한다.
     * 이렇게 해야 HP 를 감소시키는 쪽(Hero.slip, Slime.attack 등)은
     * 0 아래로 내려가지 않았다고 따로 검사할 필요가 없어진다.
     *
     * @param hp 설정할 HP. 음수이면 0 이 저장된다.
     */
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    // ==================== 검 ====================

    /**
     * @return 용사가 사용하는 검의 이름
     */
    public String getSword() {
        return this.sword;
    }

    /**
     * 용사가 사용할 검의 이름을 설정한다.
     *
     * @param sword 검의 이름
     */
    public void setSword(String sword) {
        this.sword = sword;
    }

    /**
     * 기본 공격.
     * <p>
     * Hero 를 상속한 자식 클래스(SuperHero 등)가 각자 다른 공격 방식을
     * 정의할 수 있도록 비어 있는 뼈대로만 둔다.
     */
    public void attack() {
    }

    /**
     * 도망친다.
     * <p>
     * SuperHero 가 이 메서드를 {@code @Override} 로 다시 정의(오버라이드)한다.
     * 상속받은 메서드를 재작성하는 이것이 오버라이드다.
     */
    public void run() {
        System.out.println(this.name + "는 도망쳤다!");
        System.out.println("GAME OVER");
        System.out.println("최종 HP는 " + this.hp + "입니다");
    }

    /**
     * {@code sec} 초 동안 쉬어 HP를 회복한다.
     *
     * @param sec 앉아서 지내는 시간(초). 이 값만큼 HP가 증가한다.
     */
    public void sit(int sec) {
        this.hp += sec;

        System.out.println(this.name + "는 " + sec + "초 앉았다");
        System.out.println("HP가 " + sec + "포인트 회복되었다");
    }

    /**
     * 넘어져서 5포인트의 데미지를 입는다.
     * <p>
     * {@code this.hp -= 5} 만으로 HP 가 음수가 될 수 있으므로,
     * 0 아래로 내려가지 않도록 한 번 더 보정한다.
     */
    public void slip() {
        this.hp -= 5;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(this.name + "는 넘어졌다!");
        System.out.println("5의 데미지!");
    }

    /**
     * 잠을 자서 HP를 {@link #MAX_HP} 까지 완전히 회복한다.
     */
    public void sleep() {
        this.hp = MAX_HP;
        System.out.println(this.name + "는 잠을 자고 HP를 회복했다!");
    }

    /**
     * 용사가 동료를 떠나고 게임을 마친다.
     */
    public void bye() {
        System.out.println("용자는 이별을 고했다. 빠이");
    }

    /**
     * 용사가 죽었을 때 출력한다.
     * <p>
     * private 이므로 이 클래스 안에서만, 즉 오직 {@link #attack(Kinoko)} 에서만 호출된다.
     * "이 메서드는 외부에 노출하지 않는다"는 접근 제한의 예를 보여 준다.
     */
    private void die() {
        System.out.println(this.name + "는 죽었다");
        System.out.println("Game Over");
    }

    /**
     * 괴물버섯에게 반격을 받아 2포인트의 데미지를 입는다.
     * <p>
     * 메소드 오버로딩(overloading) 예시로, 매개변수 타입이 다른
     * {@link #attack()} 와 {@code attack(Kinoko)} 가 함께 존재한다.
     *
     * @param enemy 반격을 가하는 괴물버섯
     */
    public void attack(Kinoko enemy) {
        System.out.println("반격을 받았다");
        System.out.println(
                "괴물버섯" + enemy.suffix + "로부터 2포인트의 반격을 받았다"
        );

        // HP 를 직접 줄이지 않고 setHp() 를 거친다.
        // 그래야 음수가 0 으로 보정되어 setHp() 의 계약과 어긋나지 않는다.
        // (직접 this.hp -= 2 를 쓰면 HP 가 -1 같은 음수가 될 수 있었다)
        this.setHp(this.hp - 2);

        // HP 가 1 미만(= 사망) 이면 사망 메시지를 출력한다.
        if (this.hp < 1) {
            die();
        }
    }
}
