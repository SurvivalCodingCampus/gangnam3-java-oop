package com.survivalcoding.day13.json

import kotlinx.serialization.Serializable

@Serializable
data class Employee(val name: String, val age: Int)

@Serializable
data class Department(val name: String, val leader: Employee)