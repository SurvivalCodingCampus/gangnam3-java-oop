# DataSource 작성 원칙

로컬 파일 실습의 `ItemDataSource` / `FileItemDataSource` 와 원격 API 실습(포켓몬)의 `PokemonDataSource` / `RemotePokemonDataSource` / `Response` 를 기준으로 정리한,
새로운 DataSource 를 만들 때 지켜야 할 필수 원칙이다.
DTO 쪽 규칙은 [dto.md](dto.md), DTO → 모델 변환 규칙은 [mapper.md](mapper.md), 그 위에 올라가는 Repository 쪽 규칙은 [repository.md](repository.md),
테스트 규칙은 [test.md](test.md) 를 따른다. 이 문서는 `datasource` 패키지 안의 파일만 다룬다.
코드 발췌는 원본 그대로이되, `package` · `import` 줄의 수업 날짜 패키지명만 `<dayXX>` 로 바꿔 적었다.

> **한 줄 요약**
> DataSource 는 저장 매체(파일 · DB · 네트워크)를 만지는 **유일한** 곳이다.
> 로컬은 **모델** 을, 원격은 **`Response<DTO>`** 를 돌려준다. 그 아래 기술은 전부 구현체 안에 숨긴다.

---

## 0. 두 가지 DataSource — 로컬과 원격

```
로컬 파일 : items.json ──File.readText──▶ Json.decode ──▶ List<Item> (모델)         ──▶ Repository
            └──────────── FileItemDataSource ────────────┘

원격 API  : 서버 ──httpClient.get──▶ bodyAsText ──▶ Json.decode ──▶ Response<PokemonDto> ──▶ Repository ──toModel()──▶ Pokemon
            └──────────────── RemotePokemonDataSource ─────────────────┘                        └─ mapper.md ─┘
```

| 구분                 | 로컬 파일 (`ItemDataSource`)        | 원격 API (`PokemonDataSource`)                              |
|----------------------|-------------------------------------|-------------------------------------------------------------|
| 메서드               | `fun loadItems()`, `fun saveItems()` | `suspend fun getPokemon(name)`                             |
| 돌려주는 것          | `List<Item>` — **모델**             | `Response<PokemonDto>` — **DTO 를 담은 응답 포장**          |
| 인터페이스 import    | `model.Item`                        | `dto.PokemonDto`                                            |
| 구현체 접두사        | `File`                              | `Remote`                                                    |
| 구현체가 받는 것     | `File` (생성자, 기본값 있음)        | `HttpClient` (생성자, 기본값 있음)                          |
| 기술 라이브러리      | `java.io.File`, `kotlinx.serialization` | Ktor `HttpClient`, `kotlinx.serialization`              |
| 실패하면             | (실습 코드는 파일이 있다고 가정)    | 예외를 그대로 던진다 → Repository 가 `catch` 해서 `null`   |
| 모델 변환은 누가     | DataSource (decode 결과가 곧 모델)  | Repository 가 `dto.toModel()` 호출 ([mapper.md](mapper.md)) |
| 테스트 더블          | `Fake<모델명>DataSource`            | Ktor `MockEngine` ([test.md](test.md))                      |

- 원격이 모델 대신 DTO 를 돌려주는 이유는 [dto.md 0장](dto.md) 에 있다. 요점은 "서버 응답 모양은 서버가 정하고, 모델 모양은 우리가 정한다. 그 둘을 붙이는 자리(Mapper)를 DataSource 밖으로 뺀다" 다.
- 선생님 주석: "데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자" — 통신 · 문자열 · 파싱은 전부 DataSource 가 끝내고, **밖으로는 DTO 만** 나간다.

---

## 1. 네이밍 규칙

### 인터페이스: `<모델명>DataSource`

- 모델 이름 뒤에 `DataSource` 를 붙인다. 접두사 (`I`)나 기술명은 붙이지 않는다.
- `ItemDataSource`, `TodoDataSource`, `StockDataSource`, `PokemonDataSource`
- 원격이어도 인터페이스 이름에는 `Remote` 가 없다. 인터페이스는 "포켓몬 데이터를 준다" 만 말하고 어디서 가져오는지는 말하지 않는다.

### 구현체: `<기술명><인터페이스명>`

- 인터페이스 이름 앞에 **데이터를 어디서 / 어떤 형식으로 가져오는지** 를 나타내는 기술명을 접두사로 붙인다.
- 기술명은 한 단어로, 보고 바로 알 수 있는 것을 쓴다.

| 기술            | 접두사                | 예시                                       | 실습 코드                   |
|-----------------|-----------------------|--------------------------------------------|-----------------------------|
| 로컬 파일(JSON) | `File`                | `FileItemDataSource`                       | `FileItemDataSource`        |
| 로컬 파일(CSV)  | `Csv`                 | `CsvStockDataSource`                       |                             |
| 원격 API        | `Remote` 또는 `Api`   | `RemoteItemDataSource`                     | `RemotePokemonDataSource`   |
| 데이터베이스    | `Db` 또는 구체 제품명 | `DbItemDataSource`, `SqliteItemDataSource` |                             |
| 테스트 더블     | `Fake`                | `FakeItemDataSource`                       | `FakePokemonDataSource` → [test.md](test.md) |

- 접두사 뒤는 **인터페이스 이름 그대로** 다. `RemotePokemonDataSource : PokemonDataSource` 처럼 뒤쪽이 일치해야 한다.
- 규칙과 다른 예 (다른 원격 실습 코드): `JsonPlaceHolderTodoDataSource : PostDataSource` — 접두사가 사이트 이름이고, 뒤쪽 `Todo` 가 인터페이스 `Post` 와 다르다. 규칙대로면 `RemotePostDataSource` 다.

### 응답 포장: `Response<T>`

- 원격 DataSource 가 돌려주는 포장 클래스는 `Response` 하나로 통일한다. `PokemonResponse`, `ApiResult` 처럼 모델마다 만들지 않는다. 제네릭 `T` 에 DTO 를 넣는다.
- `Response.kt` 는 `datasource` 패키지에 둔다. DataSource 가 만들고 Repository 가 풀기 때문에 둘 사이, 즉 DataSource 쪽에 있다. (3-2)

---

## 2. 캡슐화 원칙

> **인터페이스는 "무엇을 주고받는지"만 말한다. "어떻게"는 전부 구현체 안에 숨긴다.**

### 인터페이스에 노출하면 안 되는 것

| 분류            | 금지 예시                                        | 이유                                            |
|-----------------|--------------------------------------------------|-------------------------------------------------|
| 파일            | `File`, `Path`, 파일명 파라미터, `InputStream`   | CSV → JSON → DB 로 바뀌면 인터페이스가 깨진다   |
| DB              | `Connection`, `ResultSet`, SQL 문자열            | 사용자가 DB 종류를 알 필요가 없다               |
| 네트워크        | `HttpClient`, `HttpResponse`, URL, 헤더          | 통신 방식이 바뀌어도 사용처는 그대로여야 한다   |
| 직렬화          | `Json`, `JsonElement`, 원본 문자열, CSV 한 줄    | 포맷 변환은 구현체의 책임이다                   |
| 라이브러리 타입 | `kotlinx.serialization`, JDBC, Ktor 등 외부 타입 | 라이브러리 교체 시 인터페이스까지 수정하게 된다 |

```kotlin
// 나쁜 예: 기술 세부사항이 인터페이스로 새어 나왔다
interface ItemDataSource {
    fun loadItems(file: File): List<Item>          // File 노출
    fun loadJson(): String                         // 직렬화 포맷 노출, Json
    fun query(sql: String): ResultSet              // DB 노출, DB 에서만 쓰는 것
}

// 좋은 예: 모델만 오간다
interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}
```

```kotlin
// 좋은 예 (원격): 우리가 만든 Response 에 DTO 만 담아서 오간다. Ktor 타입은 없다
interface PokemonDataSource {
    suspend fun getPokemon(name: String): Response<PokemonDto>
}

// 나쁜 예 (원격)
interface PokemonDataSource {
    suspend fun getPokemon(name: String): HttpResponse          // Ktor 의 응답 타입 노출
    suspend fun getPokemon(url: String): Response<PokemonDto>   // URL 을 밖에서 조립하게 한다
    suspend fun getPokemonJson(name: String): String            // 파싱을 사용자에게 떠넘긴다
}
```

- `Response<T>` 는 **우리가 만든 data class** 라서 인터페이스에 써도 된다. Ktor 의 `HttpResponse` 를 그대로 돌려주면 위반이다. 이름이 비슷하니 주의한다.
- `suspend` 는 기술이 아니라 "기다려야 한다" 는 약속이라 인터페이스에 붙인다. 통신 라이브러리가 바뀌어도 기다려야 하는 것은 같다.

### 구현체 안에서 지킬 것

- 파일 경로, URL, 테이블명 같은 설정값은 `private val` 로 둔다.
  필요하면 **생성자 파라미터** 로 받고, 메서드 파라미터로는 받지 않는다.
  ```kotlin
  class FileItemDataSource(
      private val file: File = File("items.json"),
  ) : ItemDataSource { ... }
  ```
  ```kotlin
  // 원격 (원본 그대로): 통신 객체는 생성자로, URL 과 Json 설정은 private val 로
  class RemotePokemonDataSource(
      private val httpClient: HttpClient = HttpClientFactory.create()
  ) : PokemonDataSource {

      private val baseUrl = "https://pokeapi.co/api/v2"
      private val json = Json { ignoreUnknownKeys = true }
      ...
  }
  ```
- `HttpClient` 를 **생성자로 받는 이유** 는 테스트다. 테스트에서 `RemotePokemonDataSource(HttpClient(mockEngine))` 로 가짜 엔진을 꽂을 수 있다. 안에서 `HttpClient(CIO)` 를 직접 만들면 테스트가 진짜 서버로 나간다.
- 기본값 `= HttpClientFactory.create()` 를 두는 것은 **DataSource 에서는 허용** 한다. `HttpClient` 는 계층이 아니라 도구라서, `main` 이 매번 만들어 넘길 이유가 없다. (Repository 생성자에 DataSource 기본값을 두는 것은 금지다 — [repository.md 2-1](repository.md). 계층을 숨기는 것과 도구를 숨기는 것은 다르다.)
- 외부 라이브러리 import 는 구현체 파일에만 존재해야 한다.
  인터페이스 파일의 import 가 `model` (로컬) 또는 `dto` (원격) 패키지 **하나뿐** 이면 잘 지켜진 것이다.
- 사용하는 쪽은 **인터페이스 타입으로 선언** 한다.
  ```kotlin
  val dataSource: ItemDataSource = FileItemDataSource()             // O
  val dataSource = FileItemDataSource()                             // X (구현체에 묶임)
  val dataSource: PokemonDataSource = RemotePokemonDataSource()     // O
  ```

---

## 3. 반환 타입 원칙

> **밖으로 나가는 것은 정제된 것만이다. 로컬은 모델(또는 모델의 컬렉션), 원격은 DTO 를 담은 `Response`.**

### 3-1. 로컬 — 모델만 돌려준다

#### 허용

| 상황                     | 반환 타입          |
|--------------------------|--------------------|
| 여러 건 조회             | `List<Item>`       |
| 단건 조회 (없을 수 있음) | `Item?`            |
| 저장 / 삭제              | `Unit` (반환 없음) |

#### 금지

- 원본 문자열: `String`(JSON 텍스트, CSV 텍스트), `List<String>`(CSV 줄 목록) //객체에 담아 쓰자
- 중간 표현: `JsonElement`, `Map<String, Any>`, `Array<String>`(split 결과)
- 기술 타입: `ResultSet`, `HttpResponse`, `File`
- 가변 컬렉션: `MutableList<Item>` (내부 상태가 밖으로 새어 나간다)

```kotlin
// 나쁜 예
fun loadItems(): String                 // JSON 문자열을 그대로 던짐. 파싱은 사용자 몫
fun loadItems(): List<Array<String>>    // CSV split 결과. 컬럼 순서를 사용자가 알아야 함
fun loadItems(): MutableList<Item>      // 밖에서 add/remove 하면 내부 상태가 깨짐

// 좋은 예
fun loadItems(): List<Item>
```

#### 반환 전 정제 규칙

구현체는 **파싱 → 모델 변환 → 정제** 를 마친 뒤 반환한다.

1. 데이터가 없으면 예외 대신 **빈 컬렉션** 을 돌려준다. (`emptyList()`)
2. 헤더 줄, 빈 줄 등 데이터가 아닌 것은 구현체 안에서 제거한다.
3. 공백 제거 (`trim()`), 타입 변환 (`toInt()`, `toLong()`) 은 모델에 넣기 전에 끝낸다.
4. 내부에서 `mutableListOf` 로 모았더라도 반환 타입은 `List` 로 선언한다.

### 3-2. 원격 — `Response<DTO>` 를 돌려준다

```kotlin
// Response.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

data class Response<T>(
    val statusCode: Int = 200,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: T? = null,
)
```

| 필드         | 타입                  | 무엇을 담나                                 | 누가 쓰나                                   |
|--------------|-----------------------|---------------------------------------------|---------------------------------------------|
| `statusCode` | `Int`                 | HTTP 상태 코드 (`response.status.value`)    | Repository 가 성공 여부를 판단할 때         |
| `headers`    | `Map<String, String>` | 응답 헤더 (실습 코드는 기본값만 쓴다)       | (지금은 아무도 안 쓴다)                     |
| `body`       | `T?`                  | **DTO**. 없으면 `null`                      | Repository 가 `?: return null` 뒤 `toModel()` |

#### `T` 에 넣는 것

| `T`                  | 판정 | 이유                                                                 |
|----------------------|------|----------------------------------------------------------------------|
| `PokemonDto` (DTO)   | **O** | 파싱까지 DataSource 가 끝냈다. 실습 코드                            |
| `String` (JSON 원문) | X    | 파싱이 Repository 로 올라간다. 2장의 "직렬화 노출" 과 같은 위반       |
| `Pokemon` (모델)     | X    | DataSource 가 모델 모양까지 알게 된다. 변환은 Repository 가 Mapper 로 |
| `HttpResponse`       | X    | 기술 타입. 포장한 의미가 없다                                        |

```kotlin
// 규칙과 다른 예 (다른 원격 실습 코드): body 가 String 이라 Repository 가 파싱을 떠안는다
data class Response(
    val statusCode: Int = 200,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: String? = null,                                  // ← 제네릭이 아니고 원문 문자열
)

class PostRepositoryImpl(private val dataSource: PostDataSource) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        val response = dataSource.getPosts()
        return Json.decodeFromString<List<Post>>(response.body!!) // ← Repository 에 Json import, !! 까지
    }
}
```

- 위 예에서 Repository 파일에 `kotlinx.serialization.json.Json` 이 import 된 것이 "파싱이 Repository 로 올라왔다" 는 신호다 ([repository.md 2-2](repository.md)). 포켓몬 실습이 `Response<T>` + DTO 로 바꾼 것이 이 문제를 고친 것이다.

#### 반환 전 정제 규칙 (원격)

구현체는 **요청 → 문자열 → DTO decode → `Response` 포장** 을 마친 뒤 반환한다.

1. `bodyAsText()` 로 받은 문자열은 메서드 밖으로 내보내지 않는다. 바로 `json.decodeFromString<DTO>()` 한다.
2. `Json { ignoreUnknownKeys = true }` 로 만든다. 서버가 DTO 에 없는 키를 더 보내도 깨지지 않게 한다. (DTO 쪽 짝은 [dto.md 2-1](dto.md) 의 `= null` 기본값)
3. `statusCode` 는 `response.status.value` 를 그대로 담는다. 성공 · 실패 판단은 하지 않는다. 그 판단은 Repository 몫이다.
4. 통신 예외 · decode 예외는 **잡지 않고 던진다.** 실습 코드의 `RemotePokemonDataSource` 에는 `try` 가 없다. Repository 가 `catch` 해서 `null` 로 바꾼다 ([repository.md 3-3](repository.md)).
5. [관찰] 실습 코드는 `statusCode` 가 200 이 아니어도 그대로 decode 를 시도한다. 404 면 본문이 `"Not Found"` 라서 decode 가 예외를 던지고, Repository 의 `catch` 가 받아 `null` 이 된다. 결과는 맞지만 "404 라서" 가 아니라 "파싱이 깨져서" `null` 인 셈이다. 상태 코드로 먼저 거르려면 `if (response.status.value != 200) return Response(statusCode = ..., body = null)` 을 decode 앞에 둔다. (원본에는 없다. 규칙으로 정하지는 않는다.)

---

## 4. 새로운 DataSource 추가 템플릿

새 DataSource 를 만들 때는 다음 순서를 따른다.

1. `model` 패키지에 모델을 만든다. 로컬은 `@Serializable data class`, 원격은 **순수 코틀린** data class ([dto.md 0-1](dto.md)).
2. 원격이면 `dto` 패키지에 `<모델명>Dto` 를 만든다. → [dto.md](dto.md)
3. `datasource` 패키지에 `<모델명>DataSource` 인터페이스를 만든다. import 는 모델(로컬) 또는 DTO(원격) 하나.
4. `datasource` 패키지에 `<기술명><모델명>DataSource` 구현체를 만든다. 원격이면 `Response.kt` 도 같은 패키지에.
5. 원격이면 `mapper` 패키지에 `toModel()` 을 만든다. → [mapper.md](mapper.md)
6. Repository 에서 받아 가공한다. → [repository.md](repository.md)
7. `Main.kt` 에서 인터페이스 타입으로 선언해 사용한다.

### 4-1. 모델 + 인터페이스 (로컬, 공통)

```kotlin
// Item.kt
package com.survivalcoding.<dayXX>.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val price: Int,
)
```

```kotlin
// ItemDataSource.kt
package com.survivalcoding.<dayXX>.datasource

import com.survivalcoding.<dayXX>.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}
```

### 4-2. Remote API 구현체 (Ktor + DTO, 포켓몬 실습 원본)

Ktor `HttpClient` 를 쓴다. `build.gradle.kts` 에 다음이 있어야 한다. (원본 그대로)

```kotlin
    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // Http 통신
    implementation("io.ktor:ktor-client-core:3.6.0")
    implementation("io.ktor:ktor-client-cio:3.6.0")
```

네 파일이 한 세트다.

| 파일                          | 역할                                         | 패키지                                  |
|-------------------------------|----------------------------------------------|-----------------------------------------|
| `HttpClientFactory.kt`        | `HttpClient` 를 만드는 한 곳                 | 실습 루트 (다른 원격 실습과 공유)       |
| `Response.kt`                 | 응답 포장 (3-2)                              | `datasource`                            |
| `PokemonDataSource.kt`        | 인터페이스                                   | `datasource`                            |
| `RemotePokemonDataSource.kt`  | Ktor 구현체                                  | `datasource`                            |

```kotlin
// HttpClientFactory.kt  (원본 전체) — 다른 원격 실습 패키지에 있고, 포켓몬 실습이 import 해서 쓴다
package com.survivalcoding.<dayYY>

import io.ktor.client.*
import io.ktor.client.engine.cio.*

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient(CIO)
    }
}
```

- `HttpClient(CIO)` 를 만드는 코드가 **이 한 곳** 에만 있다. 엔진을 바꾸거나 타임아웃 · 로깅 설정을 더할 때 여기만 고친다. 각 DataSource 가 제각기 `HttpClient(CIO)` 를 만들지 않는다.
- `object` 라서 `HttpClientFactory.create()` 로 바로 부른다. 인스턴스를 만들 필요가 없다.

```kotlin
// PokemonDataSource.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

import com.survivalcoding.<dayXX>.dto.PokemonDto

interface PokemonDataSource {
    suspend fun getPokemon(name: String): Response<PokemonDto>
}
```

- import 가 `dto.PokemonDto` **하나** 다. `Response` 는 같은 패키지라 import 가 없다. Ktor 는 보이지 않는다.

```kotlin
// RemotePokemonDataSource.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

import com.survivalcoding.<dayYY>.HttpClientFactory
import com.survivalcoding.<dayXX>.dto.PokemonDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

// 데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자
class RemotePokemonDataSource(
    private val httpClient: HttpClient = HttpClientFactory.create()
) : PokemonDataSource {

    private val baseUrl = "https://pokeapi.co/api/v2"
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPokemon(name: String): Response<PokemonDto> {
        val response = httpClient.get("$baseUrl/pokemon/$name")
        val jsonString = response.bodyAsText()
        return Response(
            statusCode = response.status.value,
            body = json.decodeFromString<PokemonDto>(jsonString)
        )
    }
}
```

한 줄씩 보면:

| 줄                                                        | 하는 일                                                | 규칙                              |
|-----------------------------------------------------------|--------------------------------------------------------|-----------------------------------|
| `private val httpClient: HttpClient = HttpClientFactory.create()` | 통신 객체를 생성자로 받고, 안 주면 공장에서 만든다 | 2장 "구현체 안에서 지킬 것"       |
| `private val baseUrl = "https://pokeapi.co/api/v2"`        | URL 은 `private`. 밖에서 모른다                        | 2장                               |
| `private val json = Json { ignoreUnknownKeys = true }`     | 파서 설정도 `private`. 모르는 키는 무시                | 3-2 정제 규칙 2                   |
| `httpClient.get("$baseUrl/pokemon/$name")`                 | 요청. `name` 만 밖에서 받는다                          | 2장 (URL 조립은 안에서)           |
| `response.bodyAsText()`                                    | 응답 본문을 문자열로. 이 변수는 밖으로 안 나간다       | 3-2 정제 규칙 1                   |
| `json.decodeFromString<PokemonDto>(jsonString)`            | 문자열 → DTO. 실패하면 예외가 그대로 올라간다          | 3-2 정제 규칙 4                   |
| `Response(statusCode = ..., body = ...)`                    | 상태 코드와 DTO 를 포장해서 반환                       | 3-2                               |

- 이 파일의 import 에 `model.Pokemon` 이 **없다.** DataSource 는 모델을 모른다. DTO 까지가 끝이다.
- `io.ktor.client.statement.*` 가 `bodyAsText()` 를, `io.ktor.client.request.*` 가 `get()` 을 준다. 둘 다 확장 함수라 import 가 없으면 보이지 않는다.

### 4-3. DB 구현체

JDBC 예시다. (SQLite 를 쓰려면 `build.gradle.kts` 에 `org.xerial:sqlite-jdbc` 의존성을 추가한다.)

```kotlin
// DbItemDataSource.kt
package com.survivalcoding.<dayXX>.datasource

import com.survivalcoding.<dayXX>.model.Item
import java.sql.Connection
import java.sql.DriverManager

class DbItemDataSource(
    private val jdbcUrl: String = "jdbc:sqlite:items.db",
) : ItemDataSource {

    init {
        connect().use { conn ->
            conn.createStatement().use {
                it.execute(
                    """
                    CREATE TABLE IF NOT EXISTS items (
                        id    INTEGER PRIMARY KEY,
                        name  TEXT    NOT NULL,
                        price INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }

    private fun connect(): Connection = DriverManager.getConnection(jdbcUrl)

    override fun loadItems(): List<Item> {
        val result = mutableListOf<Item>()

        connect().use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery("SELECT id, name, price FROM items ORDER BY id")
                // ResultSet 은 여기서 끝낸다. 밖으로 나가는 것은 Item 만
                while (rs.next()) {
                    result.add(
                        Item(
                            id = rs.getLong("id"),
                            name = rs.getString("name"),
                            price = rs.getInt("price"),
                        )
                    )
                }
            }
        }

        return result   // 선언 타입은 List<Item>
    }

    override fun saveItems(items: List<Item>) {
        connect().use { conn ->
            conn.autoCommit = false
            conn.createStatement().use { it.execute("DELETE FROM items") }
            conn.prepareStatement("INSERT INTO items (id, name, price) VALUES (?, ?, ?)").use { ps ->
                for (item in items) {
                    ps.setLong(1, item.id)
                    ps.setString(2, item.name)
                    ps.setInt(3, item.price)
                    ps.addBatch()
                }
                ps.executeBatch()
            }
            conn.commit()
        }
    }
}
```

### 4-4. 사용처

구현체를 바꿔도 **첫 줄만 바뀐다.**

```kotlin
fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()
    // val dataSource: ItemDataSource = DbItemDataSource()

    dataSource.saveItems(
        listOf(
            Item(id = 1L, name = "Item 1", price = 1000),
            Item(id = 2L, name = "Item 2", price = 2000),
        )
    )

    dataSource.loadItems().forEach(::println)
}
```

```kotlin
// 원격 (포켓몬 실습 Main.kt 원본 중 조립 부분) — suspend 를 부르므로 runBlocking, DataSource 는 Repository 에 넣어서 쓴다
fun main(): Unit = runBlocking {
    val dataSource: PokemonDataSource = RemotePokemonDataSource()
    val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

    val pokemon: Pokemon? = repository.getPokemon("pikachu")
    println(pokemon?.name ?: "이름 없음")
    println(pokemon?.imageUrl ?: "이미지 없음")
}
```

- 원격 DataSource 는 `main` 이 **직접 부르지 않는다.** `Response<PokemonDto>` 를 `main` 이 받으면 DTO 가 화면까지 올라온다. 반드시 Repository 를 거쳐 모델로 받는다. ([repository.md 2-3](repository.md))

---

## 5. 테스트 — DataSource 는 MockEngine 으로

> **Repository 테스트는 Fake 로, DataSource 테스트는 Ktor MockEngine 으로.** 자세한 규칙은 [test.md](test.md).

- Fake 는 DataSource 를 **통째로 바꿔 끼우는** 것이라 DataSource 자체를 검증할 수 없다. DataSource 를 검증하려면 그 안의 `HttpClient` 만 가짜로 바꿔야 하고, 그것이 MockEngine 이다.
- 2장에서 `HttpClient` 를 생성자로 받게 한 이유가 여기서 쓰인다: `RemotePokemonDataSource(HttpClient(mockEngine))`.

```kotlin
// Mocks.kt  (테스트 소스셋, 원본 전체) — "이 URL 이 오면 이 응답" 을 미리 적어 둔 가짜 엔진
package com.survivalcoding.<dayXX>.data_source

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.engine.mock.respond

val mockEngine = MockEngine { request ->
    when (request.url.toString()) {
        "https://pokeapi.co/api/v2/pokemon/ditto" -> {
            respond(
                content = jsonString,
            )
        }

        else -> error("Unhandled ${request.url}")
    }
}
```

- `jsonString` 은 `FakePokemonDataSource.kt` 의 `const val` 이다. Fake 와 MockEngine 이 **같은 응답 전문** 을 쓴다.
- `build.gradle.kts` 의 `testImplementation("io.ktor:ktor-client-mock:3.6.0")` 이 필요하다. (원본에 있다)

```kotlin
// RemotePokemonDataSourceTest.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemotePokemonDataSourceTest {
    @Test
    fun `포켓몬 데이터가 잘 들어와야 한다`() = runBlocking {
        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))

        val response = dataSource.getPokemon("ditto")
        assertEquals(jsonString, response.body)
    }
}
```

- [발견한 문제] 마지막 줄이 `String`(`jsonString`) 과 `PokemonDto`(`response.body`) 를 비교한다. 타입이 달라 **항상 실패** 한다. 3-2 대로 `body` 는 DTO 이므로 `assertEquals("ditto", response.body?.name)` 처럼 DTO 의 필드를 비교해야 한다. (코드는 고치지 않았다. [test.md](test.md) 에도 적어 두었다.)

---

## 6. 커밋 전 체크리스트

- [ ] 인터페이스가 `<모델명>DataSource`, 구현체가 `<기술명><인터페이스명>` 이고 뒤쪽 이름이 일치하는가
- [ ] 인터페이스 파일의 import 가 `model` (로컬) 또는 `dto` (원격) **하나뿐** 인가
- [ ] 인터페이스에 `File`, `HttpClient`, `HttpResponse`, `Json`, `String`(원문), URL 파라미터가 없는가
- [ ] 로컬 반환 타입이 모델 · `List<모델>` · `모델?` · `Unit` 중 하나이고 `MutableList` 가 아닌가
- [ ] 원격 반환 타입이 `Response<DTO>` 이고, `T` 가 `String` 이나 모델이 아닌가
- [ ] 구현체의 URL · 파일 경로 · `Json` 설정이 `private val` 인가. `HttpClient` 는 생성자로 받는가
- [ ] 원격 구현체 import 에 `model.<모델>` 이 없는가 (DataSource 는 모델을 모른다)
- [ ] `bodyAsText()` 결과가 메서드 밖으로 안 나가고 바로 `decodeFromString<DTO>` 되는가
- [ ] `Json { ignoreUnknownKeys = true }` 인가
- [ ] 구현체에 `try/catch` 가 없고 예외를 Repository 로 올려 보내는가
- [ ] `HttpClient(CIO)` 를 만드는 코드가 `HttpClientFactory` 한 곳뿐인가
- [ ] 사용하는 쪽이 인터페이스 타입으로 선언했고, 원격은 `main` 이 직접 부르지 않고 Repository 를 거치는가
- [ ] DataSource 테스트가 `HttpClient(mockEngine)` 을 꽂아서 돌고, `body` 를 DTO 필드로 비교하는가
