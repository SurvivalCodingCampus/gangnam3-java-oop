# Repository 작성 원칙 가이드 (Repository Architecture Guide)

본 문서는 프로젝트에서 작성된 `CommentRepository`, `CommentRepositoryImpl`, `FakeCommentDataSource`, `CommentRepositoryTest`의 설계 및 테스트 패턴을 분석하여, 향후 새로운 도메인의 Repository를 추가할 때 일관되게 지켜야 할 **5가지 핵심 원칙과 코드 템플릿**을 정의합니다.

---

## 1. 네이밍 및 패키지 구조 규칙

계층 간의 역할과 책임을 명확히 구분하고, 코드의 일관성과 가독성을 유지하기 위해 엄격한 네이밍 및 패키지 구조를 준수합니다.

### 1-1. 네이밍 규칙

| 계층 / 역할 | 명명 규칙 | 예시 | 설명 |
|---|---|---|---|
| **Repository 인터페이스** | `<Model>Repository` | `ItemRepository` | 순수 도메인 관점의 데이터 접근 계약을 정의합니다. `I` 접두사(예: `IItemRepository`)는 사용하지 않습니다. |
| **Repository 구현체** | `<Model>RepositoryImpl` | `ItemRepositoryImpl` | 인터페이스명 뒤에 `Impl` 접미사를 붙입니다. 특정 저장 기술명이 아닌 비즈니스 계층임을 명시합니다. |
| **DataSource 인터페이스** | `<Model>DataSource` | `ItemDataSource` | 저수준 데이터 입출력(I/O) 계약을 정의합니다. |
| **DataSource 실제 구현체** | `<기술명><Model>DataSource` | `FileItemDataSource`<br>`DbItemDataSource`<br>`NetworkItemDataSource` | 실제 접근하는 물리적 저장 매체/기술명을 접두사로 사용합니다. |
| **DataSource Fake 구현체** | `Fake<Model>DataSource` | `FakeItemDataSource` | 테스트를 위해 In-Memory 컬렉션을 사용하여 DataSource 인터페이스를 모방한 Test Double입니다. |
| **단위 테스트 클래스** | `<대상클래스명>Test` | `ItemRepositoryTest`<br>`FakeItemDataSourceTest` | 테스트 대상 클래스명 뒤에 `Test` 접미사를 붙입니다. |

### 1-2. 패키지 구조 규칙

프로젝트의 패키지는 도메인 모델, 저수준 I/O(DataSource), 고수준 비즈니스 데이터 가공(Repository)으로 명확히 계층화합니다.

```
src/
├── main/kotlin/com/survivalcoding/dayXX/
│   ├── model/
│   │   └── Item.kt                      # 도메인 데이터 모델 (data class, Serializable)
│   ├── datasource/
│   │   ├── inter/                       # DataSource 인터페이스 계층
│   │   │   └── ItemDataSource.kt
│   │   └── impl/                        # DataSource 물리적 I/O 구현체 계층
│   │       └── FileItemDataSource.kt    # (또는 DbItemDataSource, NetworkItemDataSource)
│   └── repository/
│       ├── inter/                       # Repository 인터페이스 계층 (비즈니스 요구사항 정의)
│       │   └── ItemRepository.kt
│       └── impl/                        # Repository 구현체 계층 (DI 주입 및 데이터 가공)
│           └── ItemRepositoryImpl.kt
└── test/kotlin/dayXX/
    ├── datasource/
    │   └── mock/                        # (선택) DataSource Fake 구현체를 test 디렉터리에 배치할 경우
    │       └── FakeItemDataSource.kt
    └── repository/
        └── ItemRepositoryTest.kt        # Repository 비즈니스 로직 단위 테스트
```

> [!NOTE]
> 프로젝트의 컨벤션에 따라 인터페이스와 구현체를 `inter`, `impl` 하위 패키지로 분리하거나, 동일 패키지 내에 두고 이름으로 구분할 수 있습니다. 중요한 것은 **DataSource와 Repository가 물리적 디렉터리 수준에서 분리**되어 있어야 한다는 점입니다.

---

## 2. 의존성 주입(DI) 원칙

> **Repository는 특정 구체 기술(File, DB, Retrofit 등)에 직접 의존하지 않고, 오직 DataSource 인터페이스를 생성자로 주입받아야 한다.**

### 2-1. 의존관계 역전 원칙(DIP, Dependency Inversion Principle) 준수
- 고수준 모듈(Repository)은 저수준 모듈(File, DB 등)의 구현에 직접 의존해서는 안 되며, 둘 다 추상화(DataSource 인터페이스)에 의존해야 합니다.
- Repository 내부에서 `FileItemDataSource()`처럼 구체 클래스를 직접 인스턴스화(`new`)하지 않고, **생성자 주입(Constructor Injection)**을 통해 외부에서 주입받습니다.

### 2-2. 의존성 주입의 이점
1. **저장 기술 변경에 대한 유연성**: 데이터 저장소가 로컬 JSON 파일에서 SQLite DB, 또는 원격 REST API로 변경되더라도 Repository의 코드는 단 한 줄도 수정되지 않습니다.
2. **테스트 격리성**: 실제 파일 시스템이나 네트워크 연결 없이도, 테스트 시 `FakeItemDataSource`를 주입하여 100% 독립적인 순수 단위 테스트를 수행할 수 있습니다.

```kotlin
// ❌ 나쁜 예: 구체 기술에 직접 의존하거나 내부에서 생성 (결합도 극대화, 테스트 불가)
class ItemRepositoryImpl : ItemRepository {
    private val dataSource = FileItemDataSource(Path.of("items.json")) // 강결합 발생!
    
    // ...
}

// ⭕ 좋은 예: DataSource 인터페이스를 생성자로 주입 (느슨한 결합, 유연성 및 테스트 용이성 확보)
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource // 어떤 구현체(File, DB, Fake)든 수용 가능
) : ItemRepository {
    
    // ...
}
```

---

## 3. 비즈니스 로직과 책임 분리 (Separation of Concerns)

> **저장 매체 조작(I/O)은 DataSource에 위임하고, Repository는 데이터 가공(필터링, 정렬, 검색, 비즈니스 규칙)에 집중한다.**

단일 책임 원칙(SRP)에 따라 각 계층이 담당해야 할 역할의 경계를 엄격히 유지합니다.

### 3-1. 계층별 책임 매트릭스

| 계층 | 주 책임 | 반드시 수행해야 할 작업 (DO) | 절대 하지 말아야 할 작업 (DON'T) |
|---|---|---|---|
| **DataSource** | 저수준 데이터 입출력 (Raw I/O) | • 파일 읽기/쓰기 (`Path`, `readText`)<br>• 네트워크 API 호출<br>• JSON/CSV 직렬화 및 역직렬화<br>• 데이터 원본 그대로의 반환 | • 조건별 데이터 필터링/정렬<br>• 도메인 유효성 검증<br>• 비즈니스 예외 처리 및 정책 적용 |
| **Repository** | 도메인 데이터 공급 및 가공 | • 비즈니스 조건 필터링 (`filter`)<br>• 데이터 정렬 (`sortedBy`)<br>• ID 또는 조건 검색 (`find`)<br>• 데이터 통계/집계 및 가공<br>• 도메인 비즈니스 규칙 검증 | • `java.nio.file.Path` 등 파일 직접 조작<br>• `Json.decodeFromString` 등 직렬화 라이브러리 직접 호출<br>• DB 쿼리 문맥 직접 제어 |

### 3-2. 안티패턴과 올바른 예시

```kotlin
// ❌ 나쁜 예: Repository가 파일 I/O 및 직렬화 책임을 직접 떠안은 경우
class ItemRepositoryImpl : ItemRepository {
    private val filePath = Path.of("items.json")

    override fun getItemsByMaxPrice(maxPrice: Int): List<Item> {
        // 저수준 I/O와 역직렬화가 Repository에 침투함
        val json = filePath.readText()
        val items = Json.decodeFromString<List<Item>>(json)
        return items.filter { it.price <= maxPrice }
    }
}

// ⭕ 좋은 예: 저수준 I/O는 DataSource에 위임하고, Repository는 순수 비즈니스 가공에 전념
class ItemRepositoryImpl(
    private val dataSource: ItemDataSource
) : ItemRepository {

    override fun getItemsByMaxPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems()
            .filter { it.price <= maxPrice } // 비즈니스 필터링
            .sortedBy { it.price }           // 비즈니스 정렬
    }
}
```

---

## 4. 테스트 용이성 및 Test Double 원칙

> **순수 JVM 환경에서 빠르고 신뢰성 높은 단위 테스트를 위해 Mock 프레임워크 대신 In-Memory Fake를 우선 사용한다.**

### 4-1. Mock 프레임워크 대비 Fake의 압도적 우위

1. **초고속 실행 속도 (Pure JVM Execution)**
   - Mockito, MockK 등은 리플렉션 및 런타임 CGLIB/바이트코드 조작 오버헤드가 발생하여 테스트 실행 속도가 저하됩니다.
   - Fake는 순수 Kotlin 컬렉션(`MutableList`) 기반 메모리 연산으로 동작하므로 **0~1ms 미만**의 초고속으로 수백 개의 테스트를 수행할 수 있습니다.
2. **상태 검증(State Verification)을 통한 높은 신뢰성**
   - Mock은 호출 반환값(`every { ... } returns ...`)을 하드코딩하고 행위 검증(`verify { ... }`)에 치중하여, 내부 구현이 조금만 바뀌어도 테스트가 쉽게 깨지는 취약점이 있습니다.
   - Fake는 실제 인터페이스 명세에 따라 데이터를 저장하고 조회하는 상태 변화를 완벽히 흉내 내므로 **리팩토링 내성**이 매우 높습니다.
3. **환경 독립성 및 사이드 이펙트 제거**
   - 디스크 I/O 권한 문제, 임시 파일 생성 및 삭제(`@TempDir`, `tearDown`), 네트워크 연결 상태 등에 전혀 영향을 받지 않습니다.

### 4-2. In-Memory Fake 작성 가이드라인

- **초기 데이터 주입 지원**: 기본 파라미터(`initialItems: List<Item> = emptyList()`)를 제공하여, 테스트 셋업 시 필요한 픽스처 데이터를 손쉽게 주입할 수 있게 합니다.
- **방어적 복사 (Defensive Copying)**:
  - 내부 데이터는 `private val items = initialItems.toMutableList()`로 캡슐화합니다.
  - 조회 메서드(`loadItems()`) 호출 시 외부에서 내부 컬렉션을 임의로 조작하지 못하도록 불변 리스트 `items.toList()`로 복사하여 반환합니다.
- **저장/갱신 정책 명확화**: 요구사항에 따라 누적 추가(`addAll`) 또는 대체/갱신 로직을 명확히 구현합니다.

---

## 5. 새로운 Repository 추가 템플릿 예시

향후 새로운 도메인(예: `Item`)의 Repository를 추가할 때는 아래의 완성형 템플릿 구조를 기반으로 작성합니다.

### 5-1. 데이터 모델 (`Item.kt`)
```kotlin
package com.survivalcoding.dayXX.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val price: Int,
    val isAvailable: Boolean = true,
)
```

### 5-2. DataSource 인터페이스 (`ItemDataSource.kt`)
```kotlin
package com.survivalcoding.dayXX.datasource.inter

import com.survivalcoding.dayXX.model.Item

interface ItemDataSource {
    fun loadItems(): List<Item>
    fun saveItems(items: List<Item>)
}
```

### 5-3. In-Memory Fake DataSource (`FakeItemDataSource.kt`)
```kotlin
package com.survivalcoding.dayXX.datasource.mock

import com.survivalcoding.dayXX.datasource.inter.ItemDataSource
import com.survivalcoding.dayXX.model.Item

class FakeItemDataSource(
    initialItems: List<Item> = emptyList(),
) : ItemDataSource {

    private val items = initialItems.toMutableList()

    override fun loadItems(): List<Item> {
        // 방어적 복사를 통해 불변 리스트 반환
        return items.toList()
    }

    override fun saveItems(items: List<Item>) {
        this.items.addAll(items)
    }
}
```

### 5-4. Repository 인터페이스 (`ItemRepository.kt`)
```kotlin
package com.survivalcoding.dayXX.repository.inter

import com.survivalcoding.dayXX.model.Item

interface ItemRepository {
    fun getAllItems(): List<Item>
    fun getItemById(id: Long): Item?
    fun getItemsByMaxPrice(maxPrice: Int): List<Item>
    fun getAvailableItems(): List<Item>
}
```

### 5-5. Repository 구현체 (`ItemRepositoryImpl.kt`)
```kotlin
package com.survivalcoding.dayXX.repository.impl

import com.survivalcoding.dayXX.datasource.inter.ItemDataSource
import com.survivalcoding.dayXX.model.Item
import com.survivalcoding.dayXX.repository.inter.ItemRepository

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource, // DI: DataSource 인터페이스에만 의존
) : ItemRepository {

    override fun getAllItems(): List<Item> {
        return dataSource.loadItems()
    }

    override fun getItemById(id: Long): Item? {
        return dataSource.loadItems()
            .find { it.id == id }
    }

    override fun getItemsByMaxPrice(maxPrice: Int): List<Item> {
        return dataSource.loadItems()
            .filter { it.price <= maxPrice }
            .sortedBy { it.price }
    }

    override fun getAvailableItems(): List<Item> {
        return dataSource.loadItems()
            .filter { it.isAvailable }
    }
}
```

### 5-6. 단위 테스트 코드 (`ItemRepositoryTest.kt`)
```kotlin
@file:Suppress("NonAsciiCharacters", "ClassName")

package dayXX

import com.survivalcoding.dayXX.datasource.mock.FakeItemDataSource
import com.survivalcoding.dayXX.model.Item
import com.survivalcoding.dayXX.repository.impl.ItemRepositoryImpl
import com.survivalcoding.dayXX.repository.inter.ItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ItemRepository 단위 테스트")
class ItemRepositoryTest {

    private lateinit var itemRepository: ItemRepository
    private lateinit var mockItems: List<Item>

    @BeforeEach
    fun setUp() {
        mockItems = listOf(
            Item(id = 1L, name = "노트북", price = 1_500_000, isAvailable = true),
            Item(id = 2L, name = "기계식 키보드", price = 120_000, isAvailable = true),
            Item(id = 3L, name = "무선 마우스", price = 60_000, isAvailable = false),
            Item(id = 4L, name = "모니터", price = 300_000, isAvailable = true)
        )

        // Mock 프레임워크 대신 In-Memory FakeDataSource를 주입
        itemRepository = ItemRepositoryImpl(
            FakeItemDataSource(mockItems)
        )
    }

    @Nested
    @DisplayName("전체 및 단건 조회 테스트")
    inner class FindTest {

        @Test
        fun `전체 아이템 목록을 정상적으로 반환한다`() {
            val items = itemRepository.getAllItems()

            assertThat(items)
                .hasSize(4)
                .containsExactlyElementsOf(mockItems)
        }

        @Test
        fun `존재하는 ID로 조회 시 해당 아이템을 반환한다`() {
            val item = itemRepository.getItemById(1L)

            assertThat(item).isNotNull
            assertThat(item?.name).isEqualTo("노트북")
            assertThat(item?.price).isEqualTo(1_500_000)
        }

        @Test
        fun `존재하지 않는 ID로 조회 시 null을 반환한다`() {
            val item = itemRepository.getItemById(999L)

            assertThat(item).isNull()
        }
    }

    @Nested
    @DisplayName("비즈니스 가공 및 필터링 테스트")
    inner class BusinessFilterTest {

        @Test
        fun `지정된 최대 가격 이하의 아이템 목록을 가격 오름차순으로 정렬하여 반환한다`() {
            // maxPrice = 200,000 이하: 키보드(120,000), 마우스(60,000)
            val filteredItems = itemRepository.getItemsByMaxPrice(200_000)

            assertThat(filteredItems)
                .hasSize(2)
                .extracting("name")
                .containsExactly("무선 마우스", "기계식 키보드") // 가격 오름차순 검증
        }

        @Test
        fun `구매 가능한(isAvailable=true) 아이템만 필터링하여 반환한다`() {
            val availableItems = itemRepository.getAvailableItems()

            assertThat(availableItems)
                .hasSize(3)
                .allMatch { it.isAvailable }
        }

        @Test
        fun `빈 DataSource로 초기화 시 모든 조회는 빈 결과를 반환한다`() {
            val emptyRepository = ItemRepositoryImpl(FakeItemDataSource())

            assertThat(emptyRepository.getAllItems()).isEmpty()
            assertThat(emptyRepository.getItemById(1L)).isNull()
            assertThat(emptyRepository.getItemsByMaxPrice(100_000)).isEmpty()
            assertThat(emptyRepository.getAvailableItems()).isEmpty()
        }
    }
}
```

---

## 6. Repository 작성 자가 체크리스트

새로운 Repository를 구현한 후 코드 리뷰 전 다음 항목을 자체 점검합니다.

- [ ] **패키지 분리**: Repository와 DataSource가 명확한 패키지로 격리되어 있는가?
- [ ] **네이밍 규칙**: 인터페이스(`~Repository`), 구현체(`~RepositoryImpl`), Fake(`Fake~DataSource`) 네이밍을 준수했는가?
- [ ] **DIP 준수**: Repository는 구체 클래스가 아닌 `~DataSource` 인터페이스를 생성자로 주입받고 있는가?
- [ ] **책임 분리**: Repository 내에 파일 I/O, 직렬화/역직렬화(Json 등) 저수준 코드가 포함되어 있지 않은가?
- [ ] **도메인 가공**: 정렬, 필터링, 조건 검색 등 비즈니스 로직이 Repository 계층에 충실히 구현되었는가?
- [ ] **Fake 구현**: Mock 프레임워크 대신 `Fake~DataSource`를 활용하여 순수 JVM 단위 테스트를 작성했는가?
- [ ] **불변성 보장**: Fake DataSource는 `toList()`를 통해 내부 리스트의 방어적 복사를 수행하고 있는가?
