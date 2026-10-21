package com.survivalcoding.day12_datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class StockListing(
    val symbol: String,
    val name: String,
    val exchange: String,
    val assetType: String,
    val ipoDate: String,
    val status: String,
)
