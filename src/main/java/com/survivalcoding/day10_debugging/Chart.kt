package com.survivalcoding.day10_debugging

import kotlinx.serialization.Serializable

@Serializable
data class Chart(
    val collectionChartDataList: List<CollectionChartData>
)