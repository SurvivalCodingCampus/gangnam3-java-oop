package com.survivalcoding.day12_abstract_interface;

/**
 * {@code extends} 와 {@code implements} 를 <b>둘 다</b> 사용한 클래스.
 * <pre>
 * public class Wizard extends Character implements Attackable, MagicCaster {
 * </pre>
 * 강의 자료의 "상속(extends)과 구현(implements)을 동시에 사용" 에 해당한다.
 *
 * <h2>두 키워드의 역할이 완전히 다르다</h2>
 * <table border="1">
 *     <caption>extends 와 implements 의 차이</caption>
 *     <tr><th></th><th>extends (상속)</th><th>implements (구현)</th></tr>
 *     <tr><td>의미</td><td>is-a (것이다)</td><td>Can-Do (할 수 있다)</td></tr>
 *     <tr><td>대상</td><td>class</td><td>interface</td></tr>
 *     <tr><td>개수</td><td>1개만</td><td>여러 개 가능</td></tr>
 *     <tr><td>물려주는 것</td><td>필드 + 메서드</td><td>메서드 시그니처만</td></tr>
 * </table>
 * <p>
 * 이 클래스에서 실제로 쓰인 것:
 * <ul>
 *     <li>{@code extends Character} → <b>"마법사는 캐릭터다"</b>
 *         부모의 {@code name} 필드, {@code getName()}, 그리고 추상 메서드 {@code attack()} 시그니처를 물려받는다.</li>
 *     <li>{@code implements Attackable, MagicCaster} → <b>"전투도 할 수 있고 마법도 쓸 수 있다"</b>
 *         두 인터페이스의 메서드를 직접 구현할 책임이 생긴다.</li>
 * </ul>
 * 상속은 <b>구조를 빌려오고</b>, 구현은 <b>능력을 선언한다.</b>
 *
 * <h2>일반전투와 마법이 모두 가능한 마법사</h2>
 * {@code Attackable} 만 구현했다면 근접 전투 specialist 였을 것이다.
 * {@code MagicCaster} 까지 implements 해서 "전투 + 마법"을 모두 갖춘 캐릭터가 되었다.
 * 이런 <b>능력의 조합</b>을 클래스의 상속 계층이 아니라 implements 목록으로 표현한 것이다.
 *
 * @see Character
 * @see Attackable
 * @see MagicCaster
 */
public class Wizard extends Character implements Attackable, MagicCaster {

    /** 화염구 한 번에 필요한 마력. */
    public static final int MAGIC_COST = 10;

    /** 마법사의 현재 마력. */
    private int mp;

    /**
     * 이름과 MP를 받는 생성자.
     *
     * @param name 마법사의 이름
     * @param mp   시작 마력
     */
    public Wizard(String name, int mp) {
        // extends 로 물려받은 부모의 생성자 호출
        super(name);

        this.mp = mp;
    }

    /**
     * 현재 마력을 돌려준다.
     *
     * @return 현재 마력
     */
    public int getMp() {
        return mp;
    }

    /**
     * 근접 공격한다 — {@link Attackable#attack()} 의 구현.
     * <p>
     * 부모 {@link Character#attack()} 를 implements 로도 구현하게 됐다.
     * 결과적으로 {@code Wizard} 는 상속과 구현을 <b>둘 다</b> 한 메서드를 가진다.
     */
    @Override
    public void attack() {
        System.out.println(getName() + "이(가) 지팡이로 근접 공격합니다!");
    }

    /**
     * 마법을 사용한다 — {@link MagicCaster#castMagic()} 의 구현.
     * <p>
     * 마력이 부족하면 {@code IllegalStateException} 을 던져 호출한 쪽에 알린다.
     * 단순히 "출력만 하고 끝내기" 보다 나은 처리다 — 잘못된 상태가 조용히 넘어가지 않는다.
     * <p>
     * <b>반드시 MP 를 줄이기 전에 검사해야 한다.</b>
     * {@code if (mp <= 0)} 처럼 "0 이하인가" 만 보면, MP 가 5 인 상태에서 마법을 쓰면
     * 그대로 음수(-5) 가 된다. 필요한 양({@link #MAGIC_COST})과 비교해야 한다.
     * (마찬가지로 {@code Hero.setHp} 가 하는 "0 하한 보정"과 같은 종류의 실수다)
     */
    @Override
    public void castMagic() {
        if (mp < MAGIC_COST) {
            throw new IllegalStateException(
                    getName() + "의 마력이 부족합니다. 필요: " + MAGIC_COST + ", 보유: " + mp
            );
        }

        mp -= MAGIC_COST;
        System.out.println(getName() + "이(가) 화염구를 던집니다. 남은 MP: " + mp);
    }
}