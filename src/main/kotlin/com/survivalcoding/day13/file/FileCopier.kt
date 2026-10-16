package com.survivalcoding.day13.file

import java.io.File

interface FileOperations {
    fun copyFile(source: File, destination: File)
}

class FileCopier: FileOperations {
    override fun copyFile(source: File, destination: File) {

        source.inputStream().buffered().use { bis ->
            destination.outputStream().buffered().use { bos ->
                bis.copyTo(bos)
            }
        }
    }
}