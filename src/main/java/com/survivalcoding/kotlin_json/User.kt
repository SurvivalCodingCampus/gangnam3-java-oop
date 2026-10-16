package com.survivalcoding.kotlin_json

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
		name = "John",
		email = "aaa@ccc.com",
	)
	
	// json 형태로 직렬화
	val json: String = Json.encodeToString(user)
	println(json)
	
	// 파일 저장하기
	val file = File("src/main/java/com/survivalcoding/kotlin_json/user.json")
	file.writeText(json)
	
	// 역직렬화 : json을 객체로 읽어오기
	val loadJson: String = file.readText()
	
	val loadUser: User = Json.decodeFromString<User>(loadJson)
	println(loadUser)  // User가 data class니까 출력됨. 그냥 class였으면 내용 출력 안됐다. toString으로 재정의 해줘야 함.
	
	println(user == loadUser)  // true
	println(user === loadUser)  // false
	
}