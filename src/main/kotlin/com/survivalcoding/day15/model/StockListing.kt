package com.survivalcoding.day15.model

import kotlinx.serialization.Serializable

@Serializable
data class StockListing(
    val symbol: String,
    val name: String,
    val exchange: String,
    val assetType: String,
    val ipoData: String,
    val delistingDate: String,
    val status: String,
)
