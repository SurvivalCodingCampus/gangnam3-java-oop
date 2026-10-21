package com.survivalcoding.day15_datasource.data_source

import com.survivalcoding.day15_datasource.model.Item
import kotlinx.serialization.json.Json
import java.io.File

class FileItemDataSource: ItemDataSource {
    private val file = File("items.json")

    override fun loadItems(): List<Item> {
        if (!file.exists()) {
            return emptyList()
        }

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