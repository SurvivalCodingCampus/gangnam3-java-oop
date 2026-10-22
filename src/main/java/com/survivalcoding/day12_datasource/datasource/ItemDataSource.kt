package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item

interface ItemDataSource {
    
    fun loadItems(): List<Item>
    
    fun saveItems(items: List<Item>)
}