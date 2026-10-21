package com.survivalcoding.day15.datasourceImpl

import com.survivalcoding.day15.datasource.StockDataSource
import com.survivalcoding.day15.model.StockListing
import kotlin.io.path.Path
import kotlin.io.path.readText

class CsvStockDataSource : StockDataSource {
    private val path = Path("src/main/resources/listing_status.csv")

    override fun loadStockListings(): List<StockListing> {
        val csv = path.readText()

        val rows: List<String> = csv.split('\n')

        val stockList = mutableListOf<StockListing>()

        for (row in rows.drop(1).dropLast(1)) {
            val fields = row.split(',')

            stockList.add(
                StockListing(
                    symbol = fields[0],
                    name = fields[1],
                    exchange = fields[2],
                    assetType = fields[3],
                    ipoData = fields[4],
                    status = fields[5],
                )
            )
        }

        return stockList
    }
}