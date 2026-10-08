package com.survivalcoding.day06_generic

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals

@DisplayName("StrongBox 클래스 테스트")
class StrongBoxTest {

    private lateinit var strongBox: StrongBox<KeyType>

    @BeforeEach
    fun setUp() {
        strongBox = StrongBox<KeyType>(KeyType.PADLOCK)
    }

    @Test
    fun `금고는 키 타입의 정해진 사용횟수에 도달하면 열린다`() {
        // given
        val limit = KeyType.PADLOCK.limit // 1024

        // when
        repeat(limit) { _ ->
            strongBox.get()
        }

        // then
        assertEquals(limit, strongBox.count)
        assertNotNull(strongBox.get())
    }

    @Test
    fun `금고는 키 타입의 정해진 사용횟수에 도달하지 못하면 null을 반환한다`() {
        // given
        val limit = KeyType.PADLOCK.limit // 1024

        // when
        repeat(limit.minus(2)) { _ ->
            strongBox.get()
        }

        // then
        assertNotEquals(limit, strongBox.count)
        assertNull(strongBox.get())
    }

    @Test
    fun `금고의 키 타입을 새로 정의하면 사용횟수는 0으로 초기화된다`() {
        // given
        val limit = KeyType.PADLOCK.limit // 1024

        // when
        repeat(limit) { _ ->
            strongBox.get()
        }

        strongBox.put(KeyType.BUTTON)

        // then
        assertEquals(0, strongBox.count)
    }
}