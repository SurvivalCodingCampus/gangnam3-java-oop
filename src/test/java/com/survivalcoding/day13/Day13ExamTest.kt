@file:Suppress("NonAsciiCharacters")

package com.survivalcoding.day13

import com.survivalcoding.day13.FileEx.FileOperations
import com.survivalcoding.day13.FileEx.MyFileCopier
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import java.io.FileNotFoundException


class Day13ExamTest {
    @Nested
    inner class `파일 복사 테스트` {

        @ParameterizedTest
        @ValueSource(strings = ["a.txt"])
        fun `원본 파일이 존재하지 않으면 예외가 발생한다`(src: String) {

            val myCopier: FileOperations = MyFileCopier()

            val srcFile = File(src)
            val destFile = File("b.txt")

            assertThatThrownBy { myCopier.copyFile(srcFile, destFile) }
                .isInstanceOf(FileNotFoundException::class.java)
        }
    }
}