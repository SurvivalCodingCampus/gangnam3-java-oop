package com.survivalcoding.`18day`.Data_source


interface ImageDataSource {

    suspend fun fetchImage(url: String): ByteArray
    suspend fun saveImage(bytes: ByteArray, path: String)
}