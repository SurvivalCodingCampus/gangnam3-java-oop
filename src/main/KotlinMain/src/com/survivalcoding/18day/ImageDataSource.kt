package com.survivalcoding.`18day`

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// 1. [ImageDataSource 인터페이스]: 사진을 인터넷에서 다운받고 저장하는 기술적 세부사항 정의
interface ImageDataSource {
    // URL을 통해 인터넷에서 이미지를 다운로드하여 바이트 배열(ByteArray)로 반환
    suspend fun fetchImage(url: String): ByteArray

    // 바이트 배열 데이터를 받아 지정된 경로(path)에 파일로 저장
    suspend fun saveImage(bytes: ByteArray, path: String)
}

// 2. [ImageDataSourceImpl 구현체]: Ktor 클라이언트를 이용해 실제 네트워크 통신과 파일 저장을 수행
class ImageDataSourceImpl(
    private val client: HttpClient // 인터넷 통신을 위한 Ktor 클라이언트 주입
) : ImageDataSource {

    override suspend fun fetchImage(url: String): ByteArray {
        // 클라이언트를 통해 GET 요청을 보내고, 응답 데이터를 바이트 배열(ByteArray)로 변환하여 반환
        return client.get(url).bodyAsBytes()
    }

    override suspend fun saveImage(bytes: ByteArray, path: String) {
        // 파일 입출력은 디스크를 사용하는 무거운 작업이므로 IO 전용 일꾼(Dispatchers.IO)에게 시킴
        withContext(Dispatchers.IO) {
            File(path).writeBytes(bytes) // 받아온 바이트 배열을 지정된 경로의 파일로 씀
        }
    }
}

// 3. [ImageRepository 인터페이스]: 비즈니스 로직(이미지 저장, 중복 확인 등)을 위한 창구 정의
interface ImageRepository {
    // URL에서 이미지를 다운로드하여 지정된 경로에 저장
    suspend fun saveImage(url: String, path: String)

    // 여러 URL의 이미지들을 지정된 디렉토리에 순차적으로 저장
    suspend fun saveImages(urls: List<String>, directory: String)

    // 이미지가 존재하지 않는 경우에만 URL에서 다운로드하여 저장 (성공하면 true, 이미 있으면 false 반환)
    suspend fun saveImageIfNotExists(url: String, path: String): Boolean
}

// 4. [ImageRepositoryImpl 구현체]: DataSource를 주입받아 구체적인 비즈니스 규칙 처리
class ImageRepositoryImpl(
    private val imageDataSource: ImageDataSource // 데이터소스를 생성자로 주입받음
) : ImageRepository {

    override suspend fun saveImage(url: String, path: String) {
        // 1. DataSource를 통해 인터넷에서 이미지 데이터를 ByteArray로 다운로드
        val imageBytes = imageDataSource.fetchImage(url)
        // 2. 다운로드받은 데이터를 지정된 경로에 파일로 저장
        imageDataSource.saveImage(imageBytes, path)
    }

    override suspend fun saveImages(urls: List<String>, directory: String) {
        // 리스트에 있는 모든 URL을 하나씩 순회하며 이미지 다운로드 및 저장 수행
        urls.forEachIndexed { index, url ->
            val path = "$directory/image_$index.jpg" // 파일 이름 자동 생성
            saveImage(url, path)
        }
    }

    override suspend fun saveImageIfNotExists(url: String, path: String): Boolean {
        val file = File(path)
        // 만약 해당 경로에 파일이 이미 존재한다면 다운로드하지 않고 false 반환
        if (file.exists()) {
            return false
        }

        // 파일이 존재하지 않는 경우에만 다운로드 후 저장하고 true 반환
        val imageBytes = imageDataSource.fetchImage(url)
        imageDataSource.saveImage(imageBytes, path)
        return true
    }
}