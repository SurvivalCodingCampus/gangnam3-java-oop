package com.survivalcoding.day15.exercise2

import com.survivalcoding.day15.exercise2.data_source.CsvStockDataSource
import com.survivalcoding.day15.exercise2.data_source.StockDataSource

fun main() {
    // 구현체 인스턴스 생성
    val dataSource: StockDataSource = CsvStockDataSource()

    // 로드
    val stockList = dataSource.loadStockListings()

    println("총 로드된 상장 종목 수: ${stockList.size}개")
    println("\n=== 상위 5개 종목 샘플 ===")
    stockList.take(5).forEach { stock ->
        println("[${stock.exchange}] ${stock.symbol} - ${stock.name} (${stock.assetType})")
    }
}