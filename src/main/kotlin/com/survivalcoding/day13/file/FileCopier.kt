package com.survivalcoding.day13.file

import java.io.File

interface FileOperations {
    fun copyFile(source: File, destination: File)
}

class FileCopier: FileOperations {
    override fun copyFile(source: File, destination: File) {
        source.bufferedReader().use { br ->
            destination.bufferedWriter().use { bw ->
                br.copyTo(bw)
            }
        }
    }
}