package com.survivalcoding.kotlin_json

import java.io.File
import java.io.FileNotFoundException

interface FileOperations {
	fun copyFile(source: File, destination: File)
}

class DefaultFileOperation : FileOperations {
	override fun copyFile(source: File, destination: File) {
		if (!source.exists()) {
			throw FileNotFoundException()
		}
		
		val sourceFile = source.readText()
		
		destination.writeText(sourceFile)
		println("복사 완료")
	}
}