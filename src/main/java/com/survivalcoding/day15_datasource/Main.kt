package com.survivalcoding.day15_datasource

import com.survivalcoding.day15_datasource.data_source.FileItemDataSource
import com.survivalcoding.day15_datasource.data_source.ItemDataSource
import com.survivalcoding.day15_datasource.model.Item
import com.survivalcoding.day15_datasource.repository.ItemRepository
import com.survivalcoding.day15_datasource.repository.ItemRepositoryImpl

fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()
    val repository: ItemRepository = ItemRepositoryImpl(dataSource)

    val newItems = listOf(
        Item(id = 1L, name = "Item 1", price = 1000),
        Item(id = 2L, name = "Item 2", price = 2000),
    )

    newItems.forEach {
        repository.addItem(it)
    }

    val loadedItems: List<Item> = repository.getItems()
    loadedItems.forEach(::println)
}