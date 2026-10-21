package com.survivalcoding.day13

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Employee(
    var name: String,
    var age: Int
)

@Serializable
data class Department(
    var name: String,
    var leader: Employee
)

fun main() {
    val department: Department = Department(
        name = "총무부",
        leader = Employee(
            name = "홍길동",
            age = 41
        )
    )

    // 직렬화
    val json: String = Json.encodeToString(department)
    println(json)

    // 저장
    val file: File = File("company.json")
    file.writeText(json)

    // 다시 읽어
    val loadJson: String = file.readText()

    // 역직렬화
    val loadUser: Department = Json.decodeFromString<Department>(loadJson)
    println(loadUser)

    println(department == loadUser)
    println(department === loadUser)

}