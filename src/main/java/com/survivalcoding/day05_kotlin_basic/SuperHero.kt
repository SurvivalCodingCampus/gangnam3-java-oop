package com.survivalcoding.day05_kotlin_basic

class SuperHero(name: String, nickname: String, hp: Int) : Hero(name, nickname, hp) {

    override fun attack() {
        super.attack()
        println("추가 공격")
    }

    override fun fly() {
        super.fly()
        println("추가 비행")
    }
}