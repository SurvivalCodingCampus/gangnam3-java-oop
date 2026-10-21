package com.survivalcoding.model

@Serializable
data class Item(val id: Long, val name: String, val price : Int
)
interface ItemDataSource {
    fun getItem(id: Long): Item?
    fun getItems(): List<Item>

}
