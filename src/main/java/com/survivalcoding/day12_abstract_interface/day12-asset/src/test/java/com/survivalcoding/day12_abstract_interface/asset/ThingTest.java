package com.survivalcoding.day12_abstract_interface.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <b>[12장 연습문제 3] 인터페이스 Thing 규칙 검증 테스트.</b>
 * <p>
 * 인터페이스의 제약("모든 메서드가 추상", "필드를 가지지 않는다")은
 * <b>컴파일 타임에</b> 동작하므로 실행 중에는 확인할 수 없다.
 * 그 제약이 실제로 걸려 있는지 대신 <b>클래스 파일의 구조 자체</b>를 리플렉션으로 확인한다.
 * <p>
 * 검증 항목
 * <ul>
 *     <li>{@code Thing} 은 {@code interface} 다</li>
 *     <li>{@code Thing} 의 메서드는 {@code public abstract}</li>
 *     <li>{@code Thing} 인스턴스 필드가 없다 (선언된 필드는 전부 static)</li>
 * </ul>
 */
@DisplayName("12장 연습문제 3: 인터페이스 Thing 규칙 검증")
class ThingTest {

    /**
     * {@code Thing} 의 메서드가 {@code public abstract} 인지 확인한다.
     */
    @Test
    @DisplayName("getWeight() 메서드가 public abstract 다")
    void getWeightMethodIsPublicAbstract() throws NoSuchMethodException {
        Method getWeight = Thing.class.getDeclaredMethod("getWeight");

        assertTrue(Modifier.isPublic(getWeight.getModifiers()));
        assertTrue(Modifier.isAbstract(getWeight.getModifiers()));
    }

    @Test
    @DisplayName("setWeight 메서드가 public abstract 다")
    void setWeightMethodIsPublicAbstract() throws NoSuchMethodException {
        Method setWeight = Thing.class.getDeclaredMethod("setWeight", double.class);

        assertTrue(Modifier.isPublic(setWeight.getModifiers()));
        assertTrue(Modifier.isAbstract(setWeight.getModifiers()));
    }

    @Test
    @DisplayName("Thing 은 interface 다")
    void thingIsInterface() {
        assertTrue(Thing.class.isInterface());
    }

    @Test
    @DisplayName("인터페이스는 public + abstract 로 기록된다")
    void interfaceIsPublicAndAbstract() {
        int modifiers = Thing.class.getModifiers();

        assertTrue(Modifier.isPublic(modifiers));
        assertTrue(Modifier.isAbstract(modifiers));
    }

    @Test
    @DisplayName("인터페이스에는 인스턴스 필드가 없다")
    void interfaceHasNoInstanceFields() {
        // "인터페이스는 필드를 가지지 않는다" 는 규칙의 직접적인 검증이다.
        // 선언된 필드가 있더라도 전부 static 이어야 한다.
        for (java.lang.reflect.Field field : Thing.class.getDeclaredFields()) {
            assertTrue(
                    java.lang.reflect.Modifier.isStatic(field.getModifiers()),
                    "Thing 의 필드 " + field.getName() + " 가 static 이 아니다"
            );
        }
    }
}