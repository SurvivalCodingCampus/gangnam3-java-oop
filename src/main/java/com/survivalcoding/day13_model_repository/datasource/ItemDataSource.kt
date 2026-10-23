package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Item

interface ItemDataSource {

    fun loadItems(): List<Item>

    fun saveItems(items: List<Item>)
}