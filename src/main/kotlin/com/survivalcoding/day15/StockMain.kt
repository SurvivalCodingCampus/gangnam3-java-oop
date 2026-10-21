package com.survivalcoding.day15

import com.survivalcoding.day15.datasource.StockDataSource
import com.survivalcoding.day15.datasourceImpl.CsvStockDataSource

fun main() {
    val dataSource: StockDataSource = CsvStockDataSource()

    dataSource
        .loadStockListings()
        .take(5)
        .forEach(::println)
}