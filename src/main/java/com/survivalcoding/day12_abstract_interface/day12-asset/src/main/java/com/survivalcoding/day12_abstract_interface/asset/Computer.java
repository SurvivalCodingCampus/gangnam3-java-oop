package com.survivalcoding.day12_abstract_interface.asset;

/**
 * <b>[12장 연습문제 1 의 기존 클래스 → 연습문제 4 까지 계층을 따라 개편된 결과]</b> 컴퓨터.
 *
 * <h2>연습문제 1 이후 이 클래스는 얼마나 줄었는가</h2>
 * 연습문제 1 이전의 {@code Computer} 는 <b>혼자서 다 처리하는 클래스</b>였다.
 * 이름과 가격을 각각 필드로 갖고, 그에 맞는 getter 도 스스로 갖고 있었다.
 * <pre>
 * // 연습문제 1 이전 — 독립 클래스 (parent 가 Object 였던 시절)
 * public class Computer {
 *     private String name;      // 이름
 *     private int price;        // 가격
 *     public Computer(String name, int price) { ... }
 *     public String getName() { return name; }   // 이름 getter
 *     public int getPrice() { return price; }     // 가격 getter
 * }
 * </pre>
 * 그리고 {@code Book} 이 거의 똑같은 모양이었다.
 * <b>문제는 이 "거의 똑같음"이 코드에 남아 있었다는 것이다.</b> 이름을 어디에 저장할지,
 * 이름을 어떻게 읽을지를 두 클래스가 각자 결정하고 있었다.
 * <p>
 * 연습문제 1, 2 를 거치며 이름은 {@link Asset} 으로 올라가고, 무게는 {@link TangibleAsset} 으로 올라갔다.
 * <b>그래서 이 클래스에는 {@code price} 하나만 남는다.</b> 그것도 {@link Asset#getPrice()} 라는
 * 추상 메서드를 <b>구현</b>하기 위해서만 존재한다.
 *
 * <h2>이 클래스의 정체</h2>
 * 이 클래스는 사실상 <b>딱 한 가지 일만 한다</b> — "내 가격은 이 값이다"라고 말해주는 것.
 * 나머지는 전부 물려받았다.
 * <table border="1">
 *     <caption>{@code Computer} 의 메서드 출처</caption>
 *     <tr><th>메서드</th><th>어디서 왔는가</th><th>키워드</th></tr>
 *     <tr><td>{@code getName()}</td><td>{@link Asset}</td><td>extends (2단계 위)</td></tr>
 *     <tr><td>{@code getWeight()}</td><td>{@link TangibleAsset}</td><td>implements + extends</td></tr>
 *     <tr><td>{@code setWeight(double)}</td><td>{@link TangibleAsset}</td><td>implements + extends</td></tr>
 *     <tr><td>{@code printInfo()}</td><td>{@link TangibleAsset}</td><td>extends (부모)</td></tr>
 *     <tr><td>{@code getPrice()}</td><td><b>여기서 직접 구현</b></td><td>추상 메서드 구현</td></tr>
 * </table>
 *
 * <h2>{@code getPrice()} 하나만 직접 구현하는 이유</h2>
 * {@code Asset.getPrice()} 는 추상 메서드였으므로, 이 클래스가 구체 클래스(실제 인스턴스를
 * 만들 수 있는 클래스)가 되려면 반드시 구현해야 한다. 안 하면 컴파일 에러:
 * <pre>
 * error: Computer is not abstract and does not override abstract method getPrice() in Asset
 * </pre>
 * <p>
 * <b>여기서 "컴퓨터의 가격은 얼마냐"를 결정하는 것이 컴퓨터 고유의 역할</b> 인 것이다.
 * 책의 가격 계산과 컴퓨터의 가격 계산은 다른데, 부모 {@code Asset} 은 그 차이를 알 필요가 없다.
 * 이것이 추상 메서드가 존재하는 이유 그 자체다.
 *
 * @see Asset
 * @see TangibleAsset
 * @see Book
 */
public class Computer extends TangibleAsset {

    /** 이 컴퓨터의 가격. */
    private final int price;

    /**
     * 이름, 무게, 가격을 받는 생성자.
     * <p>
     * <b>{@code super(name, weight)} 로 부모에게 두 값을 넘긴다.</b>
     * {@code weight} 는 이 클래스에도, {@link TangibleAsset} 에도 필드가 없고
     * 오직 {@link TangibleAsset#getWeight()} / {@link TangibleAsset#setWeight(double)} 로만 다룬다.
     * 그래서 생성자에서 <b>넘기기만 하면 끝</b>이고, 따로 저장할 필요가 없다.
     * <p>
     * 생성자 호출은 항상 1개만, 그리고 가장 위쪽 부모를 향해 간다.
     * {@code super} 를 생략하면 컴파일 에러가 나므로, 이 규칙은 외우는 게 아니라
     * 컴파일러가 강제해 주는 규칙이다.
     *
     * @param name   컴퓨터의 이름
     * @param weight 컴퓨터의 무게(kg)
     * @param price  컴퓨터의 가격
     */
    public Computer(String name, double weight, int price) {
        super(name, weight);

        this.price = price;
    }

    /**
     * 컴퓨터의 가격을 돌려준다 —— {@link Asset#getPrice()} 의 구현.
     * <p>
     * {@link TangibleAsset} 가 추상 메서드 <b>시그니처만</b> 물려받은 상태였으므로,
     * 이 구현은 이 클래스가 처음으로 가격을 계산하는 지점이 된다.
     * <p>
     * {@code @Override} 는 필수가 아니지만 붙인다. 반환 타입을 실수로 {@code double} 로
     * 적는 등 시그니처가 어긋나면 여기서 컴파일 에러가 나기 때문이다.
     *
     * @return 컴퓨터의 가격
     */
    @Override
    public int getPrice() {
        return price;
    }
}
