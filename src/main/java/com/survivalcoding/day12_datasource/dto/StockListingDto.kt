package com.survivalcoding.day12_datasource.dto

import com.survivalcoding.day12_datasource.model.StockListing

data class StockListingDto(
    val symbol: String,
    val name: String,
    val exchange: String,
    val assetType: String,
    val ipoDate: String,
    val delistingDate: String? = null,
    val status: String,
)

fun StockListingDto.toEntity(): StockListing {
    return StockListing(
        symbol = symbol,
        name = name,
        exchange = exchange,
        assetType = assetType,
        ipoDate = ipoDate,
        status = status,
    )
}

fun List<String>.toDto(): StockListingDto {
    return StockListingDto(
        symbol = getOrElse(0) { "" },
        name = getOrElse(1) { "" },
        exchange = getOrElse(2) { "" },
        assetType = getOrElse(3) { "" },
        ipoDate = getOrElse(4) { "" },
        delistingDate = getOrNull(5)?.takeIf { it.isNotBlank() && it != "null" },
        status = getOrElse(6) { "" },
    )
}
