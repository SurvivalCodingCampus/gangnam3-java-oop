package com.survivalcoding.kotlin_datasource.model

data class StockListing(
	val symbol: String,     // 티커 심볼 ex. AAPL
	val name: String,       // 회사명 ex. Apple Inc
	val exchange: String,   // 거래소 ex. NASDAQ, NYSE
	val assetType: String,  // 자산 종류 ex. Stock, ETF
	val ipoDate: String,    // 상장일 ex. 1980-12-12
	val status: String,     // 상태 ex. Active
)
