package com.survivalcoding.kotlin_basic

fun main() {
	println("Hello World")
	
	val hero = Hero("hello", "")
	
	val i = minOf(1, 2)
	
//	val h = Hero(hobby = "da")
	
	val name = "kij"
	println(name.subSequence(0,1))
	
	val name2 = "kij"
	println(name == name2)
	
	val name3 = "KiJ"
	println(name == name3)
	println(name.lowercase() == name3.lowercase())
	
	println("")
	
	println(name === name2)
	
	println("kij"[0])
}

class Hero (
	val name: String,
	var hobby: String,
)

open class Person() {

}

interface Moveable {
	fun move()
}

class Human : Person(), Moveable {
	override fun move() {
		TODO("Not yet implemented")
	}
	
}
