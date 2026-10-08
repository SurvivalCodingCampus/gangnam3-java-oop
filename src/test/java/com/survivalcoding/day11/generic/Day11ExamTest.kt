@file:Suppress("NonAsciiCharacters")

package com.survivalcoding.day11.generic

import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

class `11일차 테스트` {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `스트롱 박스 테스트` {
        @ParameterizedTest
        @EnumSource(KeyType::class)
        fun `시도횟수에 도달하지 않으면 null을 출력한다`(keytype: KeyType) {
            val item = "보물"
            val box = StrongBox(data = item, key = keytype)

            // 기본 반복 5
            val repeatCount = if (box._requiredCount < 5) {
                box._requiredCount
            } else {
                box._requiredCount - 5
            }

            box._count = box._requiredCount - repeatCount

            repeat(repeatCount) {
                assertThat(box.get()).isNull()
            }
        }

        @ParameterizedTest
        @MethodSource("provideStrongBoxTestData")
        fun <T> `시도횟수에 도달하면 data를 반환한다`(keyType: KeyType, item: T) {
            val box = StrongBox(data = item, key = keyType)
            box._count = box._requiredCount

            assertThat(box.get()).isEqualTo(item)
        }

        fun provideStrongBoxTestData(): List<Arguments> {
            return listOf(
                Arguments.of(KeyType.PADLOCK, "보물"),
                Arguments.of(KeyType.BUTTON, 10000),
                Arguments.of(KeyType.FINGER, 99.9)
            )
        }
    }

    @Nested
    inner class `문자열 연습문제 테스트` {

        @ParameterizedTest
        @ValueSource(strings = ["vOiㄷd", "pIㄷpp", "lo ll"])
        fun `영어가 아닌 문자열이 들어오면 생성 예외 발생`(str: String) {
            assertThatThrownBy { Word(str) }.hasMessage("영문자만 입력 가능")
        }

        @ParameterizedTest
        @ValueSource(strings = ["vOid", "pIpp", "loll"])
        fun `문자열에 모음이 있으면 isVowel은 true를 반환한다`(str: String) {
            val word = Word(str);

            assertThat(word.isVowel(1)).isTrue()
        }

        @ParameterizedTest
        @ValueSource(strings = ["bcdf", "pqrst", "skrt"])
        fun `문자열에 모음이 없으면 isVowel은 false를 반환한다`(str: String) {
            val word = Word(str)

            // 1번 인덱스 문자가 모음이 아니므로 false
            assertThat(word.isVowel(1)).isFalse()
        }

        @ParameterizedTest
        @ValueSource(strings = ["Ussi", "awsa", "Idso"])
        fun `문자열에 모음이 있으면 isConsonant은 true를 반환한다`(str: String) {
            val word = Word(str);

            assertThat(word.isConsonant(1)).isTrue()
        }

        @ParameterizedTest
        @ValueSource(strings = ["vOid", "pipp", "loll"])
        fun `문자열에 모음이 없으면 isConsonant은 false를 반환한다`(str: String) {
            val word = Word(str)

            // 1번 인덱스 문자가 모음(o, i, o)이므로 자음 검사는 false
            assertThat(word.isConsonant(1)).isFalse()
        }
    }
}

