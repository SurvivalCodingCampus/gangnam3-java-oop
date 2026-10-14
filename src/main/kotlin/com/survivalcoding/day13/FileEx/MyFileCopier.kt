package com.survivalcoding.day13.FileEx

import java.io.File

interface FileOperations {
    fun copyFile(source: File, destination: File)
}

class MyFileCopier: FileOperations {
    override fun copyFile(source: File, destination: File) {
        val srcText = source.readText()
        destination.writeText(srcText)
    }
}