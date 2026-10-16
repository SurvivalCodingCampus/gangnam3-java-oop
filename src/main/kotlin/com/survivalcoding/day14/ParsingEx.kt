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
    val collectionSalePrice: List<Collection>
)

@Serializable
data class Collection(
    val price: Double,
    val cvtDatetime: String
)

fun main() {
    val dataPath = Path(
        "src",
        "main",
        "resources"
    ) / "chart_data.json"
    val dataStr = dataPath.readText()

    val collectionChartData
        = Json.decodeFromString<CollectionChartData>(dataStr)
}