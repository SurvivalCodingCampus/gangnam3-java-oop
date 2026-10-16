package com.survivalcoding.day13

import java.io.File
import java.io.FileNotFoundException

interface FileOperations {
    fun copyFile(source: File, destination: File)
}

class DefaultFileOperations : FileOperations {

    override fun copyFile(source: File, destination: File) {
        var text: String

        try {
            text = source.readText()
        } catch (_: FileNotFoundException) {
            throw FileNotFoundException("원본 파일이 없습니다.")
        }

        destination.writeText(text)
    }
}
