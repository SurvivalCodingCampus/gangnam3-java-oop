package com.survivalcoding.day09_data_type

import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val str = "홍길동,한석봉,신사임당"
    println(str.split(","))

    val user = User(
        name = "Jane Doe",
        email = "aaa@ccc.com"
    )

    // 직렬화
    val json = Json.encodeToString(user)
    println(json)

    val file = File("user.json")
    file.writeText(json)

    // 역직렬화
    val `object` = Json.decodeFromString<User>(file.readText())
    println(`object`)

    // 연습문제
    val employee = Employee("홍길동", 41)
    val department = Department("총무부", employee)

    val departmentFile = File("company.json")
    val departmentJson = Json.encodeToString(department)

    departmentFile.writeText(departmentJson)

    val departmentObject = Json.decodeFromString<Department>(departmentFile.readText())
    println(departmentObject)
}