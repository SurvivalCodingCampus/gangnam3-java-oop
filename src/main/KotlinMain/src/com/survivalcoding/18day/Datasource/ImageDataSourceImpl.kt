package com.survivalcoding.`18day`.Datasource

import com.survivalcoding.day15_network.HttpClientFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class ImageDataSourceImpl(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : ImageDataSource {

    // override suspend fun fetchImage(url: String): ByteArray = withContext(Dispatchers.IO) {
    //     URI(url).toURL().openStream().use { it.readBytes() }
    // }

    override suspend fun fetchImage(url: String): ByteArray {
        val response = httpClient.get(url)

        if (!response.status.isSuccess()) {
            throw IOException("이미지 다운로드 실패: ${response.status.value} ($url)")
        }

        return response.body<ByteArray>()
    }

    override suspend fun saveImage(bytes: ByteArray, path: String) = withContext(Dispatchers.IO) {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeBytes(bytes)
    }
}