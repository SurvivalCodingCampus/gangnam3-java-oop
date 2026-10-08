package com.survivalcoding.kotlin_strongbox


enum class KeyType(val attemptCount: Int) {
	PADLOCK(1_024),
	BUTTON(10_000),
	DIAL(30_000),
	FINGER(1_000_000),
}


class StrongBox<E> {
	private var _data: E? = null
	
	fun put(data: E) {
		_data = data
	}
	
	fun get() = _data
}


fun main() {

}