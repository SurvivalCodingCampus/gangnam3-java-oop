package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 4 의 정답]</b> —— 유형자산.
 *
 * <h2>연습문제 4 의 요구사항</h2>
 * <blockquote>
 * "유형자산(TangibleAsset)은, 자산(Asset)의 일종이며, 형태가 있는 것(Thing)의 일종이기도 하다.
 * 이 정의에 맞도록 TangibleAsset 의 소스 코드를 수정하시오.
 * 이 때, TangibleAsset 에 필드나 메소드의 추가가 필요하다면, 적당히 추가하시오."
 * </blockquote>
 * <p>
 * "자산의 일종"과 "무게를 잴 수 있는 것의 일종"을 <b>한 문장 안에 두 개나 넣었다</b>는 점이
 * 이 문제의 전부다. 두 개의 서로 다른 계약을 동시에 만족시키라는 요구다.
 *
 * <h2>한 줄로 정리한 해결</h2>
 * <pre>
 * public abstract class TangibleAsset extends Asset implements Thing {
 *     private double weight;                                 // ← 추가한 필드
 *
 *     public TangibleAsset(String name, double weight) { ... }
 *
 *     &#64;Override public double getWeight()  { ... }      // ← 추가한 메서드
 *     &#64;Override public void setWeight(double weight) { ... }
 * }
 * </pre>
 *
 * <h2>왜 extends Asset 과 implements Thing 을 둘 다 쓰는가</h2>
 * 두 키워드가 하는 일이 완전히 다르다. 12장 본편에서 여러 번 나온 그 차이다.
 * <table border="1">
 *     <caption>이 한 줄에 들어간 두 키워드</caption>
 *     <tr><th></th><th>extends Asset</th><th>implements Thing</th></tr>
 *     <tr><td>뜻</td><td>"유형자산은 자산이다" (is-a)</td><td>"유형자산은 무게를 잴 수 있다" (Can-Do)</td></tr>
 *     <tr><td>물려주는 것</td><td>이름 필드, {@code getName()} — <b>구현까지 통째로</b></td>
 *         <td>{@code getWeight()}, {@code setWeight()} 시그니처 — <b>구현은 내 것</b></td></tr>
 *     <tr><td>여기서 하는 일</td><td>아무것도 안 해도 된다</td><td>메서드 2개를 직접 구현해야 한다</td></tr>
 * </table>
 * <p>
 * <b>extends Asset 으로는 아무것도 안 해도 되는 이유</b> — Asset 은 추상 클래스지만
 * {@code getName()} 과 이름 필드는 이미 완성되어 있다. 그대로 물려받으면 끝이다.
 * <p>
 * <b>implements Thing 은 반드시 손을 써야 하는 이유</b> — 인터페이스는 구현이 하나도 없다.
 * 물려받을 게 없으므로 {@code getWeight()}, {@code setWeight()} 를 <b>직접 만들어야</b> 한다.
 * 안 만들면 컴파일 에러:
 * <pre>
 * error: TangibleAsset is not abstract and does not override
 *        abstract method setWeight(double) in Thing
 * </pre>
 *
 * <h2>"필드나 메소드를 적당히 추가하라" — 왜 weight 필드를 여기 두는가</h2>
 * 연습문제 3 은 {@code Thing} 인터페이스만 만들었다. 인터페이스는 <b>행동(무게를 구하고 바꾸는)</b>만
 * 약속할 뿐, <b>데이터(무게 값 자체)</b>를 저장할 자리는 제공하지 않는다.
 * 슬라이드 12장 본편에 분명히 나왔듯이 <b>인터페이스는 인스턴스 필드를 가질 수 없다.</b>
 * <p>
 * 그러면 무게 값은 어디에 저장해야 하는가? 답은 <b>구현 클래스</b>다. 그것이 연습문제 4 가
 * "필드나 메소드를 추가하시오"라고 말한 이유다.
 * <pre>
 * interface Thing  →  "무게를 잴 수 있다"는 능력 + getWeight/setWeight 시그니처
 * TangibleAsset   →  private double weight 필드 + 그 시그니처의 실제 구현  ← 추가
 * </pre>
 * <p>
 * <b>왜 Computer 나 Book 이 아니라 TangibleAsset 에 넣는가</b> — 무게는 컴퓨터만의 것도,
 * 책만의 것도 아니다. 유형자산 <b>전부</b>의 공통 속성이다. 여기 한 번만 넣으면
 * {@link Computer} 와 {@link Book} 이 자동으로 물려받으므로 중복이 생기지 않는다.
 * 만약 자식마다 따로 넣었다면, 나중에 {@code Car}, {@code Desk} 같은 유형자산을 추가할 때마다
 * 같은 코드를 다시 작성해야 했을 것이다. <b>공통은 최대한 위에서 한 번만</b> — 이것이 상속의 기본 원칙이다.
 *
 * <h2>여전히 abstract 인 이유 (중요)</h2>
 * {@code getWeight()}, {@code setWeight()}, 이름 필드까지 다 구현했는데도
 * {@code TangibleAsset} 은 여전히 {@code abstract} 이다. 도대체 뭐가 남은 걸까?
 * <p>
 * 답은 <b>부모 {@link Asset} 의 {@link Asset#getPrice()} 이 아직 비어 있기 때문</b>이다.
 * 추상 메서드 하나라도 남아 있으면 자식은 추상 클래스다. 이 상태가 정답이다.
 * <pre>
 * // Asset.getPrice() 미구현 → 컴파일 에러
 * // error: TangibleAsset is not abstract and does not override
 * //        abstract method getPrice() in Asset
 * </pre>
 * "유형자산"은 구체적인 자산이 아니다. 컴퓨터도 책도 아닌, 그저 분류 이름일 뿐이다.
 * <b>그래서 만들어지지 않아야 하고, 그래서 {@code new TangibleAsset(...)} 는 컴파일 에러다.</b>
 * 실제 자산의 가격은 {@link Computer}, {@link Book} 이 각각 결정한다.
 *
 * @see Asset
 * @see Thing
 * @see Computer
 * @see Book
 */
public abstract class TangibleAsset extends Asset implements Thing {

    /**
     * 이 유형자산의 무게(kg).
     * <p>
     * <b>연습문제 3 에서 이 필드가 없었던 이유</b> — {@link Thing} 은 인터페이스이고,
     * 인터페이스는 필드를 가질 수 없다. (자동으로 {@code public static final} 이 되기 때문에
     * <b>인스턴스마다 다른 무게</b>를 저장하는 용도로 쓸 수 없다)
     * <p>
     * <b>여기서는 왜 final 이 아닌가</b> — {@code Asset.name} 은 final 이지만 무게는 아니다.
     * 무게는 계측 후 확정되기도 하고, 나중에 실제 측정값으로 교정되기도 한다.
     * 그래서 {@link #setWeight(double)} 로 바꿀 수 있게 <b>final 을 붙이지 않았다.</b>
     * 인터페이스가 get/set <b>둘 다</b>를 요구한 대로다.
     */
    private double weight;

    /**
     * 이름과 무게를 받는 생성자.
     * <p>
     * <b>가격은 받지 않는다.</b> 이 클래스는 여전히 추상 클래스이고
     * {@link Asset#getPrice()} 를 구현하지 않았으므로, 가격이 결정되지 않은
         * "중간제품" 인스턴스를 만들 수 없어야 한다.
     * <p>
     * 생성자 호출 규칙도 그대로 적용된다 — {@code super(name)} 로 부모의 생성자를 호출해
     * {@link Asset} 의 {@code name} 필드를 채운다. 부모가 private 필드를 갖고 있으므로
     * 이 방법밖에 없다.
     *
     * @param name   자산의 이름. {@code super(name)} 으로 부모에게 넘긴다.
     * @param weight 자산의 무게(kg). {@code this.weight} 에 저장한다.
     */
    public TangibleAsset(String name, double weight) {
        super(name);

        this.weight = weight;
    }

    /**
     * 이 유형자산의 무게를 돌려준다 —— {@link Thing#getWeight()} 의 구현.
     * <p>
     * {@code Thing} 에서 물려받은 <b>시그니처만</b> 물려받은 것이고, 이 본문은
     * {@code TangibleAsset} 가 직접 작성한 것이다. 이 한 줄이 implements 의 전부다.
     * <p>
     * <b>{@code @Override} 를 붙이는 실질적인 이득</b> — 실수로 이름을 잘못 쓰면(예: {@code getWeigt})
     * 이 어노테이션 자리에서 컴파일 에러가 난다. 인터페이스의 시그니처는 상속이 아니라
     * "구현"이라 오타를 조용히 통과시킬 수 있는데, {@code @Override} 가 그 실수를 잡아준다.
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * 이 유형자산의 무게를 바꾼다 —— {@link Thing#setWeight(double)} 의 구현.
     * <p>
     * 지금은 대입만 한다. 다만 실제 프로젝트에서는 0 이하가 들어오면 물리적으로 말이 안 되므로
     * {@code if (weight < 0) throw new IllegalArgumentException(...)} 같은 방어 코드가 필요하다.
     * 이 예제에서는 개념 전달이 목적이므로 최소한으로만 구현했다.
     *
     * @param weight 새로 설정할 무게(kg)
     */
    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * 자산 정보를 한 줄로 출력한다.
     * <p>
     * 이건 연습문제의 요구사항은 아니지만, 연습 3 에서 만든 {@code Thing} 이
     * <b>무게를 다루는 규격</b>이라는 사실을 눈으로 확인하기 위해 필요하다.
     * {@link Asset#getName()} 은 extends 로, {@link #getWeight()} 는 implements 로 얻은 메서드다.
     * <p>
     * {@code Asset} 과 {@code Thing} <b>둘 다 implements 하지 않았다는 사실이 중요하다.</b>
     * 둘 다 상속했다면 인터페이스를 쓸 이유가 없고, 둘 다 implements 했다면
     * 클래스의 단일 상속 제한에 걸렸을 것이다. <b>클래스 계층(Asset)과
     * 역할 규격(Thing)을 분리</b>한 것이 이 설계의 핵심이다.
     */
    public void printInfo() {
        System.out.println(getName() + " : " + getWeight() + "kg");
    }
}
