package com.survivalcoding.day15_network.datasource

interface ImageDataSource {

    suspend fun fetchImage(url: String): ByteArray
    suspend fun saveImage(bytes: ByteArray, path: String)
}