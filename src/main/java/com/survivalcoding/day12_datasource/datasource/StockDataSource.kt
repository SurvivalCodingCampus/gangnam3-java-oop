package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.StockListing

interface StockDataSource {

    fun loadStockListings(): List<StockListing>
}