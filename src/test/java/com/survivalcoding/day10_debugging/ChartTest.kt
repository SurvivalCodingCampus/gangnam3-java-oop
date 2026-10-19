package com.survivalcoding.day10_debugging

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("Chart 직렬화 테스트")
class ChartTest {

    private lateinit var chart: Chart

    @BeforeEach
    fun setUp() {
        chart = Chart(
            listOf(
                CollectionChartData(
                    "collection1",
                    listOf(
                        CollectionSalePrice("2023-03-26T08:00:00", 58.25),
                        CollectionSalePrice("2023-03-26T08:00:10", 58.5),
                    )
                ),
                CollectionChartData("collection2"),
            )
        )
    }

    @Test
    fun `중첩된 JSON 문자열을 역직렬화하면 Chart 객체가 된다`() {
        // given
        val json = """
            {
              "collectionChartDataList": [
                {
                  "collectionName": "collection1",
                  "collectionSalePrice": [
                    { "price": 58.25, "cvtDatetime": "2023-03-26T08:00:00" }
                  ]
                }
              ]
            }
        """.trimIndent()

        // when
        val result = Json.decodeFromString<Chart>(json)

        // then
        val chartData = result.collectionChartDataList[0]
        assertEquals("collection1", chartData.collectionName)
        assertEquals("2023-03-26T08:00:00", chartData.collectionSalePrice!![0].cvtDatetime)
        assertEquals(58.25, chartData.collectionSalePrice[0].price)
    }

    @Test
    fun `collectionSalePrice가 없는 JSON을 역직렬화하면 collectionSalePrice는 null이다`() {
        // given
        val json = """{"collectionName":"collection75"}"""

        // when
        val result = Json.decodeFromString<CollectionChartData>(json)

        // then
        assertEquals("collection75", result.collectionName)
        assertNull(result.collectionSalePrice)
    }

    @Test
    fun `직렬화한 뒤 다시 역직렬화하면 원래 객체와 같다`() {
        // when
        val json = Json.encodeToString(chart)
        val result = Json.decodeFromString<Chart>(json)

        // then
        assertEquals(chart, result)
    }

    @Test
    fun `직렬화한 JSON을 파일에 쓰고 다시 읽어 역직렬화하면 원래 객체와 같다`(@TempDir tempDir: File) {
        // given
        val file = File(tempDir, "test_chart.json")
        val json = Json.encodeToString(chart)

        // when
        file.writeText(json)
        val result = Json.decodeFromString<Chart>(file.readText())

        // then
        assertTrue(file.exists())
        assertEquals(chart, result)
    }

    @Test
    fun `chart_data json 파일을 역직렬화하면 collectionSalePrice가 없는 컬렉션은 null이다`() {
        // given
        val file = ClassLoader.getSystemResourceAsStream("chart_data.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("파일을 찾지 못했습니다")

        // when
        val result = Json.decodeFromString<Chart>(file)

        // then
        assertEquals(75, result.collectionChartDataList.size)
        assertEquals(1, result.collectionChartDataList.count { it.collectionSalePrice == null })
    }
}
