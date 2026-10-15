package com.survivalcoding.kotlin_exception

import kotlinx.serialization.Serializable

val kotlinx: Any
    get() {
        TODO()
    }

@Serializable
data class Employee(val name: String, val age: Int)

@Serializable
data class Department(val name: String, val leader: Employee)