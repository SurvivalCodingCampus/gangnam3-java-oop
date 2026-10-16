package com.survivalcoding.day08_file

import java.io.File

interface FileOperations {
    
    fun copyFile(source: File, destination: File)
}