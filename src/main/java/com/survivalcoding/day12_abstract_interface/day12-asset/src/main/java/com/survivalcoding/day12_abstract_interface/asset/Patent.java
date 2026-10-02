package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 2 의 구체 클래스]</b> 특허권 —— {@link Thing} 이 <b>아닌</b> 자산의 예.
 *
 * <h2>이 클래스의 존재 이유: 연습문제 3 의 "아니오"를 보여준다</h2>
 * 연습문제 3 은 "형태가 있는 것"에 {@link Thing} 을 붙이라고 했다.
 * 그러면 당연히 이런 질문이 따라온다.
 * <blockquote>"그럼 무게가 없는 것에는 {@code Thing} 을 붙이지 말아야 하나?"</blockquote>
 * 답은 <b>그래야 한다</b>. 그리고 이 클래스가 그 증거다.
 * <pre>
 * Asset
 *   ├─ TangibleAsset  implements Thing    ← 무게가 있다 → 붙인다
 *   │    ├─ Computer                     ← 물려받음
 *   │    └─ Book                         ← 물려받음
 *   └─ IntangibleAsset                    ← implements Thing 하지 않는다
 *        └─ Patent                        ← 무게가 없다 → 안 붙인다
 * </pre>
 * <p>
 * 만약 {@code Patent} 도 {@code Thing} 을 implements 했어야 한다면,
 * {@code getWeight()} 가 무엇을 반환해야 하는지 답할 수 없었을 것이다.
 * 0 을 반환하면 "무게가 0 kg 이다"라는 <b>거짓된 사실</b>을 만들어 버린다.
 * <p>
 * <b>그게 인터페이스를 아무 데나 붙이면 안 되는 이유다.</b>
 * 인터페이스는 "이 타입이면 반드시 이런 메서드를 가진다"를 약속하는데,
 * 약속을 지킬 내용이 없으면 인터페이스가 틀어진 것이다.
 * {@link Thing} 의 이름이 "무게를 잴 수 있는 것"이므로, 무게가 없는 것에는 애초에
 * {@code Thing} 이라는 분류가 성립하지 않는다. <b>분류 기준 자체가 맞지 않는 것이다.</b>
 *
 * <h2>{@code Patent} 에 무게 관련 메서드가 하나도 없는 이유</h2>
 * {@link IntangibleAsset} 가 {@code Thing} 을 implements 하지 않았으므로,
 * {@code getWeight()} / {@code setWeight()} 를 <b>구현해야 할 의무가 없다.</b>
 * 컴파일러도 요구하지 않는다. 이 클래스는 오로지 {@link Asset#getPrice()} 하나만 구현하면 된다.
 * <p>
 * 이 사실 자체가 인터페이스 설계의 질을 가르는 기준이다.
 * <b>반드시 의미 있는 메서드만 인터페이스에 넣어야 한다.</b>
 *
 * @see Asset
 * @see IntangibleAsset
 * @see Thing
 */
public class Patent extends IntangibleAsset {

    /** 이 특허권의 가치(가격). */
    private final int value;

    /**
     * 이름과 가치를 받는 생성자.
     * <p>
     * {@code TangibleAsset} 의 생성자와 <b>시그니처가 다르다</b> — 무게 파라미터가 없다.
     * 컴파일러가 이 차이를 그대로 강제한다. 무형자산이면서 무게를 받는 생성자를
     * 실수로 만들려면 부모 타입(인터페이스)이 아니라 <b>생성자 시그니처</b>를 바꿔야 한다.
     *
     * @param name  특허권의 이름
     * @param value 특허권의 가치
     */
    public Patent(String name, int value) {
        super(name);

        this.value = value;
    }

    /**
     * 특허권의 가치를 돌려준다 —— {@link Asset#getPrice()} 의 구현.
     * <p>
     * {@link Computer#getPrice()} 처럼 저장된 값을 그대로 돌려준다.
     * {@link Book#getPrice()} 처럼 계산하지 않는다 — 특허권의 가치는 당사자끼리 정해지는 값이기 때문이다.
     *
     * @return 이 특허권의 가치
     */
    @Override
    public int getPrice() {
        return value;
    }
}
