package com.survivalcoding.day15.data_source

import com.survivalcoding.day15.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}