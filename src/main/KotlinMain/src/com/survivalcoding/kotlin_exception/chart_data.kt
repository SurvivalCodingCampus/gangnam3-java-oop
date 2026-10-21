package com.survivalcoding.kotlin_exception

import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    // 1. 다운로드한 JSON 파일 경로 지정
    val file = File("chart_data.json")

    // 2. 안전하게 파일 열기 및 내용 읽기 (T.use 또는 readText 활용)
    val jsonString = file.readText()

    // 3. JSON 문자열을 코틀린 데이터 클래스로 역직렬화 (JSON -> 객체)
    val chartResponse: ChartResponse = Json.decodeFromString(jsonString)

    // 4. 역직렬화된 데이터 확인 및 활용
    println("불러온 데이터 개수: ${chartResponse.chartData.size}")
    chartResponse.chartData.forEach { item ->
        println("날짜: ${item.date}, 값: ${item.value}")
    }
}