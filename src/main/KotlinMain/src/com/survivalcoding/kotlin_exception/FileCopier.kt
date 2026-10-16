package com.survivalcoding.kotlin_exception

import java.io.File

interface FileOperations {
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

fun copyFile(source: File, destination: File)