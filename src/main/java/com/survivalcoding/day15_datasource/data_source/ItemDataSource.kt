package com.survivalcoding.day15_datasource.data_source

import com.survivalcoding.day15_datasource.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}