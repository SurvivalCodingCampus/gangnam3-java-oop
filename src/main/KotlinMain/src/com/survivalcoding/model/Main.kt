package com.survivalcoding.model

import com.survivalcoding.`15day_dataSource`.FileTodoDataSource
import com.survivalcoding.`15day_dataSource`.CsvStockDataSource
import com.survivalcoding.`15day_dataSource`.FileitemDataSource
import java.io.File

fun Main {
    val datasource = ItemDataSource = FileitemDataSource()

    val newItems = listOf(
        Item(id = 1L, name = "Item 1", price = 1000),
        Item(id = 2L, name = "Item 2", price = 2000),
    )
    dataSource.saveItems(newItems)

    val lines: List<String> = File("aa").readLines()
        .drop(1)

    val loadedItems : List<Item> = datasource.loadLi




}