package com.survivalcoding.day13_file

fun main() {
	val numString = "10.5"
	var num = 0

	try {
		num = numString.toInt()
	} catch (e: Exception) {
		num = 0
	}
	println(num)
}