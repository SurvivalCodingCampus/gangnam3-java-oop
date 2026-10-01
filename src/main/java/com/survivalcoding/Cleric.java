package com.survivalcoding;

/**
 * 성직자(Cleric)를 나타내는 클래스.
 * <p>
 * 연습 문제에서 다룬 "생성자 오버로딩(constructor overloading)"의 대표 예시다.
 * 이름만, 이름 + HP, 이름 + HP + MP 를 받는 생성자 세 개를 두고
 * 모두 최종 생성자(3개짜리)로 {@code this(...)} 위임(delegation)한다.
 * 중복되는 초기화 코드를 한 곳에 모으는 방법이다.
 */
public class Cleric {

    /** 성직자의 최대 HP. */
    public static final int MAX_HP = 50;

    /** 성직자의 최대 MP. */
    public static final int MAX_MP = 10;

    /** 성직자의 이름. */
    public String name;

    /** 성직자의 현재 HP. */
    public int hp = MAX_HP;

    /** 성직자의 현재 MP. */
    public int mp = MAX_MP;

    /**
     * 이름, HP, MP를 모두 지정하는 최종 생성자.
     * <p>
     * 다른 두 생성자가 모두 여기로 {@code this(...)} 로 위임하므로,
     * 실제 초기화는 이 생성자 한 곳에서만 일어난다.
     *
     * @param name 성직자의 이름
     * @param hp   성직자의 HP
     * @param mp   성직자의 MP
     */
    public Cleric(final String name, final int hp, final int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    /**
     * 이름과 HP만 지정하는 생성자.
     * <p>
     * MP는 비워 두면 안 되므로 최대치인 {@link #MAX_MP} 로 초기화한다.
     *
     * @param name 성직자의 이름
     * @param hp   성직자의 HP
     */
    public Cleric(String name, int hp) {
        this(name, hp, MAX_MP);
    }

    /**
     * 이름만 지정하는 생성자.
     * <p>
     * HP와 MP는 각각 최대치({@link #MAX_HP}, {@link #MAX_MP})로 초기화한다.
     *
     * @param name 성직자의 이름
     */
    public Cleric(String name) {
        this(name, MAX_HP, MAX_MP);
    }

    /**
     * 자기 자신을 회복한다.
     * <p>
     * MP를 5 소모하는 대신 HP를 최대치까지 채운다.
     */
    public void selfAid() {
        mp -= 5;
        hp = MAX_HP;
    }

    /**
     * 정해진 시간(초) 동안 기도해 MP를 회복한다.
     * <p>
     * 회복량은 "기도한 시간 + 0~2 의 랜덤 값"이다.
     * {@code Math.random()} 은 0.0 이상 1.0 미만을 반환하므로,
     * 여기에 3 을 곱해 정수(int)로 캐스팅하면 0, 1, 2 중 하나가 나온다.
     * <p>
     * MP는 {@link #MAX_MP} 를 넘을 수 없으므로 초과분은 잘라내고,
     * 실제로 회복된 양(최대치를 고려한 값)을 반환한다.
     *
     * @param second 기도한 시간(초)
     * @return 실제로 회복된 MP의 양. 최대치에 걸려 더 회복되지 않았다면 그만큼 작아진다.
     */
    public int pray(int second) {
        // 1. 기도 시간에 0~2 의 랜덤 값을 더해 회복량을 정한다.
        int recovery = second + (int) (Math.random() * 3);

        // 2. 회복하기 전 MP를 기억해 둔다.
        int beforeMp = mp;

        // 3. MP를 회복시킨다.
        mp += recovery;

        // 4. 최대치를 넘으면 최대치로 잘라낸다.
        if (mp > MAX_MP) {
            mp = MAX_MP;
        }

        // 5. 실제로 회복된 양(최대치 보정 이후)을 돌려준다.
        return mp - beforeMp;
    }
}
