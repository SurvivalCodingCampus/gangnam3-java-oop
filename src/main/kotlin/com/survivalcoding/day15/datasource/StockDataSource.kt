package com.survivalcoding.day15.datasource

import com.survivalcoding.day15.model.StockListing

interface StockDataSource {
    fun loadStockListings(): List<StockListing>
}