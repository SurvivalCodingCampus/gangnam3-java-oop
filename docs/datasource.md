# DataSource 설계 및 구현 원칙 가이드

본 문서는 `ItemDataSource`와 `FileItemDataSource` 구현을 바탕으로, 프로젝트 내에서 일관성 있고 유지보수하기 쉬운 DataSource를 개발하기 위해 준수해야 할 설계 원칙과 표준 가이드를 정의합니다.

---

## 1. 인터페이스와 구현체의 네이밍 규칙

DataSource는 데이터의 영속성(Persistence) 및 원격 조회를 추상화하는 계층입니다. 역할과 기술적 특징이 이름에서 명확히 드러나도록 명명합니다.

### 1.1 인터페이스 네이밍

- **규칙:** `[도메인/데이터 대상] + DataSource`
- **의미:** 무엇(What)에 대한 데이터 소스인지를 나타내며, 저장 위치나 방식은 명시하지 않습니다.
- **예시:**
  - `ItemDataSource`
  - `TodoDataSource`
  - `StockDataSource`

### 1.2 구현체 네이밍

- **규칙:** `[기술/저장소 접두사] + [인터페이스명]`
- **권장:** 어떤 매체나 기술(How)을 사용하는지 명확한 접두사(Prefix)를 붙입니다.
- **지양:** 아무런 기술 정보를 담지 못하는 `ItemDataSourceImpl`과 같은 포괄적 접미사 사용을 피합니다.
- **구현체 접두사 분류:**
  - **파일 기반:** `FileItemDataSource`, `CsvStockDataSource`
  - **원격 API / 네트워크:** `RemoteItemDataSource`, `HttpItemDataSource`
  - **데이터베이스:** `DbItemDataSource`, `RoomItemDataSource`, `SqliteItemDataSource`
  - **인메모리 / 테스트 더블:** `MemoryItemDataSource`, `MockItemDataSource`, `FakeItemDataSource`

---

## 2. 기술 세부사항 캡슐화 원칙 (Encapsulation)

인터페이스는 순수한 데이터 접근 계약(Contract)이어야 하며, 구체적인 기술이나 인프라 세부사항이 인터페이스에 누출(Leak)되어서는 안 됩니다.

### 2.1 메서드 시그니처에 기술 용어 배제

- **Bad:** `readFile()`, `writeJson()`, `fetchFromHttpApi()`, `querySql()`
- **Good:** `loadItems()`, `saveItems()`, `getItem(id: Long)`, `deleteItem(id: Long)`
- **이유:** 호출자(Repository, UseCase 등)는 데이터가 파일에 저장되는지, 네트워크 통신을 하는지 알 필요가 없어야 하며, 구현체가 바뀌어도 인터페이스를 사용하는 코드는 수정되지 않아야 합니다.

### 2.2 구현 세부사항은 `private` 영역에 은닉

- 파일 객체(`File`, `Path`), 데이터베이스 커넥션(`Connection`, `DatabaseHelper`), 네트워크 클라이언트(`HttpClient`, `Retrofit`), 파일 경로나 URL 엔드포인트 문자열은 구현체 클래스 내부에 `private`으로 캡슐화하거나 생성자 주입을 통해 받습니다.
- 직렬화/역직렬화 엔진(`Json`, `Xml`, `CsvParser` 등)의 사용 여부와 방식 역시 구현체 내부에 완전히 감춥니다.

---

## 3. 반환 타입 및 파라미터 원칙

DataSource의 경계를 넘나드는 데이터는 항상 가공되고 검증된 **도메인 모델(Model) 객체**여야 합니다.

### 3.1 원시 데이터(Raw Data) 노출 금지

- `String(JSON 문자열, CSV 라인)`, `ByteArray`, `InputStream`, `ResultSet`, `HttpResponse` 등의 저수준 원시 데이터를 인터페이스 반환 타입이나 파라미터로 노출하지 않습니다.
- 반드시 구현체 내부에서 직렬화/역직렬화 과정을 거쳐 `Item`, `List<Item>`과 같은 모델 객체로 변환하여 주고받습니다.

### 3.2 안전한 데이터 반환

- 데이터가 없거나 파일이 비어있는 경우, `null`보다는 빈 컬렉션(`emptyList()`)을 반환하여 호출 측의 불필요한 null 체크를 줄입니다.
- 단건 조회의 경우 결과가 없을 수 있다면 nullable(`Item?`)을 명시하여 안전성을 확보합니다.

---

## 4. 신규 DataSource 템플릿 코드 예시

### 4.1 기본 인터페이스 정의 (`ItemDataSource.kt`)

```kotlin
package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}
```

---

### 4.2 파일 기반 구현체 (`FileItemDataSource.kt`)

```kotlin
package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item
import kotlinx.serialization.json.Json
import java.io.File

class FileItemDataSource(
    private val file: File = File("items.json")
) : ItemDataSource {

    override fun loadItems(): List<Item> {
        if (!file.exists()) {
            return emptyList()
        }

        val jsonText = file.readText()
        if (jsonText.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Item>>(jsonText)
    }

    override fun saveItems(items: List<Item>) {
        val jsonText = Json.encodeToString(items)
        file.writeText(jsonText)
    }
}
```

---

### 4.3 원격 API 기반 구현체 템플릿 (`RemoteItemDataSource.kt`)

```kotlin
package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item
import kotlinx.serialization.json.Json

class RemoteItemDataSource(
    private val baseUrl: String = "https://api.example.com"
) : ItemDataSource {

    override fun loadItems(): List<Item> {
        // 1. 네트워크 통신으로 HTTP GET 요청 수행
        // val responseJson: String = httpClient.get("$baseUrl/items")

        // 2. 응답 상태 검증 및 역직렬화 수행
        // return Json.decodeFromString<List<Item>>(responseJson)

        // 예시용 기본 동작
        return emptyList()
    }

    override fun saveItems(items: List<Item>) {
        // 1. 모델 객체를 JSON 문자열로 직렬화
        // val requestJson = Json.encodeToString(items)

        // 2. 네트워크 통신으로 HTTP POST/PUT 요청 수행
        // httpClient.post("$baseUrl/items", body = requestJson)
    }
}
```

---

### 4.4 데이터베이스 기반 구현체 템플릿 (`DbItemDataSource.kt`)

```kotlin
package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item

class DbItemDataSource : ItemDataSource {

    override fun loadItems(): List<Item> {
        // 1. DB 쿼리 실행 (예: SELECT * FROM items)
        // 2. Cursor/ResultSet 데이터를 List<Item> 객체로 매핑하여 반환
        return emptyList()
    }

    override fun saveItems(items: List<Item>) {
        // 1. 트랜잭션 시작
        // 2. items 컬렉션을 순회하며 INSERT / UPDATE 실행
        // 3. 트랜잭션 커밋
    }
}
```

---

### 4.5 테스트 및 메모리 캐싱용 구현체 템플릿 (`MemoryItemDataSource.kt`)

```kotlin
package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Item

class MemoryItemDataSource(
    initialItems: List<Item> = emptyList()
) : ItemDataSource {

    private val storage = initialItems.toMutableList()

    override fun loadItems(): List<Item> {
        return storage.toList()
    }

    override fun saveItems(items: List<Item>) {
        storage.clear()
        storage.addAll(items)
    }
}
```

---

## 5. DataSource 작성 시 점검 체크리스트

- [ ] **인터페이스명:** `~DataSource` 형식으로 비즈니스 대상에 맞게 명명했는가?
- [ ] **구현체명:** `File~`, `Remote~`, `Db~`, `Memory~` 등 구체적인 기술 접두사를 사용했는가?
- [ ] **메서드명 추상화:** 메서드명에 `Json`, `File`, `Http`, `Sql` 등의 기술 세부사항이 포함되어 있지 않은가?
- [ ] **기술 은닉:** I/O 리소스, 네트워크 클라이언트, 직렬화 도구가 `private`으로 캡슐화되어 있는가?
- [ ] **타입 무결성:** 원시 텍스트나 바이트가 아닌 정제된 모델 객체(`Item`, `List<Item>`)를 입출력으로 사용하는가?
- [ ] **예외/부재 대응:** 데이터가 없는 경우 안전하게 빈 컬렉션 또는 적절한 기본값을 반환하는가?
