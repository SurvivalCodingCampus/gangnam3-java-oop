package com.survivalcoding.`18day`.Datasource


interface ImageDataSource {

    suspend fun fetchImage(url: String): ByteArray
    suspend fun saveImage(bytes: ByteArray, path: String)
}