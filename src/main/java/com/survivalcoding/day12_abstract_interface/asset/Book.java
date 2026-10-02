package com.survivalcoding.day12_abstract_interface.asset;

import java.util.Date;
import java.util.Objects;

// Book 클래스는 정렬 기준을 제공하기 위해 Comparable 인터페이스를 구현하고,
// 객체 복사 기능을 제공하기 위해 Cloneable 인터페이스를 구현합니다[cite: 15, 19].
public class Book implements Comparable<Book>, Cloneable{
    private String title;          // 책 제목
    private Date publishDate;      // 출간일 (참조 타입)
    private String comment;        // 책 코멘트(설명)

    // 생성자, getter, setter는 예시의 편의상 생략되어 있습니다.

    // 1) equals() 메서드 재정의: 제목과 출간일이 같으면 "같은 책"으로 판정합니다.
    @Override
    public boolean equals(Object o) {
        // 1-1. 자기 자신과 비교한 경우 참(true) 반환
        if (this == o) return true;

        // 1-2. 비교 대상이 Book 타입이 아니거나 null인 경우 거짓(false) 반환
        if (!(o instanceof Book book)) return false;

        // 1-3. 두 책의 제목과 출간일이 모두 일치하는지 비교하여 결과 반환
        return Objects.equals(title, book.title) &&
                Objects.equals(publishDate, book.publishDate);
    }

    // hashCode() 메서드 재정의: equals()를 재정의할 때는 짝꿍인 hashCode()도 반드시 함께 재정의해야 합니다.
    // 그래야 Set이나 Map 같은 컬렉션에서 데이터를 빠르고 정확하게 찾을 수 있습니다[cite: 7, 8].
    @Override
    public int hashCode() {
        return Objects.hash(title, publishDate);
    }

    // 2) Comparable 인터페이스 구현: Collections.sort()를 사용할 때 정렬 기준을 잡아줍니다[cite: 9, 10, 11, 19].
    @Override
    public int compareTo(Book other) {
        // Date 클래스에 이미 구현된 compareTo를 활용해 출간일을 서로 비교합니다.
        // 반환값이 음수면 앞의 책이 더 오래된 책, 양수면 더 나중(신상) 책이 됩니다[cite: 11].
        return this.publishDate.compareTo(other.publishDate);
    }

    // 3) clone() 메서드를 이용한 깊은 복사 구현
    @Override
    public Book clone() {
        try {
            // 3-1. super.clone()을 호출하여 기본 껍데기만 복제합니다 (얕은 복사)[cite: 15].
            Book result = (Book) super.clone();

            // 3-2. 참조 타입 필드인 publishDate는 주소만 복사하면 원본과 공유되므로 위험합니다[cite: 14, 18].
            // 따라서 새로운 Date 객체를 생성해서 대입하는 '깊은 복사'를 수행합니다.
            if (this.publishDate != null) {
                result.publishDate = (Date) this.publishDate.clone();
            }
            return result;
        } catch (CloneNotSupportedException e) {
            // Cloneable을 구현했기 때문에 발생할 일이 거의 없지만 문법상 필수로 처리해야 합니다.
            throw new AssertionError();
        }
    }
}

//package com.survivalcoding.day12_abstract_interface.asset;
//
///**
// * 책 —— {@link Computer} 와 함께 유형자산 계층을 구성하는 또 다른 구체 클래스.
// *
// * <h2>{@link Computer} 와 구조가 완전히 동일한 이유</h2>
// * 이 수업의 핵심은 여기 있다. {@code Book} 을 별도 클래스로 만들지 않고
// * {@link Computer} 를 상속했다면 이렇게 될 수도 있었다.
// * <pre>
// * // 잘못된 설계
// * public class Book extends Computer {
// *     public Book(String name, double weight, int price) {
// *         super(name, weight, price);   // 컴퓨터인데 책이 되는 모순
// *     }
// * }
// * </pre>
// * "책은 컴퓨터다"라는 말은 거짓이다. 둘은 <b>형제로서 나란히</b> 있어야 한다.
// * 공통된 성질(이름, 무게)은 <b>공통 조상</b>인 {@link TangibleAsset} 에 올려서 공유하고,
// * 물리적으로 달라야 하는 성질(가격 계산)만 각자 들면 된다.
// * <p>
// * 즉 이 두 클래스는 <b>중복이 아니라 "올바른 위치에 있는 중복"</b>이다.
// * 가격이 다르다는 사실은 두 클래스가 서로 다른 존재라는 뜻이지,
// * 두 클래스가 공통된 존재가 아니라는 뜻은 아니다.
// *
// * <h2>가격 계산이 실제로 다르도록 만든 예</h2>
// * 이 예제에서는 책 가격을 "페이지 수 × 페이지당 가격"으로 계산해 본다.
// * 그러면 {@code getPrice()} 의 본문이 {@link Computer} 와 실제로 달라지고,
// * <b>"같은 메서드 호출인데 객체마다 다른 결과"라는 다형성</b>이 눈으로 확인된다.
// *
// * @see Asset
// * @see TangibleAsset
// * @see Computer
// */
//public class Book extends TangibleAsset {
//
//    /** 이 책의 총 페이지 수. */
//    private final int pageCount;
//
//    /** 페이지 하나당 가격. */
//    private final int pricePerPage;
//
//    /**
//     * 이름, 무게, 페이지 수, 페이지당 가격을 받는 생성자.
//     * <p>
//     * {@code price} 를 직접 받는 대신 <b>페이지 수와 페이지당 가격</b>을 받는다는 점이 중요하다.
//     * 이렇게 해야 "책의 가격은 어떻게 정해지는가"라는 규칙이 코드 안에 남는다.
//     * 가격을 숫자로 그대로 받아 버리면 규칙이 사라지고 값만 남는다.
//     *
//     * @param name          책의 제목
//     * @param weight        책의 무게(kg)
//     * @param pageCount     총 페이지 수
//     * @param pricePerPage  페이지 하나당 가격
//     */
//    public Book(String name, double weight, int pageCount, int pricePerPage) {
//        super(name, weight);
//
//        this.pageCount = pageCount;
//        this.pricePerPage = pricePerPage;
//    }
//
//    /**
//     * 책의 가격을 돌려준다 —— {@link Asset#getPrice()} 의 구현.
//     * <p>
//     * {@link Computer#getPrice()} 와는 달리 <b>계산</b>을 한다.
//     * 두 메서드는 시그니처(명령, 매개변수, 반환 타입)가 같으므로
//     * {@code Asset} 타입의 변수 하나로 서로 다른 계산을 실행할 수 있다.
//     * <p>
//     * 이게 상속과 다형성이 실제로 얻는 이득이다. 호출하는 쪽은
//     * "책인가 컴퓨터인가"를 확인하지 않고 {@code asset.getPrice()} 라고만 부르면 된다.
//     *
//     * @return 이 책의 가격 (총 페이지 수 × 페이지당 가격)
//     */
//    @Override
//    public int getPrice() {
//        return pageCount * pricePerPage;
//    }
//
//    /**
//     * 총 페이지 수를 돌려준다.
//     *
//     * @return 이 책의 총 페이지 수
//     */
//    public int getPageCount() {
//        return pageCount;
//    }
//}
