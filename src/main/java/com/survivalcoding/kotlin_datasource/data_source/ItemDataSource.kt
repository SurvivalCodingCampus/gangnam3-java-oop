package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Item

// 외부 소통용 인터페이스 : DB가 뭘 하는 건지 정의
interface ItemDataSource {
	fun loadItems(): List<Item>
	fun saveItems(items: List<Item>)
}
