package com.survivalcoding.day12_abstract_interface.asset;

import java.util.ArrayList;
import java.util.List;

/**
 * 12장 연습문제 3, 4 의 결과를 실행해 보는 데모.
 *
 * <h2>이 데모가 보여줄 것 두 가지</h2>
 * <ol>
 *     <li><b>extends 축(Asset)</b> — "자산이다"만으로 한 리스트에 모을 수 있다.
 *         {@link Computer}, {@link Book}, {@link Patent} 이 서로 다른 계층인데도
 *         {@code List<Asset>} 에 함께 들어간다.</li>
 *     <li><b>implements 축(Thing)</b> — "무게를 잴 수 있다"만으로 또 다른 리스트에 모을 수 있다.
 *         이때 {@link Patent} 은 <b>아예 들어갈 수 없다.</b> 컴파일 타임에 막힌다.</li>
 * </ol>
 * <p>
 * 이 두 목록이 동시에 존재한다는 사실이 12장 전체의 요약이다.
 * <b>분류 기준(축)이 여러 개일 수 있고, 각각이 서로 독립적이라는 것.</b>
 *
 * @see Thing
 * @see TangibleAsset
 */
public class AssetDemo {

    /**
     * 데모 진입점.
     *
     * @param args 실행 인자. 사용하지 않는다.
     */
    static void main(String[] args) {
        // ==================== 준비 ====================
        Computer computer = new Computer("회사 노트북", 2.5, 1_500_000);
        Book book = new Book("클린 코드", 0.8, 464, 3_000);
        Patent patent = new Patent("화면 잠금 방식 특허", 30_000_000);

        // ==================== 축 1: extends (is-a) — "자산이다" ====================
        System.out.println("===== [축 1] Asset (extends) 으로 모으기 : '자산이다' =====");

        // 다른 계통(유형/무형)의 객체들을 한 리스트에 담는다.
        // List<Asset> 은 "자산이면서 Asset 의 하위 타입"인 것만 받아들이므로,
        // Object 나 String 은 컴파일 에러가 난다.
        List<Asset> assets = new ArrayList<>();
        assets.add(computer);
        assets.add(book);
        assets.add(patent);

        int totalPrice = 0;
        for (Asset asset : assets) {
            // 여기서는 asset 의 실제 타입을 몰라도 된다.
            // 컴파일 타임에 Asset 이 getPrice() 를 선언한다는 것이 보장되고,
            // 호출 시점에는 실제 객체(Computer/Book/Patent)의 구현이 실행된다.
            // ── 이것이 다형성(polymorphism) 다.
            System.out.println("  " + asset.getName() + " → 가격 " + asset.getPrice() + "원");
            totalPrice += asset.getPrice();
        }
        System.out.println("  총 자산 가격 : " + totalPrice + "원\n");

        // ==================== 축 2: implements (Can-Do) — "무게를 잴 수 있다" ====================
        System.out.println("===== [축 2] Thing (implements) 으로 모으기 : '무게가 있다' =====");

        List<Thing> things = new ArrayList<>();
        things.add(computer);
        things.add(book);

        // 아래 줄을 주석 해제하면 컴파일 에러가 난다.
        //   things.add(patent);
        // 이유: Patent 는 Thing 을 implements 하지 않으므로 List<Thing> 에 들어갈 수 없다.
        // 런타임에 걸러내는 게 아니라 컴파일 타임에 막힌다. 이것이 인터페이스 타입의 강점이다.

        double totalWeight = 0;
        for (Thing thing : things) {
            System.out.println("  " + thing.getWeight() + "kg");
            totalWeight += thing.getWeight();
        }
        System.out.println("  무게 총합 : " + totalWeight + "kg\n");

        // ==================== setter 데모 ====================
        System.out.println("===== [setter] 무게를 바꾸면 목록의 값도 따라 바뀐다 =====");

        book.setWeight(0.95);          // 실제 책의 무게를 나중에 측정해서 보정하는 상황

        System.out.println("  보정 전 무게 총합 : 3.3kg  (2.5 + 0.8)");
        System.out.println("  보정 후 책 무게   : " + book.getWeight() + "kg\n");

        totalWeight = 0;
        for (Thing thing : things) {
            totalWeight += thing.getWeight();
        }
        System.out.println("  보정 후 무게 총합 : " + totalWeight + "kg  (2.5 + 0.95)");
        System.out.println("  → setWeight 한 번이 객체 내부 상태를 바꾸므로");
        System.out.println("    같은 getWeight() 호출이 다른 값을 돌려준다.\n");

        // ==================== instanceof : 두 축의 소속을 동시에 확인 ====================
        System.out.println("===== [instanceof] 한 객체가 두 축에 동시에 속한다 =====");

        checkAxes("회사 노트북", computer);
        checkAxes("클린 코드", book);
        checkAxes("화면 잠금 방식 특허", patent);

        // ==================== 컴파일 에러 예시 (실행 안 함) ====================
        //
        // 1) 추상 클래스는 인스턴스화할 수 없다.
        //    Asset a = new Asset("이름");                 // ✗ 컴파일 에러
        //    TangibleAsset t = new TangibleAsset("이름", 1.0);  // ✗ 컴파일 에러
        //    //   error: TangibleAsset is abstract and does not override getPrice() in Asset
        //
        // 2) 인터페이스 타입으로는 그 인터페이스에 없는 메서드를 부를 수 없다.
        //    Thing thing = new Computer("노트북", 2.5, 1_500_000);
        //    thing.getPrice();                            // ✗ 컴파일 에러
        //    //   error: cannot find symbol: method getPrice()
        //
        //    (구체 타입으로 선언하면 된다)
        //    Computer realComputer = new Computer("노트북", 2.5, 1_500_000);
        //    realComputer.getPrice();                     // ◀ OK
    }

    /**
     * 한 객체가 {@code Asset} 축과 {@code Thing} 축에 각각 속하는지 출력한다.
     *
     * @param label 출력용 이름
     * @param obj   확인할 객체
     */
    private static void checkAxes(String label, Object obj) {
        // Asset 축(extends/is-a) 확인 — 계층이므로 조상 타입이면 모두 true
        boolean isAsset = obj instanceof Asset;
        boolean isTangible = obj instanceof TangibleAsset;
        // Thing 축(implements/Can-Do) 확인 — 독립 축이므로 별개로 판정된다
        boolean isThing = obj instanceof Thing;

        System.out.println("  " + label
                + " → Asset: " + isAsset
                + ", TangibleAsset: " + isTangible
                + ", Thing: " + isThing);
    }
}
