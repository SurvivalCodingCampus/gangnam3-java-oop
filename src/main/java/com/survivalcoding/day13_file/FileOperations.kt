package com.survivalcoding.day13_file

import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div

interface FileOperations {
    
    fun copyFile(source: File, destination: File)
}

class DefaultFileOperations : FileOperations {
    private val TestFolderPath = Path("src", "test", "resources", "FileTestFolder") / "" / ""
    override fun copyFile(source: File, destination: File) {
        source.inputStream().use { fis ->
            destination.outputStream().use { fos ->
                // fos.write(fis.readBytes())
                fis.copyTo(fos)
            }
        }

        val list1 = listOf("src", "test", "resources", "FileTestFolder")
        val list2 = listOf("src", "test", "resources", "FileTestFolder2")

        val list3 = list1 - list2
    }

    private fun isSameFile(source: File, destination: File): Boolean {
        if (source.canonicalFile == destination.canonicalFile) return true
        return source.exists() && destination.exists() && Files.isSameFile(source.toPath(), destination.toPath())
    }
}