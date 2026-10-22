# Repository 작성 원칙

`day15`의 `ItemRepository` / `ItemRepositoryImpl` / `FakeItemDataSource` / `ItemRepositoryTest`를 기준으로 정리한, 새로운 Repository를 만들 때 지켜야 할 필수 원칙이다.

---

## 1. 네이밍 및 패키지 구조 규칙

### 1-1. 네이밍 규칙

| 역할 | 명명 규칙 | 예시 | 설명 |
|---|---|---|---|
| **인터페이스** | `<모델명>Repository` | `ItemRepository` | 접두사(`I`)나 기술명을 붙이지 않는다. 비즈니스 관점의 명칭 사용. |
| **구현체** | `<모델명>RepositoryImpl` | `ItemRepositoryImpl` | 기술명이 아닌 `Impl` 접미사를 붙인다. 특정 저장 기술에 종속되지 않는 비즈니스 계층이기 때문. |
| **Fake 구현체** | `Fake<모델명>DataSource` | `FakeItemDataSource` | 테스트를 위해 DataSource 인터페이스를 메모리(In-Memory) 상에서 구현한 Test Double. |

### 1-2. 패키지 구조 규칙
- 패키지명은 소문자 표준(`lower_case` 또는 `lowercase`)을 준수한다.
- DataSource 계층과 Repository 계층을 명확히 분리한다.

```
src/
├── main/kotlin/com/survivalcoding/day15/
│   ├── model/
│   │   └── Item.kt                      # 데이터 모델
│   ├── datasource/
│   │   └── ItemDataSource.kt            # DataSource 인터페이스
│   ├── datasourceImpl/
│   │   └── FileItemDataSource.kt        # 저수준 I/O 구현체 (File, DB 등)
│   ├── repository/
│   │   └── ItemRepository.kt            # Repository 인터페이스
│   └── repositoryImpl/
│       └── ItemRepositoryImpl.kt        # Repository 구현체
└── test/kotlin/day15/
    ├── datasource/
    │   └── FakeItemDataSource.kt        # 테스트용 In-Memory DataSource
    └── repository/
        └── ItemRepositoryTest.kt        # Fake를 주입받은 비즈니스 로직 단위 테스트
```

---

## 2. 의존성 주입(DI) 원칙

> **Repository는 특정 기술(File, DB, Retrofit 등)에 의존하지 않고, 오직 DataSource 인터페이스를 생성자로 주입받아야 한다.**

- **DIP (의존관계 역전 원칙, Dependency Inversion Principle)**:
  - Repository는 저수준의 세부 기술 구현체(`FileItemDataSource`, `DbItemDataSource`)를 직접 알지 못해야 한다.
  - 오직 고수준의 추상화인 `ItemDataSource` 인터페이스에만 의존한다.
- **장점**:
  1. 데이터 저장 매체가 로컬 파일에서 DB, 네트워크 API 등으로 변경되어도 Repository의 코드는 단 한 줄도 수정되지 않는다.
  2. 실제 파일 I/O나 외부 연결 없이도, 테스트 시 `FakeItemDataSource`를 주입하여 손쉽게 격리된 단위 테스트를 수행할 수 있다.

```kotlin
// 나쁜 예: 특정 기술 구현체에 직접 의존 (결합도 증가, 테스트 어려움)
class ItemRepositoryImpl(
    private val dataSource: FileItemDataSource // File에 강하게 결합되어 교체 불가
) : ItemRepository { ... }

// 좋은 예: 인터페이스를 생성자로 주입받음 (느슨한 결합, 유연한 교체)
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource // 어떤 구현체(File, DB, Fake)든 수용 가능
) : ItemRepository { ... }
```

---

## 3. 비즈니스 로직과 책임 분리 (Separation of Concerns)

> **저장 매체 조작(I/O)은 DataSource에 위임하고, Repository는 데이터 가공(필터링, 정렬, 검색, 비즈니스 규칙)에 집중한다.**

### 3-1. 계층별 역할과 책임

| 계층 | 주요 책임 | 수행하는 작업 | 금지 작업 |
|---|---|---|---|
| **DataSource** | 저수준 데이터 입출력 (I/O) | 파일 읽기/쓰기, 네트워크 통신, DB 쿼리, JSON/CSV 직렬화 및 역직렬화 | 비즈니스 필터링, 정렬, 비즈니스 규칙 검증 |
| **Repository** | 도메인 데이터 가공 및 비즈니스 규칙 | 조건별 필터링, 정렬, 데이터 검색, 비즈니스 유효성 검증, 데이터 집계 | 파일 경로 직접 조작, 외부 라이브러리 직접 호출(Json 등) |

### 3-2. 올바른 책임 분리 코드
```kotlin
// 나쁜 예: Repository가 파일 조작이나 직렬화를 직접 수행하는 경우
class ItemRepositoryImpl : ItemRepository {
    private val path = Path("items.json") // 저수준 파일 I/O 책임이 새어 나옴
}

// 좋은 예: I/O는 DataSource에 위임하고 가공(필터링, 정렬)에 집중
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource,
) : ItemRepository {

    override fun getItems(): List<Item> {
        return dataSource.loadItems()
    }

    override fun getItemsByMaxPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems()
            .filter { it.price <= maxPrice } // 비즈니스 필터링
            .sortedBy { it.price }           // 비즈니스 정렬
    }
}
```

---

## 4. 테스트 용이성 및 Test Double 원칙

> **순수 JVM 환경에서 빠른 테스트를 위해 Mock 프레임워크 대신 In-Memory Fake를 우선 사용한다.**

### 4-1. Mock 대신 Fake를 우선 사용하는 이유
1. **초고속 실행 (순수 JVM 환경)**:
   - Mock 프레임워크(Mockito, MockK)는 리플렉션과 동적 프록시 생성 오버헤드로 인해 실행 속도가 상대적으로 느리다.
   - Fake는 순수 Kotlin 컬렉션(`mutableListOf`)으로 동작하므로 오버헤드 없이 1ms 미만의 초고속 단위 테스트가 가능하다.
2. **동작의 신뢰성 보장 (State Verification)**:
   - Mock은 호출 결과를 하드코딩(`returns ...`)하여 가짜로 응답하므로 구현 변경 시 테스트가 쉽게 깨진다.
   - Fake는 실제 인터페이스 규약대로 데이터 저장/조회 상태 변화를 충실히 반영하므로 신뢰성이 높다.
3. **외부 환경 독립성**:
   - 파일 생성, 디스크 권한, 네트워크 상태 등에 전혀 영향받지 않으며 테스트 종료 후 정리(cleanup)할 필요가 없다.

### 4-2. In-Memory Fake 작성 가이드
- `ItemDataSource` 인터페이스를 구현한다.
- 내부에 메모리 컬렉션(`private val items = initialItems.toMutableList()`)을 둔다.
- 테스트 시 초기 데이터 주입을 지원하기 위해 생성자 파라미터(`initialItems: List<Item> = emptyList()`)를 제공한다.
- `loadItems()` 호출 시 외부에서 내부 컬렉션을 변경하지 못하도록 불변 리스트(`items.toList()`)로 방어적 복사하여 반환한다.

---

## 5. 새로운 Repository 추가 템플릿 예시

향후 새로운 Repository를 추가할 때는 다음 템플릿 구조를 그대로 활용한다.

### 5-1. 모델 및 DataSource 인터페이스
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

### 5-2. Repository 인터페이스
```kotlin
// repository/ItemRepository.kt
package com.survivalcoding.day15.repository

import com.survivalcoding.day15.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
    fun getItem(id: Long): Item?
    fun getItemsByMaxPrice(maxPrice: Int): List<Item>
    fun saveItem(item: Item)
}
```

### 5-3. Repository 구현체
```kotlin
// repositoryImpl/ItemRepositoryImpl.kt
package com.survivalcoding.day15.repositoryImpl

import com.survivalcoding.day15.datasource.ItemDataSource
import com.survivalcoding.day15.model.Item
import com.survivalcoding.day15.repository.ItemRepository

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource, // DI: DataSource 인터페이스 의존
) : ItemRepository {

    override fun getItems(): List<Item> {
        return dataSource.loadItems()
    }

    override fun getItem(id: Long): Item? {
        return dataSource.loadItems().find { it.id == id }
    }

    override fun getItemsByMaxPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems()
            .filter { it.price <= maxPrice }
            .sortedBy { it.price }
    }

    override fun saveItem(item: Item) {
        val currentItems = dataSource.loadItems().toMutableList()
        val index = currentItems.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            currentItems[index] = item
        } else {
            currentItems.add(item)
        }
        dataSource.saveItems(currentItems)
    }
}
```

### 5-4. 테스트용 In-Memory Fake DataSource 구현체
```kotlin
// test/kotlin/day15/datasource/FakeItemDataSource.kt
package day15.datasource

import com.survivalcoding.day15.datasource.ItemDataSource
import com.survivalcoding.day15.model.Item

class FakeItemDataSource(
    initialItems: List<Item> = emptyList(),
) : ItemDataSource {

    private val items = initialItems.toMutableList()

    override fun loadItems(): List<Item> {
        return items.toList() // 방어적 복사를 통해 불변 리스트 반환
    }

    override fun saveItems(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
    }
}
```

### 5-5. Repository 단위 테스트
```kotlin
// test/kotlin/day15/repository/ItemRepositoryTest.kt
package day15.repository

import com.survivalcoding.day15.model.Item
import com.survivalcoding.day15.repository.ItemRepository
import com.survivalcoding.day15.repositoryImpl.ItemRepositoryImpl
import day15.datasource.FakeItemDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemRepositoryTest {

    private lateinit var fakeDataSource: FakeItemDataSource
    private lateinit var repository: ItemRepository

    private val sampleItems = listOf(
        Item(id = 1L, name = "Keyboard", price = 50000),
        Item(id = 2L, name = "Mouse", price = 30000),
        Item(id = 3L, name = "Monitor", price = 200000),
    )

    @BeforeEach
    fun setUp() {
        // In-Memory Fake를 주입하여 파일/DB I/O 없이 빠르고 독립적인 테스트 환경 구성
        fakeDataSource = FakeItemDataSource(initialItems = sampleItems)
        repository = ItemRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `getItems returns all items`() {
        val items = repository.getItems()

        assertThat(items).hasSize(3)
        assertThat(items).isEqualTo(sampleItems)
    }

    @Test
    fun `getItem with existing id returns matching item`() {
        val item = repository.getItem(1L)

        assertThat(item).isNotNull
        assertThat(item?.name).isEqualTo("Keyboard")
    }

    @Test
    fun `getItem with non-existing id returns null`() {
        val item = repository.getItem(999L)

        assertThat(item).isNull()
    }

    @Test
    fun `getItemsByMaxPrice filters items less than or equal to maxPrice and sorts ascending`() {
        val result = repository.getItemsByMaxPrice(50000)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Mouse", "Keyboard")
    }

    @Test
    fun `saveItem updates existing item or adds new item`() {
        val newItem = Item(id = 4L, name = "Headset", price = 70000)
        repository.saveItem(newItem)

        assertThat(repository.getItems()).hasSize(4)
        assertThat(repository.getItem(4L)).isEqualTo(newItem)
    }
}
```
