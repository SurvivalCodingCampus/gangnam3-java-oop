package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.ImageDataSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageDataSourceImpl(
    private val httpClient: HttpClient = HttpClient()
) : ImageDataSource {

    override suspend fun fetchImage(url: String): ByteArray {
        val response = httpClient.get(url)

        if (!response.status.isSuccess()) {
            throw java.io.IOException("이미지 다운로드 실패: ${response.status.value} ($url)")
        }

        return response.body<ByteArray>()
    }

    override suspend fun saveImage(bytes: ByteArray, path: String) = withContext(Dispatchers.IO) {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeBytes(bytes)
    }
}
