# DataSource 작성 원칙

`day15/datasource` 의 `ItemDataSource` / `FileItemDataSource` 를 기준으로 정리한, 새로운 DataSource 를 만들 때 지켜야 할 필수 원칙이다.

---

## 1. 네이밍 규칙

### 인터페이스: `<모델명>DataSource`

- 모델 이름 뒤에 `DataSource` 를 붙인다. 접두사 (`I`)나 기술명은 붙이지 않는다.
- `ItemDataSource`, `TodoDataSource`, `StockDataSource`

### 구현체: `<기술명><인터페이스명>`

- 인터페이스 이름 앞에 **데이터를 어디서 / 어떤 형식으로 가져오는지** 를 나타내는 기술명을 접두사로 붙인다.
- 기술명은 한 단어로, 보고 바로 알 수 있는 것을 쓴다.

| 기술            | 접두사                | 예시                                       |
|-----------------|-----------------------|--------------------------------------------|
| 로컬 파일(JSON) | `File`                | `FileItemDataSource`                       |
| 로컬 파일(CSV)  | `Csv`                 | `CsvStockDataSource`                       |
| 원격 API        | `Remote` 또는 `Api`   | `RemoteItemDataSource`                     |
| 데이터베이스    | `Db` 또는 구체 제품명 | `DbItemDataSource`, `SqliteItemDataSource` |

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

### 구현체 안에서 지킬 것

- 파일 경로, URL, 테이블명 같은 설정값은 `private val` 로 둔다.
  필요하면 **생성자 파라미터** 로 받고, 메서드 파라미터로는 받지 않는다.
  ```kotlin
  class FileItemDataSource(
      private val file: File = File("items.json"),
  ) : ItemDataSource { ... }
  ```
- 외부 라이브러리 import 는 구현체 파일에만 존재해야 한다.
  인터페이스 파일의 import 가 `model` 패키지 하나뿐이면 잘 지켜진 것이다.
- 사용하는 쪽은 **인터페이스 타입으로 선언** 한다.
  ```kotlin
  val dataSource: ItemDataSource = FileItemDataSource()   // O
  val dataSource = FileItemDataSource()                   // X (구현체에 묶임)
  ```

---

## 3. 반환 타입 원칙

> **밖으로 나가는 것은 정제된 모델 객체, 또는 모델 객체의 컬렉션만이다.**

### 허용

| 상황                     | 반환 타입          |
|--------------------------|--------------------|
| 여러 건 조회             | `List<Item>`       |
| 단건 조회 (없을 수 있음) | `Item?`            |
| 저장 / 삭제              | `Unit` (반환 없음) |

### 금지

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

### 반환 전 정제 규칙

구현체는 **파싱 → 모델 변환 → 정제** 를 마친 뒤 반환한다.

1. 데이터가 없으면 예외 대신 **빈 컬렉션** 을 돌려준다. (`emptyList()`)
2. 헤더 줄, 빈 줄 등 데이터가 아닌 것은 구현체 안에서 제거한다.
3. 공백 제거 (`trim()`), 타입 변환 (`toInt()`, `toLong()`) 은 모델에 넣기 전에 끝낸다.
4. 내부에서 `mutableListOf` 로 모았더라도 반환 타입은 `List` 로 선언한다.

---

## 4. 새로운 DataSource 추가 템플릿

새 DataSource 를 만들 때는 다음 순서를 따른다.

1. `model/` 에 `@Serializable data class` 모델을 만든다.
2. `datasource/` 에 `<모델명>DataSource` 인터페이스를 만든다. import 는 모델만.
3. `datasource/` 에 `<기술명><모델명>DataSource` 구현체를 만든다.
4. `Main.kt` 에서 인터페이스 타입으로 선언해 사용한다.

### 4-1. 모델 + 인터페이스 (공통)

```kotlin
// model/Item.kt
package com.survivalcoding.day15.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val price: Int,
)
```

```kotlin
// datasource/ItemDataSource.kt
package com.survivalcoding.day15.datasource

import com.survivalcoding.day15.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}
```

### 4-2. Remote API 구현체

JDK 11 에 내장된 `java.net.http.HttpClient` 를 사용한다. 추가 의존성이 필요 없다.

```kotlin

// datasource/RemoteItemDataSource.kt

package com.survivalcoding.day15.datasource


import com.survivalcoding.day15.model.Item

import kotlinx.serialization.json.Json

import java.net.URI

import java.net.http.HttpClient

import java.net.http.HttpRequest

import java.net.http.HttpResponse


class RemoteItemDataSource(

    private val baseUrl: String = "https://example.com/api",

    ) : ItemDataSource {


    // 기술 세부사항은 전부 private

    private val client: HttpClient = HttpClient.newHttpClient()

    private val json = Json { ignoreUnknownKeys = true }


    override fun loadItems(): List<Item> {

        val request = HttpRequest.newBuilder()

            .uri(URI.create("$baseUrl/items"))

            .GET()

            .build()


        val response = client.send(request, HttpResponse.BodyHandlers.ofString())


        // 실패나 빈 응답은 빈 컬렉션으로 정제해서 돌려준다

        if (response.statusCode() != 200) return emptyList()

        val body = response.body()

        if (body.isBlank()) return emptyList()


        return json.decodeFromString<List<Item>>(body)

    }


    override fun saveItems(items: List<Item>) {

        val request = HttpRequest.newBuilder()

            .uri(URI.create("$baseUrl/items"))

            .header("Content-Type", "application/json")

            .PUT(HttpRequest.BodyPublishers.ofString(json.encodeToString(items)))

            .build()


        client.send(request, HttpResponse.BodyHandlers.discarding())

    }

}

```

//### 4-3. DB 구현체

JDBC 예시다. (SQLite 를 쓰려면 `build.gradle.kts` 에 `org.xerial:sqlite-jdbc` 의존성을 추가한다.)

```kotlin

// datasource/DbItemDataSource.kt

package com.survivalcoding.day15.datasource


import com.survivalcoding.day15.model.Item

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
    // val dataSource: ItemDataSource = RemoteItemDataSource("https://example.com/api")
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
