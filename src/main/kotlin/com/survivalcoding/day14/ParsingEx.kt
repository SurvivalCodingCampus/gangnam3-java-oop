package com.survivalcoding.day14

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.readText



@Serializable
data class CollectionChartData(
    val collectionChartDataList: List<CollectionChart>
)

@Serializable
data class CollectionChart(
    val collectionName: String,
    val collectionSalePrice: List<Collection> = emptyList()
)

@Serializable
data class Collection(
    val price: Double,
    val cvtDatetime: String
)

fun main() {
    val dataPath = Path("src/main/resources/chart_data.json")
    val dataStr = dataPath.readText()

    val collectionChartData
        = Json.decodeFromString<CollectionChartData>(dataStr)

    // Exception in thread "main" kotlinx.serialization.MissingFieldException: Field 'collectionSalePrice' is required
    // for type with serial name 'com.survivalcoding.day14.CollectionChart'
    // , but it was missing at path: $.collectionChartDataList[67]

    // collectionSalePrice 필드가 필수값(반드시 존재해야 함)으로 지정되어 있는데 들어온 JSON에 이 필드가 없다
    // 에러가 발생한 코틀린 데이터 클래스의 이름이 CollectionChart
    // collectionChartDataList 인덱스[67] 배열 요소의 collectionChartDataList 안쪽 데이터를 파싱(역직렬화)하던 중에 문제가 발생

    // JSON collectionSalePrice 필드가 없음
    // 기본값을 emptyList()를 통해 해결 nullable이 좋을까요?
    // val collectionSalePrice: List<Collection> = emptyList()
    // {
    //     "collectionName": "collection68"
    // },

    collectionChartData.collectionChartDataList.forEach(::println)
}