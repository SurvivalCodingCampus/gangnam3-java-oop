@file:Suppress("NonAsciiCharacters", "ClassName")

package day13

import com.survivalcoding.day13.file.FileCopier
import com.survivalcoding.day13.file.FileOperations
import com.survivalcoding.day13.json.Department
import com.survivalcoding.day13.json.Employee
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class Day13ExamTest {
    companion object {
        // OS마다 경로 구분자가 다를 수 있음 Path를 사용하면 OS 맞게 구분자를 넣어줌
        private val TestFolderPath = Path("src", "test", "resources", "FileTestFolder")

        @BeforeAll
        @JvmStatic
        fun init() {
            if (TestFolderPath.notExists()) {
                TestFolderPath.createDirectories()
            }
        }
    }

    @Nested
    inner class `파일 복사 테스트` {
        private val myCopier: FileOperations = FileCopier()
        private val srcPath = TestFolderPath / "src.txt"
        private val destPath = TestFolderPath / "dest.txt"
        private val srcFile = srcPath.toFile()
        private val destFile = destPath.toFile()

        // 테스트 메서드가 끝나면 지움
        @AfterEach
        fun tearDown() {
            srcPath.deleteIfExists()
            destPath.deleteIfExists()
        }

        @Test
        fun `원본 파일이 존재하지 않으면 예외가 발생한다`() {
            assertThatThrownBy { myCopier.copyFile(srcFile, destFile) }
                .isInstanceOf(FileNotFoundException::class.java)
        }

        @Test
        fun `원본 파일이 존재하면 복사한다`() {

            srcPath.writeText("안녕")

            val srcText = srcPath.readText()

            myCopier.copyFile(srcFile, destFile)

            val destText = destPath.readText()

            assertThat(srcText).isEqualTo(destText)
        }
    }

    @Nested
    inner class `JSON 변환, 저장 테스트` {

        private val companyPath = TestFolderPath / "company.json"

        private val expectedDepartment = Department(
            name = "총무부",
            leader = Employee(name = "홍길동", age = 41)
        )

        // 테스트 메서드가 끝나면 지움
        @AfterEach
        fun tearDown() {
            companyPath.deleteIfExists()
        }

        @Test
        fun `직렬화를 통해 JSON으로 저장한다`() {
            val jsonText = Json.encodeToString(expectedDepartment)

            companyPath.writeText(jsonText)
            val readText = companyPath.readText()

            assertThat(readText).isEqualTo(jsonText)
        }

        @Test
        fun `역직렬화를 통해 클래스로 변환이 가능하다`() {
            val jsonText = Json.encodeToString(expectedDepartment)
            companyPath.writeText(jsonText)

            val readText = companyPath.readText()
            val actualDepartment = Json.decodeFromString<Department>(readText)

            assertThat(actualDepartment).isEqualTo(expectedDepartment)
        }

        @Test
        fun `Non-null 타입일 때 JSON에 null이 있으면 역직렬화에 실패한다`() {
            val jsonText = """
                {
                    "name": "총무부",
                    "leader": null
                }
            """.trimIndent()

            companyPath.writeText(jsonText)
            val readText = companyPath.readText()

            assertThatThrownBy { Json.decodeFromString<Department>(readText) }
                .isInstanceOf(kotlinx.serialization.SerializationException::class.java)
        }
    }
}