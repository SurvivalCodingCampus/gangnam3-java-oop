package com.survivalcoding.day01_class_instance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 1장 - {@link Hero} 의 가장 초기 형태를 검증하는 테스트.
 * <p>
 * 1장 수업에서 {@code sleep()} 이 HP를 100 이 아닌 200 으로 만드는 연습을 했기 때문에,
 * 기대값이 200 인 점에 주의한다.
 * (뒤에 만들어진 {@code com.survivalcoding.Hero} 는 MAX_HP = 100 을 사용한다)
 */
class HeroTest {

    /**
     * 50 HP 로 시작해 sleep() 호출 후 200 이 되는지 확인한다.
     */
    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest2() {
        // given (준비)
        Hero hero = new Hero();
        hero.hp = 50;

        // when (실행)
        hero.sleep();

        // then (검증)
        assertEquals(200, hero.hp);
    }

    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest() {
        // given (준비)
        Hero hero = new Hero();
        hero.hp = 50;

        // when (실행)
        hero.sleep();

        // then (검증)
        assertEquals(200, hero.hp);
    }
}