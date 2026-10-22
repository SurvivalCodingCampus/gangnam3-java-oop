package com.survivalcoding.day15_datasource.repository

import com.survivalcoding.day15_datasource.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
    fun getItem(id: Long): Item?
    fun getItemsUnderPrice(price: Int): List<Item>
    fun searchByName(name: String): List<Item>
    fun searchByNameDesc(name: String): List<Item>
    fun addItem(item: Item)
    fun deleteItem(item: Item)
}