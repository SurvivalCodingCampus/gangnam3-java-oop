package com.survivalcoding.kotlin_json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Employee(
	var name: String,
	var age: Int,
)

@Serializable
data class Department(
	var name: String,
	var leader: Employee,
)

fun main() {
	// 0. 저장할 파일 경로
	val filePath = "src/main/java/com/survivalcoding/kotlin_json/company.json"
	
	// 1. 리더가 "홍길동(41세)"인 "총무부" Department 인스턴스 생성
	val employee = Employee("홍길동", 41)
	val department = Department("총무부", employee)
	
	// 2. 해당 객체를 json으로 직렬화해 저장
	val departmentJson = Json.encodeToString(department)
	val companyFile = File(filePath)
	
	companyFile.writeText(departmentJson)
	
	// 3. 저장된 파일을 다시 읽어 역직렬화 후 출력
	val companyText = companyFile.readText()
	val readJson = Json.decodeFromString<Department>(departmentJson)
	
	println(readJson)
}