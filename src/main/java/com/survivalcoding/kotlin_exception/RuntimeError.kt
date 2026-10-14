package com.survivalcoding.kotlin_exception

fun main() {
	val numString = "10.5"
	
	val num = try {
		numString.toInt()
	} catch (e: NumberFormatException) {
		0
	}
	
	println(num)
}