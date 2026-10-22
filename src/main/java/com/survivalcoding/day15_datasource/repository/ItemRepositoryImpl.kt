package com.survivalcoding.day15_datasource.repository

import com.survivalcoding.day15_datasource.data_source.ItemDataSource
import com.survivalcoding.day15_datasource.model.Item

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource,
): ItemRepository {
    override fun getItems(): List<Item> {
        return dataSource.loadItems().sortedBy { it.name }
    }

    override fun getItem(id: Long): Item? {
        return dataSource.loadItems().find { it.id == id }
    }

    override fun getItemsUnderPrice(price: Int): List<Item> {
        TODO("Not yet implemented")
    }

    override fun searchByName(name: String): List<Item> {
        TODO("Not yet implemented")
    }

    override fun searchByNameDesc(name: String): List<Item> {
        TODO("Not yet implemented")
    }

    override fun addItem(item: Item) {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: Item) {
        TODO("Not yet implemented")
    }

}