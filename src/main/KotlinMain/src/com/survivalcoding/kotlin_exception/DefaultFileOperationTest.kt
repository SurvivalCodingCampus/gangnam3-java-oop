package com.survivalcoding.kotlin_exception
import java.io.File
import java.io.FileNotFoundException
import java.io.FileInputStream
import java.io.FileOutputStream

class DefaultFileOperations : FileOperations {
    override fun copyFile(source: File, destination: File) {
        // 원본 파일이 존재하지 않는 경우 FileNotFoundException 발생
        if (!source.exists()) {
            throw FileNotFoundException("원본 파일이 존재하지 않습니다: ${source.absolutePath}")
        }

        // 스트림을 이용해 원본 파일의 내용을 읽어 대상 파일에 쓰기
        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }
            }
        }
    }
}