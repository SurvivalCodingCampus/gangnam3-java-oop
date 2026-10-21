package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.StockListing
import com.survivalcoding.kotlin_datasource.model.Todo
import java.io.File
import kotlin.system.exitProcess

class CsvStockDataSource : StockDataSource {
	private val file = File("listing_status.csv")
	
	override fun loadStockListings(): List<StockListing> {
		if (!file.exists()) {
			return emptyList()
		}
		
		val csvData = file.readLines()
			.drop(1)  // 인덱스가 아니라 지울 개수를 의미
			.filter { it.isNotBlank() }
			.map {
				val values = it.split(",")
//				println(values.size)
//				println(values.lastIndex)
//				exitProcess(0)
				
				StockListing(
					symbol = values[0].trim(),
					name = values[1].trim(),
					exchange = values[2].trim(),
					assetType = values[3].trim(),
					ipoDate = values[4].trim(),
					status = values[6].trim()
				)
			}
		
		return csvData
	}
	
	
}


fun main() {
	 val dataSource: StockDataSource = CsvStockDataSource()
	
	val stockList = dataSource.loadStockListings()
	
	println("총 로드된 상장 종목 수: ${stockList.size}개")
	println("\n=== 상위 5개 종목 샘플 ===")
	stockList.take(5).forEach { stock ->
		println("[${stock.exchange}] ${stock.symbol} - ${stock.name} (${stock.assetType})")
	}
}
