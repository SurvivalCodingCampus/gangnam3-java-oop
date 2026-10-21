package com.survivalcoding.`15day_dataSource`

import com.survivalcoding.model.Item

class FileitemDataSource: ItemDataSource {
    private var items: List<Item> = listOf()

    override fun getItem(id: Long): Item? {
        return items.find { item -> item.id == id }
    val jsonText = file.readText()
    if (jsonText.isBlank()) {
        return emptyList()
    }
        return Json.decodeFromString<List<Item>>(jsonText)
    }
    override fun saveItems(items: List<Item>) {
        val jsonText = Json.encodeToString(items)
        file.writeText(jsonText)

    }

}