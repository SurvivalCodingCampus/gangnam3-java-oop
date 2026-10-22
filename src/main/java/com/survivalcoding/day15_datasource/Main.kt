package com.survivalcoding.day15_datasource

import com.survivalcoding.day15_datasource.data_source.FileItemDataSource
import com.survivalcoding.day15_datasource.data_source.ItemDataSource
import com.survivalcoding.day15_datasource.model.Item
import java.io.File

fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()

    val newItems = listOf(
        Item(id = 1L, name = "Item 1", price = 1000),
        Item(id = 2L, name = "Item 2", price = 2000),
    )

    dataSource.saveItems(newItems)

    val loadedItems: List<Item> = dataSource.loadItems()
    loadedItems.forEach(::println)
}