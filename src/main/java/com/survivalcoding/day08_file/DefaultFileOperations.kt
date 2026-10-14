package com.survivalcoding.day08_file

import java.io.File

class DefaultFileOperations : FileOperations {

    override fun copyFile(source: File, destination: File) {
        // val text = source.readText()
        // destination.writeText(text)

        // source.copyTo(destination, overwrite = true)

        source.inputStream().use { fis ->
            destination.outputStream().use { fos ->
                // fos.write(fis.readBytes())
                fis.copyTo(fos)
            }
        }
    }
}