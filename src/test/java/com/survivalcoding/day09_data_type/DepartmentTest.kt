package com.survivalcoding.day09_data_type

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File

@DisplayName("Department 직렬화 테스트")
class DepartmentTest {

    private lateinit var department: Department
    private lateinit var file: File

    @BeforeEach
    fun setUp() {
        department = Department("총무부", Employee("홍길동", 41))
        file = File("test_company.json")
    }

    @AfterEach
    fun tearDown() {
        file.delete()
    }

    @Test
    fun `Department를 직렬화하면 leader가 중첩된 JSON 문자열이 된다`() {
        // when
        val json = Json.encodeToString(department)

        // then
        assertEquals("""{"name":"총무부","leader":{"name":"홍길동","age":41}}""", json)
    }

    @Test
    fun `JSON 문자열을 역직렬화하면 Department 객체가 된다`() {
        // given
        val json = """{"name":"총무부","leader":{"name":"홍길동","age":41}}"""

        // when
        val result = Json.decodeFromString<Department>(json)

        // then
        assertEquals("총무부", result.name)
        assertEquals("홍길동", result.leader.name)
        assertEquals(41, result.leader.age)
    }

    @Test
    fun `직렬화한 뒤 다시 역직렬화하면 원래 객체와 같다`() {
        // when
        val json = Json.encodeToString(department)
        val result = Json.decodeFromString<Department>(json)

        // then
        assertEquals(department, result)
    }

    @Test
    fun `직렬화한 JSON을 파일에 쓰고 다시 읽어 역직렬화하면 원래 객체와 같다`() {
        // given
        val json = Json.encodeToString(department)

        // when
        file.writeText(json)
        val result = Json.decodeFromString<Department>(file.readText())

        // then
        assertTrue(file.exists())
        assertEquals(department, result)
    }
}
