package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 에서 만든 추상 클래스]</b> —— 모든 자산의 공통 조상.
 *
 * <h2>이 클래스가 왜 필요한가</h2>
 * 연습문제 1 에서는 {@code TangibleAsset} 라는 추상 클래스만 만들었다.
 * <pre>
 *   Object
 *     └─ TangibleAsset
 *          ├─ Computer
 *          └─ Book
 * </pre>
 * 그런데 곧 "형태가 없는 무형자산(특허권 등)도 관리하고 싶다"는 요구가 들어온다(연습문제 2).
 * {@code IntangibleAsset} 을 만들면 이렇게 된다.
 * <pre>
 *   Object
 *     ├─ TangibleAsset          ← 컴퓨터, 책
 *     └─ IntangibleAsset        ← 특허권
 * </pre>
 * 그런데 둘의 공통 조상이 {@code Object} 라는 점이 문제다.
 * "자산이 총 몇 개 남았나"를 물으면 {@code Asset} 타입으로 담을 수가 없다.
 * 결국 모든 자산을 한 리스트에 모으려면 두 타입을 매번 따로 다루어야 한다.
 * <p>
 * 그래서 <b>{@code Asset} 이라는 한 단계가 더 필요</b>해진다. 이게 연습문제 2 이다.
 * <pre>
 *   Asset (추상)                    ← (가) : 자산의 일종
 *     ├─ TangibleAsset  (추상)      ← (나) : 손으로 만질 수 있는 자산
 *     │    ├─ Computer             ← (다)
 *     │    └─ Book                 ← (다)
 *     └─ IntangibleAsset (추상)     ← 무형자산
 *          └─ Patent
 * </pre>
 * <b>추상 클래스는 "계통이 같고 기능이 약간 다른 것"에 잘 맞는다</b> 라는 12장 본편의
 * 결론이 그대로 적용되는 자리다. 자산이라는 계통이 여기다.
 *
 * <h2>추상 메서드가 하는 일</h2>
 * {@link #getPrice()} 를 추상 메서드로 둔 이유는, 모든 자산이 "가격"을 갖기 때문이다.
 * 컴퓨터도 책도 특허권도 아무리 값의 차이가 있어도 <b>구하는 방법은 반드시 있다</b>.
 * <pre>
 * // Asset 이 일반 클래스였고 getPrice() 가 비어 있었다면
 * public class Asset {
 *     public int getPrice() {
 *         // 비어 있음 — 자식이 구현을 잊어도 컴파일이 된다
 *     }
 * }
 * </pre>
 * 이렇게 두면 미래의 개발자가 {@code Car} 를 만들면서 {@code getPrice()} 를 잊어도
 * 아무 오류 없이 컴파일된다. 그러면 {@code Asset} 타입의 리스트를 전부 출력하는 코드가
 * 조용히 잘못된 값을 찍는다. 버그는 실행해봐야 안다.
 * <p>
 * {@code abstract} 를 붙이면 그 실수를 <b>컴파일 에러</b>로 막을 수 있다.
 * 그리고 {@code Asset} 자체는 인스턴스화할 수 없게 되므로,
 * 가격이 정해지지 않은 "그냥 자산"이라는 존재를 실수로 만드는 일도 막는다.
 *
 * @see TangibleAsset
 * @see IntangibleAsset
 * @see Thing
 */

public abstract class Asset {

    /** 자산의 이름. {@code Computer} 라거나 {@code "3GPP 특허"} 같은 식이다. */
    private final String name;

    /**
     * 이름을 받는 생성자.
     * <p>
     * 가격은 이 생성자에 <b>없다.</b> 가격은 하위 클래스마다 계산 방법이 다르기 때문이다.
     * 부모는 "가격을 구할 수 있다"는 사실만 알고 있으면 된다.
     * 이게 추상 메서드의 위기도 하다 — 부모 생성자에서 추상 메서드를 호출할 수는 없으므로,
     * 가격을 여기서 확정시키는 것은 구조적으로 불가능하다.
     *
     * @param name 자산의 이름
     */
    public Asset(String name) {
        this.name = name;
    }

    /**
     * 자산의 이름을 돌려준다.
     * <p>
     * {@code name} 이 {@code private} 이므로, 자식 클래스도 이 getter 를 통해서만 이름을 얻는다.
     * private 은 자식에게 열려 있지 않는다.
     *
     * @return 자산의 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 이 자산의 가격을 돌려준다 —— {@link Asset} 의 유일한 추상 메서드.
     * <p>
     * 여기에는 구현이 없다. 세미콜론으로 끝난다는 점이 일반 메서드와의 차이다.
     * 자식 클래스가 이 메서드를 정의하지 않으면 컴파일 에러:
     * <pre>
     * error: TangibleAsset is not abstract and does not override
     *        abstract method getPrice() in Asset
     * </pre>
     * <p>
     * <b>{@link TangibleAsset} 에서도 구현하지 않는 이유</b> — 유형자산은 컴퓨터도 있을 수 있고
     * 책도 있을 수 있다. 가격을 구하는 방법을 부모가 정해버리면 자식이 자기 방식을 쓰지 못한다.
     * 그래서 <b>"가격을 구할 수 있다"까지만 부모가 정하고, "어떻게 구하는가"는 자식이 정하도록</b>
     * 미루는 것이다. 이 미루기가 추상 메서드의 본질이다.
     *
     * @return 이 자산의 가격
     */
    public abstract int getPrice();
}
