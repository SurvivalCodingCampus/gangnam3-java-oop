package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.dto.toDto
import com.survivalcoding.day12_datasource.dto.toEntity
import com.survivalcoding.day12_datasource.model.StockListing
import kotlinx.serialization.json.Json
import java.io.File

class CsvStockDataSource : StockDataSource {

    private val file = File("listing_status.csv")

    override fun loadStockListings(): List<StockListing> {
        if (!file.exists()) {
            return emptyList()
        }

        val stockList = file.readLines()
            .drop(1)
            .filter { it.isNotBlank() }
            .map {
                val lines = it.split(",")

                // Case 1
                // val data = """
                //     { 
                //         "symbol": "${lines[0]}",
                //         "name": "${lines[1]}", 
                //         "exchange": "${lines[2]}",
                //         "assetType": "${lines[3]}",
                //         "ipoDate": "${lines[4]}",
                //         "status": "${lines[6]}"
                //     }
                // """.trimIndent()
                // Json.decodeFromString<StockListing>(data)

                // Case 2
                // StockListing(
                //     symbol = lines[0],
                //     name = lines[1],
                //     exchange = lines[2],
                //     assetType = lines[3],
                //     ipoDate = lines[4],
                //     status = lines[6],
                // )

                // Case 3
                lines.toDto().toEntity()
            }

        return stockList
    }
}