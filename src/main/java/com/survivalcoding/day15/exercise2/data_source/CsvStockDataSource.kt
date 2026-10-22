package com.survivalcoding.day15.exercise2.data_source

import java.io.File

class CsvStockDataSource : StockDataSource {

    private val file = File("listing_status.csv")

    override fun loadStockListings(): List<StockListing> {
        if (!file.exists()) {
            return emptyList()
        }

        val csvText = file.readText()
        if (csvText.isBlank()) {
            return emptyList()
        }
        val lines = csvText.split("\n")
            .drop(1)
            .filter { it.isNotBlank() }

        return emptyList()
    }
}

// 루트 폴더가 아닌 resource 폴더에 있는 파일을 가져오고 싶을 경우 사용하는 코드
//val file = ClassLoader.getSystemResourceAsStream("chart_data.json")
//    ?.bufferedReader()
//    ?.use { it.readText() }
//    ?: error("파일을 찾지 못했습니다")