package com.survivalcoding.day10_debugging

import kotlinx.serialization.Serializable

@Serializable
data class CollectionChartData(
    val collectionName: String,
    val collectionSalePrice: List<CollectionSalePrice>? = null
)