# Repository 템플릿 (`Item` 예시)

`dayXX`, `Item`, URL을 실제 값으로 바꿔서 사용. import는 생략했으니 프로젝트 공통 타입은 SKILL.md의 전체 경로로 import.

## 모델 — `model/Item.kt`
```kotlin
data class Item(
    val id: Long,
    val name: String,
    val price: Int,
)
```

## DTO — `dto/ItemDto.kt`
```kotlin
@Serializable
data class ItemDto(
    val id: Long?,
    val name: String?,
    @SerialName("item_price")
    val price: Int?,
)
```

## Mapper — `mapper/ItemMapper.kt`
```kotlin
fun ItemDto.toModel(): Item = Item(
    id = id ?: 0L,
    name = name.orEmpty(),
    price = price ?: 0,
)
```

## DataSource — `datasource/inter/ItemDataSource.kt`
```kotlin
package com.survivalcoding.dayXX.datasource.inter

interface ItemDataSource {
    suspend fun getItemById(id: Long): MyResponse<ItemDto>
}
```

## DataSource 구현체 — `datasource/impl/RemoteItemDataSource.kt`
```kotlin
package com.survivalcoding.dayXX.datasource.impl

class RemoteItemDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create(),
    private val baseUrl: String = "https://api.example.com/items",
) : ItemDataSource {
    override suspend fun getItemById(id: Long): MyResponse<ItemDto> {
        val response = httpClient.get("$baseUrl/$id")
        val statusCode = StatusCode.from(response.status.value)

        return MyResponse(
            statusCode = statusCode,
            headers = response.headers.toMap(),
            body = if (statusCode.isSuccess()) response.body<ItemDto>() else null,
        )
    }
}
```

## Repository — `repository/inter/ItemRepository.kt`
```kotlin
package com.survivalcoding.dayXX.repository.inter

interface ItemRepository {
    suspend fun getItemById(id: Long): Result<Item, NetworkError>
}
```

## Repository 구현체 — `repository/impl/ItemRepositoryImpl.kt`
```kotlin
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource,
) : ItemRepository {
    override suspend fun getItemById(id: Long): Result<Item, NetworkError> {
        return try {
            val response = dataSource.getItemById(id)

            when {
                response.statusCode.isServerError() -> Result.Error(NetworkError.SERVER_ERR)
                response.statusCode.isClientError() -> Result.Error(NetworkError.CLIENT_ERR)
                else -> response.body
                    ?.let { Result.Success(it.toModel()) }
                    ?: Result.Error(NetworkError.NULL_DATA_ERR)
            }
        } catch (_: SocketException) {
            Result.Error(NetworkError.NO_INTERNET_ERR)
        } catch (_: HttpRequestTimeoutException) {
            Result.Error(NetworkError.TIMEOUT_ERR)
        } catch (_: TimeoutCancellationException) {
            Result.Error(NetworkError.TIMEOUT_ERR)
        } catch (_: Exception) {
            Result.Error(NetworkError.UNKNOWN_ERR)
        }
    }
}
```

## 테스트 — `src/test/kotlin/dayXX/ItemRepositoryTest.kt`
```kotlin
class ItemRepositoryTest {
    private val itemJson = """
        { "id": 1, "name": "노트북", "item_price": 1500000 }
    """.trimIndent()

    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    // MockEngine을 넣은 HttpClient — 실제 HttpClientFactory와 같은 플러그인 설정
    private fun mockClient(engine: MockEngine, requestTimeoutMillis: Long = 10_000) = HttpClient(engine) {
        install(HttpTimeout) { this.requestTimeoutMillis = requestTimeoutMillis }
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    private fun repositoryWith(handler: MockRequestHandler) = ItemRepositoryImpl(
        RemoteItemDataSource(mockClient(MockEngine(handler)))
    )

    @Test
    fun `200 응답이면 Success로 Model을 반환한다`() = runTest {
        val repository = repositoryWith { request ->
            assertThat(request.url.toString()).isEqualTo("https://api.example.com/items/1")
            respond(content = itemJson, status = HttpStatusCode.OK, headers = jsonHeaders)
        }

        val result = repository.getItemById(1L)

        assertThat(result).isEqualTo(Result.Success(Item(id = 1L, name = "노트북", price = 1_500_000)))
    }

    @Test
    fun `응답 필드가 null이면 기본값으로 매핑한다`() = runTest {
        val repository = repositoryWith {
            respond(content = """{ "id": 1 }""", status = HttpStatusCode.OK, headers = jsonHeaders)
        }

        val result = repository.getItemById(1L)

        assertThat(result).isEqualTo(Result.Success(Item(id = 1L, name = "", price = 0)))
    }

    @Test
    fun `4xx 응답이면 CLIENT_ERR을 반환한다`() = runTest {
        val repository = repositoryWith { respondError(HttpStatusCode.NotFound) }

        assertThat(repository.getItemById(999L)).isEqualTo(Result.Error(NetworkError.CLIENT_ERR))
    }

    @Test
    fun `5xx 응답이면 SERVER_ERR을 반환한다`() = runTest {
        val repository = repositoryWith { respondError(HttpStatusCode.InternalServerError) }

        assertThat(repository.getItemById(1L)).isEqualTo(Result.Error(NetworkError.SERVER_ERR))
    }

    @Test
    fun `네트워크가 끊기면 NO_INTERNET_ERR을 반환한다`() = runTest {
        val repository = repositoryWith { throw SocketException("network unreachable") }

        assertThat(repository.getItemById(1L)).isEqualTo(Result.Error(NetworkError.NO_INTERNET_ERR))
    }

    @Test
    fun `응답이 타임아웃보다 늦으면 TIMEOUT_ERR을 반환한다`() = runTest {
        // 엔진 dispatcher를 테스트 스케줄러에 연결해야 가상 시간으로 동작함
        val engine = MockEngine.create {
            dispatcher = StandardTestDispatcher(testScheduler)
            addHandler {
                delay(11.seconds)
                respond(content = itemJson, status = HttpStatusCode.OK, headers = jsonHeaders)
            }
        }
        val repository = ItemRepositoryImpl(
            RemoteItemDataSource(mockClient(engine, requestTimeoutMillis = 10_000))
        )

        assertThat(repository.getItemById(1L)).isEqualTo(Result.Error(NetworkError.TIMEOUT_ERR))
    }

    @Test
    fun `그 외 예외가 발생하면 UNKNOWN_ERR을 반환한다`() = runTest {
        val repository = repositoryWith { throw IllegalStateException("unexpected") }

        assertThat(repository.getItemById(1L)).isEqualTo(Result.Error(NetworkError.UNKNOWN_ERR))
    }
}
```
