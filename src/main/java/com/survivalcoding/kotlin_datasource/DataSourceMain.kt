package com.survivalcoding.kotlin_datasource

import com.survivalcoding.kotlin_datasource.data_source.FileItemDataSource
import com.survivalcoding.kotlin_datasource.data_source.ItemDataSource
import com.survivalcoding.kotlin_datasource.model.Item


// main이 깔끔해짐. model, data_source 폴더 구분하면서.
fun main() {
	val dataSource: ItemDataSource = FileItemDataSource()  // 데이터 타입을 인터페이스로!
	// 구체화된 클래스보다 인터페이스를 사용해야 다형성을 활용하기 좋다
	// 클래스 타입을 쓰면 해당 클래스여야만 해! -> 인터페이스는 이런 결 하나만 있어도 돼~
	// 인터페이스가 있으면 이왕이면 인터페이스를 사용하라
	// 데이터 타입 추론을 하도록 :ItemDataSource 라고 인터페이스 타입으로 선언 안 하면
	// 데이터 타입을 클래스 FileItemDataSource로 자동 할당해서 다형성 활용 어려움
	
	val newItems = listOf<Item>(
		Item(1L, "Item 1", 1000),
		Item(2L, "Item 2", 2000),
	)
	
	dataSource.saveItems(newItems)
	
	val loadedItems = dataSource.loadItems()
	loadedItems.forEach {
		println(it)
	}
	
	// 이렇게 하면 데이터 저장소가 로컬/서버 등으로 바뀌거나
	// 저장 데이터 타입이 json에서 yaml로 바뀌거나 등의 문제가 생겼을 때
	// 디버깅 쉽게, 코드 쉽게 수정하려고 폴더 구분하고 이렇게 가는 거
	// main은 위 코드 그대로 놔두고 인터페이스도 그대로 두고
	// FileItemDataSource 클래스의 코드만 수정하면 되는 거지.
	// 인터페이스만 짜면 main 짜는 사람, DB 짜는 사람, FileItemDataSource 짜는 사람 구분해서 작업 가능
	// 분업할 때 룰 정하기 위함. 이렇게 데이터 주거니받거니 하는 게 있겠구나, 하고.
	
}