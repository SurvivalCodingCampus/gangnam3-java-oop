package com.survivalcoding.day05_kotlin_basic

class Wizard : Character(), Attackable {
    
    override fun attack(slime: Slime) {
        println("공격")
    }
}