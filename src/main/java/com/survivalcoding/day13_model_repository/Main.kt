package com.survivalcoding.day13_model_repository

import com.survivalcoding.day13_model_repository.datasource.FileItemDataSource
import com.survivalcoding.day13_model_repository.model.Item
import com.survivalcoding.day13_model_repository.repository.ItemRepository
import com.survivalcoding.day13_model_repository.repository.ItemRepositoryImpl

fun main() {
    val repository: ItemRepository = ItemRepositoryImpl(FileItemDataSource())

    repository.addItem(Item(id = 1L, name = "기계식 키보드", price = 120_000))
    repository.addItem(Item(id = 2L, name = "무선 마우스", price = 89_000))
    repository.addItem(Item(id = 3L, name = "27인치 모니터", price = 350_000))

    println("=== 전체 아이템 목록 (이름순) ===")
    
    val allItems = repository.getItems()

    allItems.forEach { item ->
        println("- [ID: ${item.id}}] ${item.name} (${item.price}원)")
    }

    println("\n=== 100,000원 이하 아이템 ===")

    val afforadbleItems = repository.getItemsUnderPrice(100_000)

    afforadbleItems.forEach { item ->
        println("- ${item.name}: ${item.price}원")
    }

    println("\n=== ID 2L 아이템 조회 ===")

    val item = repository.getItem(2L)

    println("조회 결과: $item")
}