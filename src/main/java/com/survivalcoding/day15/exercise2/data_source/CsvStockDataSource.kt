package com.survivalcoding.day15.exercise2.data_source

import com.survivalcoding.day15.exercise2.model.StockListing
import java.io.File

class CsvStockDataSource : StockDataSource {

    private val file = File("listing_status.csv")

    override fun loadStockListings(): List<StockListing> {
        if (!file.exists()) {
            return emptyList()
        }

        // Text->Lines List<String>로 넘어옴
        val csvText = file.readLines()

        if (csvText.isBlank()) {
            return emptyList()
        }


        //val lines: List<String> = File("").readLines()
        //    .drop(1)

        val lines = csvText.split("\n")
            .drop(1)
            .filter { it.isNotBlank() }

// .map { val values: List<String> = it.split(",")
        // List는 읽기 mutableListOf는 읽고 쓰기
        val result = mutableListOf<StockListing>()

        for (line in lines) {
            val parts = line.split(",")

            val stock = StockListing(
                symbol = parts[0].trim(),
                //.trim() 생략 가능
                name = parts[1].trim(),
                exchange = parts[2].trim(),
                assetType = parts[3].trim(),
                ipoDate = parts[4].trim(),
                status = parts[6].trim(),
            )

            result.add(stock)
        }

        return result
    }
}

// 루트 폴더가 아닌 resource 폴더에 있는 파일을 가져오고 싶을 경우 사용하는 코드
//val file = ClassLoader.getSystemResourceAsStream("chart_data.json")
//    ?.bufferedReader()
//    ?.use { it.readText() }
//    ?: error("파일을 찾지 못했습니다")