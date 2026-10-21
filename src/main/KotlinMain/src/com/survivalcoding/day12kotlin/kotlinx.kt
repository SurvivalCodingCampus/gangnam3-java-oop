package com.survivalcoding.day12kotlin

package com.survivalcoding.kotlin_debug

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

// 1. JSON 안의 낱개 데이터 하나를 담아줄 데이터 클래스
@Serializable
data class ChartItem(
    val date: String, // 날짜 정보를 담을 변수
    val value: Int    // 수치/값 정보를 담을 변수
)

// 2. 전체 JSON 구조(리스트 형태)를 통째로 담아줄 대형 상자 클래스
@Serializable
data class ChartResponse(
    val chartData: List<ChartItem> // ChartItem들이 여러 개 들어있는 목록
)

fun main() {
    // step 1. 다운로드받은 json 파일을 자바의 File 객체로 불러옵니다.
    // (실제 경로에 파일이 존재해야 합니다)
    val file = File("chart_data.json")

    // step 2. 파일이 실제로 존재하는지 확인하고 안전하게 읽어옵니다.
    if (!file.exists()) {
        println("앗! 파일이 존재하지 않습니다. 경로를 확인해주세요.")
        return
    }

    // 파일의 모든 내용을 글자(문자열) 통째로 읽어오기
    val jsonString = file.readText()

    // step 3. 역직렬화(Deserialization): JSON 문자열을 코틀린 객체 그릇(ChartResponse)으로 짠! 하고 변환합니다.
    val chartResponse: ChartResponse = Json.decodeFromString(jsonString)

    // step 4. 잘 담겼는지 확인하기 위해 출력해봅니다.
    println("--- 성공적으로 불러온 차트 데이터 ---")
    println("총 데이터 개수: ${chartResponse.chartData.size}개")

    // 반복문을 돌며 하나씩 출력
    for (item in chartResponse.chartData) {
        println("날짜: ${item.date}, 값: ${item.value}")
    }
}