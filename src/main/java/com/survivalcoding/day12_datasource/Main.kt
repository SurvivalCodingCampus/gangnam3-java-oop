package com.survivalcoding.day12_datasource

import com.survivalcoding.day12_datasource.datasource.*
import com.survivalcoding.day12_datasource.model.Item
import com.survivalcoding.day12_datasource.model.Todo


fun main() {
    val itemDataSource: ItemDataSource = FileItemDataSource()
    val newItems = listOf(
        Item(id = 1L, name = "기계식 키보드", price = 120_000),
        Item(id = 2L, name = "무선 마우스", price = 89_000),
        Item(id = 3L, name = "27인치 모니터", price = 350_000),
    )

    itemDataSource.saveItems(newItems)

    val loadedItems = itemDataSource.loadItems()

    loadedItems.forEach { item ->
        println("- [ID: ${item.id}] ${item.name} (${item.price}원)")
    }

    // TODO: 학생 구현체 인스턴스 생성
    val toDataSource: TodoDataSource = FileTodoDataSource()

    // 1. 저장할 더미 Todo 데이터 (JSONPlaceholder 규격 샘플)
    val sampleTodos = listOf(
        Todo(userId = 1L, id = 1L, title = "파일 입출력 과제 제출하기", completed = false),
        Todo(userId = 1L, id = 2L, title = "JSON 직렬화 개념 복습", completed = true),
        Todo(userId = 1L, id = 3L, title = "DataSource 인터페이스 작성", completed = true)
    )

    // 2. 파일에 저장
    println("=== 데이터 저장 시도 ===")
    toDataSource.saveTodos(sampleTodos)
    println("todos.json 파일 저장이 완료되었습니다.\n")

    // 3. 파일에서 로드
    println("=== 저장된 데이터 불러오기 ===")
    val loadedTodos = toDataSource.loadTodos()

    loadedTodos.forEach { todo ->
        val status = if (todo.completed) "[완료]" else "[미완료]"
        println("$status ID: ${todo.id} | ${todo.title} (작성자: User ${todo.userId})")
    }

    // TODO: 학생 구현체 생성
    val stockDataSource: StockDataSource = CsvStockDataSource()

    val stockList = stockDataSource.loadStockListings()

    println("총 로드된 상장 종목 수: ${stockList.size}개")
    println("\n=== 상위 5개 종목 샘플 ===")
    stockList.take(5).forEach { stock ->
        println("[${stock.exchange}] ${stock.symbol} - ${stock.name} (${stock.assetType})")
    }
}