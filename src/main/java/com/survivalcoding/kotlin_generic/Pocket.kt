package com.survivalcoding.kotlin_generic

class Pocket {
//	 Any는 Object와 유사한 것. 코틀린에선 Any가 Object처럼 쓰임.
	private var _data: Any? = null  // Any랑 Any?랑 다른 타입으로 취급한다
	
	fun put(data: Any) {
		_data = data
	}
	
	fun get() : Any? = _data  // 함수도 한 줄이면 =로 줄이기 가능
}


class PocketGeneric<E> {  // <E: Book> 이런 식으로 Book을 상속받은 것만으로 지정도 가능
	private var _data: E? = null
	
	fun put(data: E) {
		_data = data
	}
	
	fun get() : E? = _data
}


fun main() {
	val pocket = Pocket()
	pocket.put("ddd")
	pocket.put(1111)
	
	val data = pocket.get()
	println(data)  // .toString()이 있기 때문에 Any 타입이어도 출력됐다
	
	val pocketGeneric = PocketGeneric<Int>()  // 제네릭으로 타입 설정
	pocketGeneric.put(123)
	val dataGeneric = pocketGeneric.get()  // dataGeneric은 <Int>로 인식됨
}