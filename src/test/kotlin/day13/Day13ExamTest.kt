@file:Suppress("NonAsciiCharacters")

package day13

import com.survivalcoding.day13.FileEx.FileOperations
import com.survivalcoding.day13.FileEx.FileCopier
import com.survivalcoding.day13.JsonEx.Department
import com.survivalcoding.day13.JsonEx.Employee
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
import java.io.FileNotFoundException


class Day13ExamTest {

    @Nested
    inner class `파일 복사 테스트` {

        private val saveFolder = "src/test/resources/FileTestFolder/"
        private val myCopier: FileOperations = FileCopier()
        private val srcFile = File(saveFolder + "src.txt")
        private val destFile = File(saveFolder + "dest.txt")

        // 테스트가 끝나면 지움
        @AfterEach
        fun tearDown() {

            if (srcFile.exists()) {
                srcFile.delete()
            }

            if (destFile.exists()) {
                destFile.delete()
            }
        }

        @Test
        fun `원본 파일이 존재하지 않으면 예외가 발생한다`() {

            assertThatThrownBy { myCopier.copyFile(srcFile, destFile) }
                .isInstanceOf(FileNotFoundException::class.java)
        }

        @Test
        fun `원본 파일이 존재하면 복사한다`() {

            srcFile.writeText("안녕")

            val srcText = srcFile.readText()

            myCopier.copyFile(srcFile, destFile)

            val destText = destFile.readText()

            assertThat(srcText).isEqualTo(destText)
        }
    }

    @Nested
    inner class `JSON 변환, 저장 테스트` {

//        @ParameterizedTest
//        @CsvSource(
//            // 형식: "JSON문자열", "기대하는 부서명", "기대하는 리더이름", "기대하는 리더나이"
//            value = [
//                "\"{'name':'총무부','leader':{'name':'홍길동','age':41}}\", 총무부, 홍길동, 41",
//                "\"{'name':'인사부','leader':{'name':'김철수','age':35}}\", 인사부, 김철수, 35"
//            ]
//        )
    }

    @Nested
    inner class `부서 사원 정보 직렬화, 저장 테스트` {
        val employee = Employee(name = "홍길동", age = 41)
        val department = Department(name = "총무부", leader = employee)
    }
}