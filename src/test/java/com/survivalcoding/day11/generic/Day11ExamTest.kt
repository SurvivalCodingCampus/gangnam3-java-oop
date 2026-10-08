@file:Suppress("NonAsciiCharacters")

package com.survivalcoding.day11.generic

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

import org.assertj.core.api.Assertions.*

class `11일차 테스트` {
    @Nested
    inner class `스트롱 박스 테스트` {
        @ParameterizedTest
        @EnumSource(KeyType::class)
        fun `시도횟수에 도달하지 않으면 null을 출력한다`(keytype: KeyType) {
            val item = "보물"
            val box = StrongBox(data = item, key = keytype)

            val repeatCount = 5
            box._count = maxOf(0, box._requiredCount - 5)

            repeat(repeatCount) {
                assertThat(box.get()).isNull()
            }
        }

        @ParameterizedTest
        @EnumSource(KeyType::class)
        fun `시도횟수에 도달하면 않으면 data를 출력한다`(keytype: KeyType) {
            val item = "보물"
            val box = StrongBox(data = item, key = keytype)

            box._count = box._requiredCount

            assertThat(box.get()).isEqualTo(item)
        }
    }
}