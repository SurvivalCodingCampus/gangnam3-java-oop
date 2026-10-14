package com.survivalcoding.day12kotlin

data class AttackResult(
    val attackerName: String,
    val targetName: String,
    val damage: Int,
    val remainingHp: Int,
    val attackName: String,
)
