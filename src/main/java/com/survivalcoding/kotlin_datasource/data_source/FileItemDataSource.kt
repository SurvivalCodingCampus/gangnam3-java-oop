package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Item
import kotlinx.serialization.json.Json
import java.io.File

// 구현체 코드 : 인터페이스를 구현
class FileItemDataSource: ItemDataSource {
	private val file = File("items.json")
	
	override fun loadItems(): List<Item> {
		if (!file.exists()) {
			return emptyList()  // 공백 리스트 생성해서 반환
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