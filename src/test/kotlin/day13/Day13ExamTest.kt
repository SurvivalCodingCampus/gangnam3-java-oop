@file:Suppress("NonAsciiCharacters")

package day13

import com.survivalcoding.day13.FileEx.FileOperations
import com.survivalcoding.day13.FileEx.FileCopier
import com.survivalcoding.day13.JsonEx.Department
import com.survivalcoding.day13.JsonEx.Employee
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException


class Day13ExamTest {

    private val saveFolder = "src/test/resources/FileTestFolder/"

    @Nested
    inner class `파일 복사 테스트` {
        private val myCopier: FileOperations = FileCopier()
        private val srcFile = File(saveFolder + "src.txt")
        private val destFile = File(saveFolder + "dest.txt")

        // 테스트 메서드가 끝나면 지움
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

        private val companyFile = File(saveFolder + "company.json")

        private val expectedDepartment = Department(
            name = "총무부",
            leader = Employee(name = "홍길동", age = 41)
        )

        // 테스트 메서드가 끝나면 지움
        @AfterEach
        fun tearDown() {

            if (companyFile.exists()) {
                companyFile.delete()
            }
        }

        @Test
        fun `직렬화를 통해 JSON으로 저장한다`() {

            val jsonText = Json.encodeToString(expectedDepartment)

            companyFile.writeText(jsonText)
            val readText = companyFile.readText()

            assertThat(readText).isEqualTo(jsonText)
        }

        @Test
        fun `역직렬화를 통해 클래스로 변환이 가능하다`() {

            val jsonText = Json.encodeToString(expectedDepartment)
            companyFile.writeText(jsonText)

            val readText = companyFile.readText()
            val actualDepartment = Json.decodeFromString<Department>(readText)

            assertThat(actualDepartment).isEqualTo(expectedDepartment)
        }
    }
}