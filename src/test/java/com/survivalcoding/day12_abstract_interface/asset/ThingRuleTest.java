package com.survivalcoding.day12_abstract_interface.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [12장 연습문제 3] 인터페이스 Thing 규칙 검증.
 * 기존 InterfaceTest 와 동일한 패턴을 따른다.
 */
@DisplayName("12장 연습문제 3: Thing 인터페이스 규칙")
class ThingRuleTest {

    @Test
    @DisplayName("Thing 은 interface 다")
    void thingIsInterface() {
        assertTrue(Thing.class.isInterface());
    }

    @Test
    @DisplayName("인터페이스는 public + abstract 로 기록된다")
    void interfaceIsPublicAndAbstract() {
        int modifiers = Thing.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
        assertTrue(java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    @DisplayName("인터페이스의 메서드는 public abstract 다")
    void interfaceMethodsArePublicAbstract() throws NoSuchMethodException {
        Method m = Thing.class.getDeclaredMethod("getWeight");
        assertTrue(java.lang.reflect.Modifier.isPublic(m.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isAbstract(m.getModifiers()));
    }

    @Test
    @DisplayName("인터페이스에는 인스턴스 필드가 없다")
    void interfaceHasNoInstanceFields() {
        for (java.lang.reflect.Field f : Thing.class.getDeclaredFields()) {
            assertTrue(java.lang.reflect.Modifier.isStatic(f.getModifiers()),
                    "Thing 의 필드 " + f.getName() + " 가 static 이 아니다");
        }
    }
}