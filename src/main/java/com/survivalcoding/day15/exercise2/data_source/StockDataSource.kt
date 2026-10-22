package com.survivalcoding.day15.exercise2.data_source

import com.survivalcoding.day15.exercise2.model.StockListing

internal interface StockDataSource {
    fun loadStockListings(): List<StockListing>
}