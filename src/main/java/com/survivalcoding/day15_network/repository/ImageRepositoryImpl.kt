package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.ImageDataSource
import java.io.File
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ImageRepositoryImpl(
    private val dataSource: ImageDataSource
) : ImageRepository {

    override suspend fun saveImage(url: String, path: String) {
        try {
            val bytes = dataSource.fetchImage(url)
            dataSource.saveImage(bytes, path)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            println("네트워크 또는 파일 오류: $url ($e)")
        } catch (e: Exception) {
            println("알 수 없는 오류: $url ($e)")
        }
    }

    override suspend fun saveImages(urls: List<String>, directory: String) {
        urls.forEachIndexed { index, url ->
            saveImage(url, File(directory, "image_$index.png").path)
        }
    }

    override suspend fun saveImageIfNotExists(url: String, path: String): Boolean {
        if (File(path).exists()) return false

        return try {
            val bytes = dataSource.fetchImage(url)
            dataSource.saveImage(bytes, path)
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("이미지 저장 실패: $url ($e)")
            false
        }
    }
}