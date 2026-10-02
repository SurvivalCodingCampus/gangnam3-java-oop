package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 3] 형태가 있는 것</b> —— 무게를 가진다는 "규격"만 선언한 인터페이스.
 *
 * <h2>연습문제 3 의 요구사항</h2>
 * <blockquote>
 * "자산인지 아닌지 따지지 말고, 형태가 있는 것(Thing)이면, 무게가 있다.
 * 그래서, double 형으로 무게(weight)를 얻을 수 있는 getter / setter 메소드를 가지는
 * 인터페이스 Thing 을 만들고 클래스 다이어그램을 업데이트 하시오."
 * </blockquote>
 *
 * <h2>이 문제의 핵심: 분류의 기준을 바꾼다</h2>
 * 앞선 연습문제(1, 2)에서는 모든 걸음을 {@code Asset} — 즉 "자산인가?"라는
 * <b>하나의 기준</b> 위에서 나눠 놓았다.
 * <pre>
 *   Asset
 *     ├─ TangibleAsset  (유형자산 : 손으로 만질 수 있다)
 *     └─ IntangibleAsset (무형자산 : 손으로 만질 수 없다)
 * </pre>
 * 그런데 이번 연습은 시선이 통째로 달라진다.
 * <blockquote>"자산인지 아닌지 따지지 말고"</blockquote>
 * 즉 <b>자산이라는 개념을 완전히 버린다.</b> 남는 기준은 단 하나,
 * <b>"형태가 있는가(무게가 있는가)"</b> 뿐이다.
 * <p>
 * 이 기준을 따르면 결과가 기묘해진다. {@code Patent}(특허권) 같은 무형자산은
 * {@code Thing} 이 <b>아니게</b> 된다. 무게를 잴 수 없으니까.
 * 그리고 {@code TangibleAsset}(유형자산)는 {@code Thing} 이 된다. 무게가 있으니까.
 * <p>
 * 이게 이 연습의 핵심이다. <b>같은 객체가 어떤 기준으로 묶이느냐에 따라
 * "무엇인가"가 달라진다.</b> 누가 옳고 틀린 게 아니라, 분류 기준에 따라
 * 다른 이름이 붙는다는 것. 그래서 {@code Thing} 을 만든 뒤로는
 * {@code Asset} 계층이고 상관없이 무게가 있는 것들만 한 자리에 모을 수 있다.
 *
 * <h2>왜 인터페이스인가 (클래스가 아니라)</h2>
 * {@code Asset} 쪽은 계층(is-a)이므로 <b>extends</b> 를 써야 한다. 그런데
 * {@code Thing} 은 계층이 아니다. "자산"이라는 위계와는 <b>완전히 별개의 축</b>이기 때문이다.
 * <table border="1">
 *     <caption>Asset 축과 Thing 축은 서로 독립적이다</caption>
 *     <tr><th></th><th>extends 축 (is-a)</th><th>implements 축 (Can-Do)</th></tr>
 *     <tr><td>기준</td><td>무엇인가</td><td>무엇을 할 수 있는가 / 어떤 속성인가</td></tr>
 *     <tr><td>예</td><td>컴퓨터는 자산이다</td><td>컴퓨터는 무게를 잴 수 있다</td></tr>
 *     <tr><td>어울리는 것</td><td>추상 클래스</td><td>인터페이스</td></tr>
 * </table>
 * <p>
 * 그리고 결정적인 이유 — {@link Asset} 을 상속한 {@link TangibleAsset} 같은 클래스는
 * 이미 다른 부모를 하나 갖고 있다. <b>클래스는 한 개만 extends 할 수 있다.</b>
 * 만약 "무게를 잴 수 있다"를 클래스로 만들었다면 {@code TangibleAsset} 가
 * 그것을 상속받을 자리가 없다. 인터페이스만이 여러 개를 implements 할 수 있어서
 * 이 자리가 생긴다. (12장 본편 슬라이드 "인터페이스의 특별 취급")
 *
 * <h2>메서드 구현을 강제하는 효과</h2>
 * 이 인터페이스를 implements 하는 클래스는 {@code getWeight()} 와
 * {@code setWeight(double)} 를 <b>둘 다</b> 반드시 만들어야 한다.
 * 하나라도 빠뜨리면 컴파일 에러:
 * <pre>
 * error: Computer is not abstract and does not override abstract method getWeight() in Thing
 * </pre>
 * 덕분에 "무게를 잴 수 있다고 선언했는데 실제로 잴 방법이 없는" 객체가 생기지 않는다.
 *
 * @see TangibleAsset
 * @see Asset
 */
public interface Thing {

    /**
     * 이 것의 무게(kg)를 돌려준다.
     * <p>
     * <b>반환 타입이 {@code double} 인 이유</b> — 무게는 1.5 kg 처럼 소수점이 나올 수 있다.
     * {@code int} 로 선언하면 "무게가 항상 정수"라는 사실과 상관없이
     * 컴파일러가 소수점 대입을 막아버린다. 여기서 {@code double} 을 택한 것은 단순한 타입 선택이 아니라,
     * 실수형이 "물리량"을 표현한다는 관례를 따른 것이다.
     * <p>
     * <b>구현이 없는 선언만 존재한다.</b> 본문({@code {}}) 대신 세미콜론으로 끝나는 것이
     * 인터페이스 메서드의 특징이다. "이런 메서드가 있다"는 <b>약속만</b> 남기고,
     * 실제로 어떻게 무게를 재는지는 구현 클래스가 정한다.
     * <p>
     * {@code public abstract} 는 자동으로 붙으므로 굳이 쓰지 않는다.
     */
    double getWeight();

    /**
     * 이 것의 무게를 바꾼다.
     * <p>
     * <b>왜 setter 까지 요구하는가</b> — getWeight() 만 있으면 "무게는 있지만 바꿀 수 없는" 객체가 된다.
     * 그런데 무게는 이미 실측된 값이므로, 나중에 실제 측정 결과로 <b>갱신</b>되는 것이 자연스럽다.
     * get/set 을 한 쌍으로 요구하면 "읽을 수 있고 쓸 수 있는 값"이라는 계약이 완성된다.
     * <p>
     * 반환 타입이 {@code void} 인 것도 자연스럽다. 무게를 설정한 뒤의 값을 돌려줄 필요가 없다.
     * 값을 다시 알고 싶으면 {@code setWeight(w); getWeight();} 라고 두 번 호출하면 된다.
     *
     * @param weight 새로 설정할 무게(kg)
     */
    void setWeight(double weight);
}
