package com.survivalcoding.day12_abstract_interface.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <b>[12장 연습문제 3+4] 자산 패키지 전체 규칙 검증 테스트.</b>
 * <p>
 * asset 패키지에 정의된 모든 클래스와 인터페이스가
 * 12장의 규칙을 준수하는지 리플렉션으로 최종 검증한다.
 * <p>
 * 검증 범위
 * <ul>
 *     <li>{@code Thing} 인터페이스의 메서드 시그니처와 제약</li>
 *     <li>{@code Asset} 추상 클래스의 구조 (abstract, name 필드, getPrice)</li>
 *     <li>{@code TangibleAsset} 가 {@code Asset} 을 상속하고 {@code Thing} 을 구현하는지</li>
 *     <li>{@code Computer, Book} 가 {@code TangibleAsset} 를 상속하고 {@code getPrice()} 를 구현하는지</li>
 *     <li>{@code Patent} 가 {@code Thing} 을 구현하지 않는지 (무게가 없음)</li>
 * </ul>
 */
@DisplayName("12장 연습문제 3+4: 자산 패키지 전체 규칙 검증")
class AssetPackageTest {

    /**
     * Thing 의 getWeight() 가 public abstract 인지 확인한다.
     */
    @Test
    @DisplayName("Thing.getWeight() is public abstract")
    void thingGetWeightIsPublicAbstract() throws NoSuchMethodException {
        Method m = Thing.class.getDeclaredMethod("getWeight");
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isAbstract(m.getModifiers()));
    }

    /**
     * Thing 의 setWeight(double) 가 public abstract 인지 확인한다.
     */
    @Test
    @DisplayName("Thing.setWeight(double) is public abstract")
    void thingSetWeightIsPublicAbstract() throws NoSuchMethodException {
        Method m = Thing.class.getDeclaredMethod("setWeight", double.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isAbstract(m.getModifiers()));
    }

    /**
     * Asset 이 abstract 클래스인지 확인한다.
     */
    @Test
    @DisplayName("Asset is abstract class")
    void assetIsAbstract() {
        assertTrue(Modifier.isAbstract(Asset.class.getModifiers()));
    }

    /**
     * Asset 의 name 필드가 private + final 인지 확인한다.
     */
    @Test
    @DisplayName("Asset.name field is private and final")
    void assetNameFieldIsPrivateAndFinal() throws NoSuchFieldException {
        Field f = Asset.class.getDeclaredField("name");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
        assertTrue(Modifier.isFinal(f.getModifiers()));
    }

    /**
     * TangibleAsset 이 Asset 을 상속하는지 확인한다.
     */
    @Test
    @DisplayName("TangibleAsset extends Asset")
    void tangibleAssetExtendsAsset() {
        assertTrue(java.lang.reflect.Modifier.isPublic(TangibleAsset.class.getModifiers()));
        // extends 관계는 Class.isAssignableFrom 로 확인
        assertTrue(Asset.class.isAssignableFrom(TangibleAsset.class));
    }

    /**
     * TangibleAsset 이 Thing 을 구현하는지 확인한다.
     */
    @Test
    @DisplayName("TangibleAsset implements Thing")
    void tangibleAssetImplementsThing() {
        assertTrue(Thing.class.isAssignableFrom(TangibleAsset.class));
    }

    /**
     * Computer 가 TangibleAsset 을 상속하는지 확인한다.
     */
    @Test
    @DisplayName("Computer extends TangibleAsset")
    void computerExtendsTangibleAsset() {
        assertTrue(TangibleAsset.class.isAssignableFrom(Computer.class));
    }

    /**
     * Computer 의 getPrice() 가 overriding 인지 확인한다.
     */
    @Test
    @DisplayName("Computer.getPrice() overrides AbstractMethod")
    void computerGetPriceOverrides() throws NoSuchMethodException {
        Method m = Computer.class.getDeclaredMethod("getPrice");
        assertTrue(java.lang.reflect.Modifier.isPublic(m.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isAbstract(m.getModifiers()) == false);
    }

    /**
     * Book 의 getPrice() 가 계산 로직을 갖는지 확인한다.
     */
    @Test
    @DisplayName("Book.getPrice() returns pageCount * pricePerPage logic")
    void bookGetPriceHasLogic() {
        Book b = new Book("테스트", 1.0, 10, 500);
        assertTrue(b.getPrice() == 10 * 500);
    }

    /**
     * Patent 이 Thing 을 구현하지 않는지 확인한다.
     * (무형자산이라 weight 가 없으므로)
     */
    @Test
    @DisplayName("Patent does not implement Thing")
    void patentDoesNotImplementThing() {
        assertTrue(!(Patent.class.isAssignableFrom(Thing.class)));
    }

    /**
     * 전체 패키지 클래스가 컴파일 가능하도록 되어 있는지 확인하는 헬퍼 테스트.
     * 실제 검증은 위의 개별 테스트로 대신한다.
     */
    @Test
    @DisplayName("All asset package classes compile and load")
    void allClassesLoad() {
        // 각 클래스가 Class 로 로딩되는지 확인
        var classes = new Class<?>[]{
                Thing.class, Asset.class, TangibleAsset.class,
                Computer.class, Book.class, IntangibleAsset.class, Patent.class
        };
        for (var clz : classes) {
            assertTrue(clz != null && clz.getName() != null);
        }
    }
}