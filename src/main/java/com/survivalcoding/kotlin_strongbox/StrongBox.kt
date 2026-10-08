package com.survivalcoding.kotlin_strongbox


enum class KeyType(val attemptCount: Int) {
	PADLOCK(1_024),
	BUTTON(10_000),
	DIAL(30_000),
	FINGER(1_000_000),
}


class StrongBox<E>(keyType: KeyType) {
	private var _item: E? = null
	private val keyType: KeyType = keyType
	private var attemptCount: Int = 0
	
	fun put(item: E) {
		_item = item
	}
	
	fun get(): E? = if (++attemptCount < keyType.attemptCount) null else _item
}