package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 2 의 추상 클래스]</b> —— 무형자산.
 *
 * <h2>이 클래스가 연습문제 3 을 이해하는 데 중요한 이유</h2>
 * 이 클래스는 연습문제 4(내부)와 무관하지만, 연습문제 3 을 <b>이해하는 앵커</b>가 된다.
 * {@link Patent} 이 무형자산이므로 {@code Thing} 을 <b>구현하지 않는다</b>. 무게를 잴 수 없으니까.
 * <pre>
 *   Patent extends IntangibleAsset extends Asset
 *   Patent ─ implements X ─▶ Thing     ✗ (하지 않는다)
 *
 *   Computer extends TangibleAsset extends Asset
 *   Computer ─ implements ▶ Thing     ◀ ✓ (TangibleAsset 가 구현해 물려받음)
 * </pre>
 * <p>
 * 즉 {@code Thing} 이 {@code Asset} 계층 <b>위/아래로 파고들지 않는다는 것</b>이 이 구조의 핵심이다.
 * 자산인지 아닌지를 따지지 않겠다는 연습문제 3 의 요구가 여기서 구체적으로 드러난다.
 * 같은 {@code Asset} 계층 안에서 {@code Patent} 만 {@code Thing} 이 아니게 되는,
 * <b>독립적인 두 축</b>의 설계가 성립하는 이유다.
 *
 * @see Asset
 * @see TangibleAsset
 * @see Patent
 * @see Thing
 */
public abstract class IntangibleAsset extends Asset {

    /**
     * 이름과 설명을 받는 생성자.
     * <p>
     * {@code TangibleAsset} 와 다른 점 — 무게를 받지 않는다.
     * 무게가 없는 존재이므로 {@link Thing} 을 구현하지 않고, 그에 필요한
     * {@code getWeight()} / {@code setWeight()} 도 없다.
     *
     * @param name 자산의 이름
     */
    public IntangibleAsset(String name) {
        super(name);
    }
}
