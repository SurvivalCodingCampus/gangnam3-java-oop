package com.survivalcoding.day08_file

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

@DisplayName("DefaultFileOperations 클래스 테스트")
class DefaultFileOperationsTest {

    private lateinit var fileOperations: FileOperations
    private lateinit var source: File
    private lateinit var destination: File

    @BeforeEach
    fun setUp() {
        fileOperations = DefaultFileOperations()
        source = File("test_source.txt")
        destination = File("test_destination.txt")
    }

    @AfterEach
    fun tearDown() {
        source.delete()
        destination.delete()
    }

    @Test
    fun `copyFile을 하면 원본 파일의 내용이 대상 파일로 복사된다`() {
        // given
        source.writeText("Hello, world!")

        // when
        fileOperations.copyFile(source, destination)

        // then
        assertTrue(destination.exists())
        assertEquals("Hello, world!", destination.readText())
    }

    @Test
    fun `copyFile을 하면 대상 파일의 기존 내용을 덮어쓴다`() {
        // given
        source.writeText("새로운 내용")
        destination.writeText("기존 내용")

        // when
        fileOperations.copyFile(source, destination)

        // then
        assertEquals("새로운 내용", destination.readText())
    }

    @Test
    fun `copyFile을 해도 원본 파일의 내용은 변하지 않는다`() {
        // given
        source.writeText("Hello, world!")

        // when
        fileOperations.copyFile(source, destination)

        // then
        assertEquals("Hello, world!", source.readText())
    }

    @Test
    fun `빈 파일을 copyFile하면 빈 대상 파일이 생성된다`() {
        // given
        source.writeText("")

        // when
        fileOperations.copyFile(source, destination)

        // then
        assertTrue(destination.exists())
        assertEquals("", destination.readText())
    }

    @Test
    fun `존재하지 않는 원본 파일을 copyFile하면 FileNotFoundException 예외를 던진다`() {
        // given
        val unknown = File("unknown.txt")

        // when & then
        assertThrows(FileNotFoundException::class.java) {
            fileOperations.copyFile(unknown, destination)
        }
    }

    @Test
    fun `존재하지 않는 원본 파일을 copyFile하면 대상 파일은 생성되지 않는다`() {
        // given
        val unknown = File("unknown.txt")

        // when
        runCatching { fileOperations.copyFile(unknown, destination) }

        // then
        assertFalse(destination.exists())
    }

    @Test
    fun `원본과 대상이 같은 파일을 copyFile하면 IllegalArgumentException 예외를 던진다`() {
        // given
        source.writeText("Hello, world!")
        destination = File("./test_source.txt")

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            fileOperations.copyFile(source, destination)
        }
    }

    @Test
    fun `원본과 대상이 같은 파일을 copyFile해도 원본 파일의 내용은 변하지 않는다`() {
        // given
        source.writeText("Hello, world!")
        destination = File("./test_source.txt")

        // when
        runCatching { fileOperations.copyFile(source, destination) }

        // then
        assertEquals("Hello, world!", source.readText())
    }
}
