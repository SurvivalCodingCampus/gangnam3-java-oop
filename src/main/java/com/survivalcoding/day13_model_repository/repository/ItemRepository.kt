package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
    fun getItem(id: Long): Item?
    fun getItemsUnderPrice(maxPrice: Int): List<Item>
    fun searchByName(query: String): List<Item>
    fun addItem(item: Item)
    fun deleteItem(id: Long)
}