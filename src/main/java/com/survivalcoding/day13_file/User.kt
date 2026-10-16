package com.survivalcoding.day13_file

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class User(
    val name: String,
    val email: String,
)

fun main() {
    val user = User(
        name = "John Doe",
        email = "aaa@ccc.com"
    )

    // 직렬화
    val json: String = Json.encodeToString(user)
    println(json)

    // 저장
    val file = File("user.json")
    file.writeText(json)

    // 다시 읽어
    val loadJson: String = file.readText()

    // 역직렬화
    val loadUser: User = Json.decodeFromString<User>(loadJson)
    println(loadUser)

    println(user == loadUser)   // true
    println(user === loadUser)   // false

}