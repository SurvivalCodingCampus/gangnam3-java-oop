# Repository 설계 및 구현 원칙 가이드

본 문서는 `ItemRepository`, `ItemRepositoryImpl`, `FakeItemDataSource`, `ItemRepositoryTest` 구현을 바탕으로, 데이터 접근 계층과 비즈니스 로직 사이에서 **단일 데이터 창구(Single Source of Truth, SSOT)** 역할을 수행하는 Repository를 일관되고 유지보수하기 쉽게 설계하기 위한 표준 가이드입니다.

---

## 1. 네이밍 및 패키지 구조 규칙

계층화 아키텍처(Layered Architecture)에서 각 구성 요소의 역할과 책임을 이름과 디렉토리 구조만으로 즉시 파악할 수 있도록 엄격한 네이밍 규칙과 패키지 경로를 정의합니다.

### 1.1 네이밍 규칙

| 분류                       | 네이밍 규칙                                   | 설명                                                             | 예시                                           |
| :------------------------- | :-------------------------------------------- | :--------------------------------------------------------------- | :--------------------------------------------- |
| **Repository 인터페이스**  | `[도메인/엔티티] + Repository`                | 비즈니스 로직에 노출되는 데이터 조작 계약(Contract)              | `ItemRepository`, `UserRepository`             |
| **Repository 구현체**      | `[도메인/엔티티] + RepositoryImpl`            | 인터페이스를 구현하며, DataSource를 주입받아 데이터 가공을 수행  | `ItemRepositoryImpl`, `UserRepositoryImpl`     |
| **DataSource 인터페이스**  | `[도메인/엔티티] + DataSource`                | 저장 매체(I/O, 네트워크, DB) 접근 계약                           | `ItemDataSource`, `UserDataSource`             |
| **DataSource 실제 구현체** | `[기술접두사] + [도메인/엔티티] + DataSource` | 구체적인 저장 기술(File, Remote, Db 등)을 반영한 구현체          | `FileItemDataSource`, `RemoteUserDataSource`   |
| **DataSource Fake 구현체** | `Fake + [도메인/엔티티] + DataSource`         | 빠른 단위 테스트를 위해 인메모리 컬렉션으로 동작하는 테스트 대역 | `FakeItemDataSource`, `FakeUserDataSource`     |
| **단위 테스트 클래스**     | `[구현체명]Test` 또는 `[인터페이스명]Test`    | Repository의 비즈니스 로직을 검증하는 JUnit5 테스트 클래스       | `ItemRepositoryTest`, `UserRepositoryImplTest` |

### 1.2 패키지 구조 규칙

프로젝트 소스 코드(`src/main/java`)와 테스트 코드(`src/test/java`)는 동일한 패키지 계층 구조를 미러링(Mirroring)하여 테스트 대상과 테스트 대역의 가시성과 접근성을 유지합니다.

```text
src/
├── main/java/com/survivalcoding/{feature}/
│   ├── model/                  # 순수 데이터 클래스 (Domain Model)
│   │   └── Item.kt
│   ├── datasource/             # 데이터 소스 인터페이스 및 실제 구현체
│   │   ├── ItemDataSource.kt
│   │   └── FileItemDataSource.kt
│   └── repository/             # 저장소 인터페이스 및 비즈니스 구현체
│       ├── ItemRepository.kt
│       └── ItemRepositoryImpl.kt
│
└── test/java/com/survivalcoding/{feature}/
    ├── datasource/             # 테스트 대역 (Test Double)
    │   └── FakeItemDataSource.kt
    └── repository/             # 단위 테스트 코드
        └── ItemRepositoryTest.kt
```

---

## 2. 의존성 주입(DI) 및 의존 역전 원칙(DIP)

Repository는 애플리케이션의 핵심 비즈니스 로직과 가장 맞닿아 있는 계층입니다. 특정 인프라 기술이나 외부 환경에 종속되지 않도록 **의존성 주입(Dependency Injection, DI)**과 **의존 역전 원칙(Dependency Inversion Principle, DIP)**을 반드시 준수해야 합니다.

### 2.1 생성자 주입(Constructor Injection) 강제

Repository 구현체는 필요한 모든 데이터 소스를 **반드시 주생성자(Primary Constructor)를 통해 인터페이스 형태로 주입**받아야 합니다.

```kotlin
// GOOD: 인터페이스를 생성자로 주입받음 (느슨한 결합, 테스트 용이)
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource
) : ItemRepository { ... }

// BAD: 구현체 클래스를 직접 생성하거나 특정 기술에 종속됨 (강한 결합)
class BadItemRepositoryImpl : ItemRepository {
    private val dataSource = FileItemDataSource() // 파일 시스템에 강결합!
}
```

### 2.2 기술 세부사항(File, DB, Retrofit 등) 의존 금지

- Repository는 `File`, `Json`, `Retrofit`, `RoomDatabase`, `SharedPreferences` 등 저수준 I/O 및 프레임워크 라이브러리를 **절대로 직접 참조하거나 import하지 않습니다**.
- 데이터가 로컬 JSON 파일에 저장되는지, SQLite DB에 저장되는지, 클라우드 REST API 서버에서 오는지 Repository는 알 필요가 없으며, 오직 `loadItems()`, `saveItems()` 같은 DataSource 인터페이스 메서드만을 호출합니다.
- 저장 매체가 파일에서 원격 서버(HTTP)로 변경되더라도 Repository의 코드는 단 한 줄도 수정되지 않아야 합니다.

---

## 3. 비즈니스 로직과 책임 분리 (Separation of Concerns)

Repository와 DataSource는 담당하는 역할과 책임이 명확히 다릅니다.

### 3.1 DataSource vs Repository 책임 분리 매트릭스

| 구분            | DataSource 계층                                     | Repository 계층                                                                  |
| :-------------- | :-------------------------------------------------- | :------------------------------------------------------------------------------- |
| **주요 관심사** | 데이터 영속성(Persistence) 및 원시 I/O 조작         | 비즈니스 요구사항에 따른 데이터 가공 및 단일 창구 제공                           |
| **담당 업무**   | 파일 읽기/쓰기, 직렬화/역직렬화, DB 쿼리, HTTP 요청 | 정렬(Sorting), 필터링(Filtering), 검색(Searching), 데이터 집계                   |
| **상태 변경**   | 전달받은 원시 데이터를 물리 저장소에 동기화         | 비즈니스 규칙 검증, 기존 데이터 조회 후 추가/수정/삭제 후 DataSource에 저장 위임 |
| **예외 처리**   | I/O 예외, 역직렬화 실패, 네트워크 오류 처리         | 비즈니스 예외 처리 및 가공된 도메인 모델 반환                                    |

### 3.2 Repository의 핵심 역할

1. **단일 데이터 창구 (Single Source of Truth, SSOT)**:
   - 프레젠테이션 계층(UI/ViewModel)은 데이터를 얻기 위해 여러 DataSource를 헤매지 않고, 오직 Repository만 바라봅니다.
2. **데이터 가공 및 도메인 로직 집약**:
   - `getItems()`: 이름 오름차순 정렬 (`sortedBy { it.name }`)
   - `getItemsUnderPrice(maxPrice)`: 가격 조건 필터링 (`filter { it.price <= maxPrice }`)
   - `searchByName(query)`: 대소문자 무시 검색 (`contains(query, ignoreCase = true)`)
3. **저장 매체 조작 위임**:
   - `addItem(item)`: 현재 데이터를 조회(`loadItems()`)하고 새 항목을 추가한 뒤, DataSource의 `saveItems()`에 위임합니다.
   - `deleteItem(id)`: 해당 ID를 제외(`filterNot { it.id == id }`)한 후 DataSource에 저장을 위임합니다.

---

## 4. 테스트 용이성 및 Test Double (Fake) 원칙

빠르고 신뢰할 수 있는 단위 테스트를 작성하기 위해 Mock 프레임워크 대신 **Fake DataSource**를 우선적으로 사용하는 것을 표준 가이드로 삼습니다.

### 4.1 Mock 라이브러리 대신 Fake를 우선 사용하는 이유

| 비교 항목           | Mock 객체 (Mockito, MockK)                               | Fake 객체 (In-Memory FakeDataSource)                             |
| :------------------ | :------------------------------------------------------- | :--------------------------------------------------------------- |
| **동작 방식**       | 메서드 호출 여부와 반환값을 사전에 Stubbing/Mocking      | 메모리 컬렉션(`List`, `Map`)을 활용하여 실제처럼 완전하게 동작   |
| **실행 속도**       | 바이트코드 조작 및 리플렉션 오버헤드로 다소 느림         | 순수 JVM 메모리 연산으로 **수 밀리초(ms) 단위 초고속 실행**      |
| **검증 방식**       | 행위 검증(Behavior Verification, `verify(mock)...`) 위주 | **상태 기반 검증(State Verification)**으로 실제 결과값 검증      |
| **리팩터링 내구성** | 내부 호출 방식 변경 시 테스트가 쉽게 깨짐                | 비즈니스 결과가 올바르면 내부 구현 변경에도 테스트가 깨지지 않음 |
| **유지보수성**      | 테스트마다 복잡한 stubbing 코드가 필요                   | 초기 데이터 설정 및 재사용이 간단하고 직관적임                   |

### 4.2 FakeDataSource 구현 가이드

- **인메모리 컬렉션 보관**: 실제 파일이나 DB 대신 클래스 내부의 인메모리 필드(예: `List<T>`, `MutableList<T>`)를 데이터 저장소로 사용합니다.
- **초기값 주입 지원**: 생성자 파라미터로 `initialItems: List<T> = emptyList()`를 제공하여 테스트 픽스처(Fixture)를 손쉽게 세팅할 수 있도록 합니다.
- **완전한 인터페이스 구현**: 인터페이스의 계약(`load`, `save`)을 메모리 상에서 완벽히 모사하여, Repository 입장에서 실제 저장소와 구별할 수 없도록 만듭니다.

---

## 5. 표준 템플릿 코드 예시 (Item 도메인 기반)

향후 신규 Repository를 생성할 때 복사하여 기반 코드로 활용할 수 있는 표준 템플릿입니다.

### 5.1 Repository 인터페이스 (`ItemRepository.kt`)

```kotlin
package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
    fun getItem(id: Long): Item?
    fun getItemsUnderPrice(maxPrice: Int): List<Item>
    fun searchByName(query: String): List<Item>
    fun addItem(item: Item)
    fun deleteItem(id: Long)
}
```

### 5.2 Repository 구현체 (`ItemRepositoryImpl.kt`)

```kotlin
package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.ItemDataSource
import com.survivalcoding.day13_model_repository.model.Item

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource
) : ItemRepository {

    override fun getItems(): List<Item> {
        return dataSource.loadItems().sortedBy { it.name }
    }

    override fun getItem(id: Long): Item? {
        return dataSource.loadItems().find { it.id == id }
    }

    override fun getItemsUnderPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems().filter { it.price <= maxPrice }
    }

    override fun searchByName(query: String): List<Item> {
        return dataSource.loadItems().filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override fun addItem(item: Item) {
        val currentItems = dataSource.loadItems().toMutableList()
        currentItems.add(item)
        dataSource.saveItems(currentItems)
    }

    override fun deleteItem(id: Long) {
        val currentItems = dataSource.loadItems().filterNot { it.id == id }
        dataSource.saveItems(currentItems)
    }
}
```

### 5.3 Test Double Fake 구현체 (`FakeItemDataSource.kt`)

```kotlin
package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Item

class FakeItemDataSource(
    initialItems: List<Item> = emptyList()
) : ItemDataSource {

    private var items: List<Item> = initialItems

    override fun loadItems(): List<Item> {
        return items
    }

    override fun saveItems(items: List<Item>) {
        this.items = items
    }
}
```

### 5.4 Repository 단위 테스트 코드 (`ItemRepositoryTest.kt`)

```kotlin
package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.FakeItemDataSource
import com.survivalcoding.day13_model_repository.datasource.ItemDataSource
import com.survivalcoding.day13_model_repository.model.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ItemRepositoryTest {

    private lateinit var dataSource: ItemDataSource
    private lateinit var repository: ItemRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakeItemDataSource(
            initialItems = listOf(
                Item(id = 1L, name = "Apple", price = 1000),
                Item(id = 2L, name = "Banana", price = 2000),
                Item(id = 3L, name = "Cherry", price = 1500),
            )
        )
        repository = ItemRepositoryImpl(dataSource)
    }

    @Test
    @DisplayName("전체 조회 시 아이템 이름 오름차순으로 정렬되어 반환된다")
    fun getItems_returnsItemsSortedByName() {
        val items = repository.getItems()

        assertEquals(3, items.size)
        assertEquals(listOf("Apple", "Banana", "Cherry"), items.map { it.name })
    }

    @Test
    @DisplayName("존재하는 ID로 단건 조회 시 해당 아이템이 정상 반환된다")
    fun getItem_returnsItem_whenIdExists() {
        val item = repository.getItem(2L)

        assertNotNull(item)
        assertEquals("Banana", item?.name)
    }

    @Test
    @DisplayName("존재하지 않는 ID로 단건 조회 시 null이 반환된다")
    fun getItem_returnsNull_whenIdNotExists() {
        val item = repository.getItem(999L)

        assertNull(item)
    }

    @Test
    @DisplayName("지정한 가격 이하의 아이템 목록만 필터링되어 반환된다")
    fun getItemsUnderPrice_returnsFilteredItems() {
        val items = repository.getItemsUnderPrice(1500)

        assertEquals(2, items.size)
        assertTrue(items.all { it.price <= 1500 })
    }

    @Test
    @DisplayName("이름 검색 시 대소문자를 구분하지 않고 일치하는 아이템이 반환된다")
    fun searchByName_returnsMatchingItemsIgnoreCase() {
        val items = repository.searchByName("an")

        assertEquals(1, items.size)
        assertEquals("Banana", items[0].name)
    }

    @Test
    @DisplayName("신규 아이템을 추가하면 정상적으로 목록에 반영된다")
    fun addItem_addsItemSuccessfully() {
        val newItem = Item(id = 4L, name = "Durian", price = 5000)
        repository.addItem(newItem)

        val items = repository.getItems()
        assertEquals(4, items.size)
        assertTrue(items.contains(newItem))
    }

    @Test
    @DisplayName("아이템 삭제 시 해당 아이템이 목록에서 제거된다")
    fun deleteItem_removesItemSuccessfully() {
        repository.deleteItem(1L)

        val items = repository.getItems()
        assertEquals(2, items.size)
        assertNull(repository.getItem(1L))
    }
}
```

---

## 6. Repository 작성 시 점검 체크리스트

신규 Repository와 테스트 코드를 작성할 때 다음 체크리스트를 확인하여 품질을 검증합니다.

- [ ] **네이밍 규칙 준수**
  - 인터페이스는 `~Repository`, 구현체는 `~RepositoryImpl`로 명명했는가?
  - 테스트 대역은 `Fake~DataSource`로 명명했는가?
- [ ] **패키지 위치 준수**
  - 메인 코드는 `repository/`와 `datasource/` 패키지로 명확히 분리했는가?
  - 테스트 대역과 테스트 코드는 `src/test/`의 미러링된 패키지에 위치시켰는가?
- [ ] **의존성 주입 및 결합도 분리**
  - Repository가 구체 클래스가 아닌 `~DataSource` 인터페이스를 주생성자로 주입받았는가?
  - Repository 내부에 `File`, `Json`, `Http`, `Db` 등 특정 기술에 대한 의존성이 완전히 제거되었는가?
- [ ] **책임 분리 원칙**
  - 물리적 저장/불러오기는 DataSource에 위임하고, Repository는 데이터 가공(정렬, 검색, 필터링 등)에 집중했는가?
- [ ] **Test Double 및 테스트 독립성**
  - 단위 테스트에서 외부 I/O나 Mock 라이브러리 없이 `Fake~DataSource`를 활용했는가?
  - 테스트가 순수 JVM 환경에서 수 밀리초 단위로 신속하고 결정론적(Deterministic)으로 동작하는가?
