package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.ItemDataSource
import com.survivalcoding.day13_model_repository.model.Item

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource
) : ItemRepository {

    override fun getItems(): List<Item> {
        return dataSource.loadItems().sortedBy { it.name }
    }

    override fun getItem(id: Long): Item? {
        return dataSource.loadItems().find { it.id == id }
    }

    override fun getItemsUnderPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems().filter { it.price <= maxPrice }
    }

    override fun searchByName(query: String): List<Item> {
        return dataSource.loadItems().filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override fun addItem(item: Item) {
        val currentItems = dataSource.loadItems().toMutableList()
        currentItems.add(item)
        dataSource.saveItems(currentItems)
    }

    override fun deleteItem(id: Long) {
        val currentItem = dataSource.loadItems().filterNot { it.id == id }
        dataSource.saveItems(currentItem)
    }
}