package com.survivalcoding.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// 1. 개별 가격 정보를 담는 클래스
@Serializable
data class SalePrice(
    val price: Double,
    val cvtDatetime: String
)

// 2. 컬렉션 정보를 담는 클래스
// (collectionSalePrice가 누락되었을 때를 대비해 기본값으로 빈 리스트를 지정합니다)
@Serializable
data class CollectionItem(
    val collectionName: String,
    val collectionSalePrice: List<SalePrice> = emptyList()
)

// 3. 전체 JSON 최상위 구조를 담는 클래스
@Serializable
data class CollectionChartResponse(
    val collectionChartDataList: List<CollectionItem>
)

fun main() {
    // 사용자가 제공한 JSON 문자열
    val jsonString = """
        {
          "collectionChartDataList": [
            // ... (생략된 데이터들)
            { "collectionName": "collection68" },
            // ...
          ]
        }
    """.trimIndent() // (실제 코드에서는 파일이나 API로 받은 전체 JSON 문자열을 넣으시면 됩니다)

    try {
        // JSON 문자열을 코틀린 객체로 역직렬화 (collection68의 누락된 리스트는 빈 리스트로 안전하게 처리됨)
        val response: CollectionChartResponse = Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)

        println("데이터 파싱 성공! 총 컬렉션 수: ${response.collectionChartDataList.size}")

        // 데이터 확인
        response.collectionChartDataList.forEach { item ->
            if (item.collectionSalePrice.isEmpty()) {
                println("[누락 데이터 발견] ${item.collectionName}: 가격 정보가 없습니다.")
            } else {
                println("${item.collectionName}: 가격 데이터 ${item.collectionSalePrice.size}개 존재")
            }
        }

    } catch (e: Exception) {
        println("JSON 파싱 중 에러 발생: ${e.message}")
    }
}