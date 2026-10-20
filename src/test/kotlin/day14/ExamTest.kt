@file:Suppress("NonAsciiCharacters", "ClassName")

package day14

import com.survivalcoding.day14.CollectionChartData
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class ExamTest {
    companion object {
        private val testFolderPath = Path("src/test/resources/FileTestFolder")

        private val testJsonPath = testFolderPath / "test_chart_data.json"
        private val sourceJsonPath = testFolderPath / "chart_data.json"

        @BeforeAll
        @JvmStatic
        fun init() {
            if (testFolderPath.notExists()) {
                testFolderPath.createDirectories()
            }
        }
    }

    @AfterEach
    fun tearDown() {
        testJsonPath.deleteIfExists()
    }

    @Test
    fun `JSON 직렬화 및 역직렬화 테스트`() {
        val originJsonText = sourceJsonPath.readText()
        val expectedChartData = Json.decodeFromString<CollectionChartData>(originJsonText)

        testJsonPath.writeText(Json.encodeToString(expectedChartData))

        val actualChartDataText = testJsonPath.readText()
        val actualChartData = Json.decodeFromString<CollectionChartData>(actualChartDataText)

        assertThat(actualChartData).isEqualTo(expectedChartData)
    }
}