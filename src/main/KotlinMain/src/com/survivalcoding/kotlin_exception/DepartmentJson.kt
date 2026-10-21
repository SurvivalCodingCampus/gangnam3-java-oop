package com.survivalcoding.kotlin_exception

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// 1. 직렬화가 가능하도록 @Serializable 이름표 붙이기!
@Serializable
data class Employee(val name: String, val age: Int)

@Serializable
data class Department(val name: String, val leader: Employee)

class DepartmentJson {

    @Test
    fun testDepartmentSerialization() {
        // 리더가 "홍길동(41세)"인 "총무부" Department 인스턴스 생성
        val employee = Employee("홍길동", 41)
        val department = Department("총무부", employee)

        // 해당 객체를 json으로 직렬화 (클래스 ➡️ JSON 문자열)
        val departmentJson = Json.encodeToString(department)

        // 결과 확인용 출력
        println("직렬화된 JSON: $departmentJson")

        // 기대하는 JSON 문자열과 일치하는지 확인
        val expectedJson = "{\"name\":\"총무부\",\"leader\":{\"name\":\"홍길동\",\"age\":41}}"
        assertEquals(expectedJson, departmentJson)
    }
}