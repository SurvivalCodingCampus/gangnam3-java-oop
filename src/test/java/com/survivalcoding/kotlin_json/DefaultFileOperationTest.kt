package com.survivalcoding.kotlin_json

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException

class DefaultFileOperationTest {
	private val testString = "안녕하세요"
	private val fileOperation = DefaultFileOperation()
	
	@Test
	fun `파일 복사 정상 작동`(@TempDir tempDir: File) {
		// @TempDir = 테스트 실행 후 자동으로 삭제되는 임시 디렉토리
		val source = File(tempDir, "source.txt")
		source.writeText(testString)
		
		val destination = File(tempDir, "destination.txt")
		
		fileOperation.copyFile(source, destination)
		
		assertTrue(destination.exists())
		assertEquals(testString, destination.readText())
	}
	
	@Test
	fun `존재하지 않는 파일 경로 입력 시 예외 발생`() {
		val fakeSource = File("fakePath.txt")
		val destination = File("destination.txt")
		
		assertThrows<FileNotFoundException> {
			fileOperation.copyFile(fakeSource, destination)
		}
	}
}