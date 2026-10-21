package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.StockListing

interface StockDataSource {
	fun loadStockListings(): List<StockListing>
}