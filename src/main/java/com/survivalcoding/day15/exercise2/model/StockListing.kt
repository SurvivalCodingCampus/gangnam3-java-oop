package com.survivalcoding.day15.exercise2.model

import kotlinx.serialization.Serializable

@Serializable
data class StockListing(
    val symbol: String,        // 티커 심볼 (예: AAPL)
    val name: String,          // 회사명 (예: Apple Inc)
    val exchange: String,      // 거래소 (예: NASDAQ, NYSE)
    val assetType: String,     // 자산 종류 (예: Stock, ETF)
    val ipoDate: String,       // 상장일 (예: 1980-12-12)
    val status: String         // 상태 (예: Active)
)