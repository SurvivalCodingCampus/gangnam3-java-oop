package com.survivalcoding.day13.FileEx

import java.io.File

interface FileOperations {
    fun copyFile(source: File, destination: File)
}

class FileCopier: FileOperations {
    override fun copyFile(source: File, destination: File) {
        source.bufferedReader().use { reader ->
            destination.bufferedWriter().use { writer ->
                reader.copyTo(writer)
            }
        }
    }
}