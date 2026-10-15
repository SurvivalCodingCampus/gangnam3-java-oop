package com.survivalcoding.day08_file

import java.io.File
import java.nio.file.Files

class DefaultFileOperations : FileOperations {

    override fun copyFile(source: File, destination: File) {
        // Case 1
        // val text = source.readText()
        // destination.writeText(text)

        // Case 2
        // source.copyTo(destination, overwrite = true)

        // Case 3
        require(!isSameFile(source, destination)) {
            "원본과 대상이 같은 파일입니다: ${source.path}"
        }

        source.inputStream().use { fis ->
            destination.outputStream().use { fos ->
                // fos.write(fis.readBytes())
                fis.copyTo(fos)
            }
        }
    }

    private fun isSameFile(source: File, destination: File): Boolean {
        if (source.canonicalFile == destination.canonicalFile) return true
        return source.exists() && destination.exists() && Files.isSameFile(source.toPath(), destination.toPath())
    }
}