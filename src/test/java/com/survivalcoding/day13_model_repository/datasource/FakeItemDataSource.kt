package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Item

class FakeItemDataSource : ItemDataSource {

    private var items: List<Item> = emptyList()

    override fun loadItems(): List<Item> {
        return items
    }

    override fun saveItems(items: List<Item>) {
        this.items = items
    }
}