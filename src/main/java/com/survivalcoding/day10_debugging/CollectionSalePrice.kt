package com.survivalcoding.day10_debugging

import kotlinx.serialization.Serializable

@Serializable
data class CollectionSalePrice(
    val cvtDatetime: String,
    val price: Double
)