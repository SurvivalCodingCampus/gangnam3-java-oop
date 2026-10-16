package com.survivalcoding.kotlin_json

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DepartmentJsonTest {
	// 리더가 "홍길동(41세)"인 "총무부" Department 인스턴스 생성
	val employee = Employee("홍길동", 41)
	val department = Department("총무부", employee)
	
	// 해당 객체를 json으로 직렬화
	val departmentJson = Json.encodeToString(department)
	
	@Test
	fun `Department 객체가 json으로 저장 되었는지 확인`(@TempDir tempDir: File) {
		// 객체를 파일에 저장
		val companyFile = File(tempDir, "company.json")  // @TempDir = 테스트 실행 후 자동으로 삭제되는 임시 디렉토리
		companyFile.writeText(departmentJson)
		
		// 검증
		assertTrue(companyFile.exists())
	}
	
	@Test
	fun `저장된 내용 검증`(@TempDir tempDir: File) {
		val expectedJson = """{"name":"총무부","leader":{"name":"홍길동","age":41}}"""
		
		// 해당 객체를 json으로 직렬화해 저장
		val companyFile = File(tempDir, "company.json")
		companyFile.writeText(departmentJson)
		
		// 파일 읽기 및 역직렬화
		val companyText = companyFile.readText()
		val readDepartment = Json.decodeFromString<Department>(departmentJson)
		
		// 검증
		assertEquals(expectedJson, companyText)
	}
	
	@Test
	fun `읽어온 객체가 원본 객체와 동인한지 검증`(@TempDir tempDir: File) {
		// 해당 객체를 json으로 직렬화해 저장
		val companyFile = File(tempDir, "company.json")
		companyFile.writeText(departmentJson)
		
		// 파일 읽기 및 역직렬화
		val companyText = companyFile.readText()
		val readDepartment = Json.decodeFromString<Department>(departmentJson)
		
		// 검증
		assertEquals(department, readDepartment)
		assertEquals(department.name, readDepartment.name)
		assertEquals(department.leader.name, readDepartment.leader.name)
		assertEquals(department.leader.age, readDepartment.leader.age)
	}
}