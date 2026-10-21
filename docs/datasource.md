# DataSource 작성 원칙

`ItemDataSource` / `FileItemDataSource`(그리고 같은 패턴의 `TodoDataSource`, `StockDataSource`)를 분석해 정리한 규칙이다.
새로운 DataSource를 만들 때는 이 문서를 따른다.

## 현재 구조

```
kotlin_datasource/
├── model/                       # 도메인 모델 (Item, Todo, StockListing)
└── data_source/
    ├── ItemDataSource.kt        # 인터페이스: "무엇을 하는가"
    └── FileItemDataSource.kt    # 구현체:   "어떻게 하는가" (File + JSON)
```

```kotlin
// 인터페이스: 외부와 소통하는 계약
interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}

// 구현체: 파일/JSON 세부사항은 전부 여기에만 존재
class FileItemDataSource : ItemDataSource {
    private val file = File("items.json")

    override fun loadItems(): List<Item> { /* 파일 읽기 + JSON 디코딩 */ }
    override fun saveItems(items: List<Item>) { /* JSON 인코딩 + 파일 쓰기 */ }
}
```

---

## 원칙 1. 네이밍 규칙

| 대상 | 규칙 | 예시 |
|------|------|------|
| 인터페이스 | `{도메인}DataSource` — 기술명을 넣지 않는다 | `ItemDataSource`, `TodoDataSource`, `StockDataSource` |
| 구현체 | `{기술명}{도메인}DataSource` — 기술명을 **접두사**로 붙인다 | `FileItemDataSource`, `FileTodoDataSource`, `CsvStockDataSource` |
| 메서드 | `load{도메인복수형}` / `save{도메인복수형}` 처럼 도메인 단위 동사 + 명사 | `loadItems()`, `saveItems()`, `loadStockListings()` |

- 기술명 예: `File`, `Csv`, `Json`, `Memory`, `Remote`, `Room`, `Sql`
- 인터페이스 이름만 봐도 **어떤 데이터를 다루는지**, 구현체 이름만 봐도 **어떤 기술을 쓰는지** 알 수 있어야 한다.
- 인터페이스와 구현체는 같은 `data_source` 패키지에 둔다. 모델은 `model` 패키지에 분리한다.
- 메서드 이름에 기술 용어를 넣지 않는다. (`readJson()`, `queryDb()` ✗ → `loadItems()` ✓)

## 원칙 2. 캡슐화 원칙

기술 세부사항은 **구현체 내부(`private`)에만** 둔다. 인터페이스 시그니처에 다음이 나타나면 안 된다.

| 기술 | 인터페이스에 노출 금지 | 구현체 내부에 숨길 것 |
|------|-------------------------|------------------------|
| 파일 | `File`, `Path`, 파일명, `InputStream` | `private val file = File("items.json")` |
| DB | `Connection`, `Cursor`, SQL 문자열, Entity | 쿼리, 트랜잭션, Entity ↔ 모델 변환 |
| 네트워크 | `HttpClient`, URL, `Response`, DTO, 상태 코드 | 요청/응답 처리, 재시도, 헤더 |
| 직렬화 | `Json`, `String`(JSON 본문), `ByteArray` | `Json.decodeFromString` / `encodeToString` |

체크리스트:
- [ ] 인터페이스의 import에 `java.io`, `kotlinx.serialization.json`, `java.sql`, `okhttp`, `retrofit` 등이 없는가?
- [ ] 구현체의 필드(`file`, `client` 등)가 `private`인가?
- [ ] 저장 위치(파일 경로, URL)를 호출자가 알 필요가 없는가?
- [ ] 구현체를 바꿔도(File → DB) 호출 코드를 수정하지 않아도 되는가?

호출하는 쪽은 항상 **인터페이스 타입**으로 사용한다.

```kotlin
val dataSource: ItemDataSource = FileItemDataSource()  // 구현체 교체는 이 한 줄만 바뀐다
```

## 원칙 3. 반환 타입 원칙

외부에는 **정제된 도메인 모델 객체 또는 컬렉션**만 반환/수신한다.

- ✅ `List<Item>`, `Item?`, `List<StockListing>`
- ❌ `String`(원본 JSON/CSV), `File`, `ResultSet`, `Response<T>`, `ItemDto`, `JsonElement`

규칙:
1. **파싱·변환은 DataSource 안에서 끝낸다.** CSV 줄 분리, JSON 디코딩, DTO → 모델 변환 모두 구현체 책임이다.
2. **데이터가 없으면 `null`/예외가 아니라 빈 컬렉션을 반환한다.** 파일이 없거나 내용이 비어 있어도 `emptyList()`를 반환한다. (`FileItemDataSource`의 `!file.exists()`, `isBlank()` 처리)
3. 컬렉션은 읽기 전용 `List`로 노출한다. (`MutableList` ✗)
4. 모델(`Item`)은 `model` 패키지의 도메인 객체를 그대로 사용한다. 저장 형식 전용 모델이 필요하면(DTO, Entity) 구현체 내부에서만 쓰고 변환해서 내보낸다.
5. 값을 쓰는 메서드(`save*`)도 모델 컬렉션을 받는다. 직렬화된 문자열을 받지 않는다.

## 원칙 4. 새 DataSource 템플릿

### 4-1. 새 도메인을 위한 인터페이스

```kotlin
package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Product

// 외부 소통용 계약: 기술 세부사항 없음, 도메인 모델만 사용
interface ProductDataSource {
    fun loadProducts(): List<Product>
    fun saveProducts(products: List<Product>)
}
```

### 4-2. Remote API 구현체

```kotlin
package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Product
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RemoteProductDataSource : ProductDataSource {
    // 네트워크 세부사항은 모두 private
    private val client = HttpClient.newHttpClient()
    private val baseUrl = "https://example.com/api/products"
    private val json = Json { ignoreUnknownKeys = true }

    // 응답 전용 DTO: 외부로 절대 노출하지 않는다
    @Serializable
    private data class ProductDto(val id: Long, val name: String, val price: Int)

    override fun loadProducts(): List<Product> {
        val request = HttpRequest.newBuilder(URI.create(baseUrl)).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            return emptyList()  // 데이터 없음 = 빈 리스트 (실패 정책은 프로젝트 규칙에 맞춰 결정)
        }

        return json.decodeFromString<List<ProductDto>>(response.body())
            .map { Product(id = it.id, name = it.name, price = it.price) }  // DTO -> 모델
    }

    override fun saveProducts(products: List<Product>) {
        val body = json.encodeToString(
            products.map { ProductDto(it.id, it.name, it.price) }  // 모델 -> DTO
        )
        val request = HttpRequest.newBuilder(URI.create(baseUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()
        client.send(request, HttpResponse.BodyHandlers.discarding())
    }
}
```

### 4-3. DB 구현체 (JDBC 예시)

```kotlin
package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Product
import java.sql.DriverManager

class SqlProductDataSource : ProductDataSource {
    private val url = "jdbc:sqlite:products.db"  // 접속 정보는 private

    override fun loadProducts(): List<Product> {
        DriverManager.getConnection(url).use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery("SELECT id, name, price FROM products")
                val result = mutableListOf<Product>()
                while (rs.next()) {
                    result += Product(rs.getLong("id"), rs.getString("name"), rs.getInt("price"))
                }
                return result  // ResultSet이 아닌 List<Product>만 반환
            }
        }
    }

    override fun saveProducts(products: List<Product>) {
        DriverManager.getConnection(url).use { conn ->
            conn.prepareStatement("INSERT OR REPLACE INTO products(id, name, price) VALUES (?, ?, ?)").use { ps ->
                products.forEach {
                    ps.setLong(1, it.id)
                    ps.setString(2, it.name)
                    ps.setInt(3, it.price)
                    ps.addBatch()
                }
                ps.executeBatch()
            }
        }
    }
}
```

### 4-4. 테스트용 메모리 구현체 (선택)

```kotlin
class MemoryProductDataSource : ProductDataSource {
    private var products: List<Product> = emptyList()

    override fun loadProducts(): List<Product> = products
    override fun saveProducts(products: List<Product>) {
        this.products = products.toList()  // 방어적 복사
    }
}
```

### 4-5. 사용하는 쪽

```kotlin
fun main() {
    val dataSource: ProductDataSource = RemoteProductDataSource()  // 구현체 교체는 이 줄만
    dataSource.saveProducts(listOf(Product(1, "Sword", 1000)))
    println(dataSource.loadProducts())
}
```

---

## 새 DataSource 추가 체크리스트

- [ ] 모델 클래스를 `model` 패키지에 만들었다 (`@Serializable`은 필요한 경우만).
- [ ] 인터페이스 이름이 `{도메인}DataSource`이고, 시그니처에 기술 타입이 없다.
- [ ] 구현체 이름이 `{기술명}{도메인}DataSource`이다.
- [ ] 파일 경로, URL, 연결 정보, `Json`, DTO는 구현체 내부 `private`이다.
- [ ] 반환 타입이 `List<모델>` / `모델?` 이다.
- [ ] 데이터 없음(파일 없음, 빈 내용, 빈 응답)을 `emptyList()`로 처리했다.
- [ ] 호출 코드는 인터페이스 타입으로 변수를 선언했다.
- [ ] 구현체별 테스트(또는 `Memory` 구현체)를 만들었다.

## 참고: 현재 코드에서 개선할 점

- `CsvStockDataSource.loadStockListings()`의 `TODO(...)` 뒤 `return emptyList()`는 도달할 수 없는 코드이고, 파일이 없을 때 예외가 발생한다. 원칙 3-2(데이터 없음 → 빈 리스트)에 맞게 정리가 필요하다.
- 파일 경로(`"items.json"`)가 구현체에 하드코딩되어 있다. 테스트 편의를 위해 생성자 인자(`class FileItemDataSource(private val file: File = File("items.json"))`)로 받으면, 인터페이스는 그대로 두면서 경로만 주입할 수 있다.
