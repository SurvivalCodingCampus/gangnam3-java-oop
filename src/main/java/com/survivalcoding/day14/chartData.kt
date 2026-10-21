package com.survivalcoding.day14

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class CollectionSalePrice{
    val price: Double,
    val cvtDatetime: String
}

@Serializable
data class CollectionChartData(
    val colletionName: String,
    val collectionSalePrice: List<CollectionSalePrice>? = null
    )
)

@Serializable
data class ChartData(
    val collectionChartDataList: List<CollectionChartData>
)

fun main() {
    val file: File = File("chart_data.json")
    val text: String = file.readText()

    val chartData: ChartData = Json.decodeFromString<ChartData>(text)
}