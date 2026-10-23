# Repository 작성 원칙

`day15` 의 `ItemRepository` / `ItemRepositoryImpl`, `day16_repository` 의 `UserRepository` / `UserRepositoryImpl` / `FakeUserDataSource` / `UserRepositoryTest` 를 기준으로 정리한, 새로운 Repository 를 만들 때 지켜야 할 필수 원칙이다.
DataSource 쪽 규칙은 [datasource.md](datasource.md) 를 따른다. 이 문서는 그 위에 올라가는 Repository 계층만 다룬다.

> **한 줄 요약**
> DataSource 는 "읽고 쓴다", Repository 는 "가공한다", 테스트는 "Fake 로 바꿔 끼운다".

---

## 0. 현재 코드 분석 결과

문서를 쓰기 전에 기준 코드를 점검한 결과다. 아래 원칙들은 이 문제들을 다시 만들지 않기 위한 것이다.

| 파일 | 상태 | 발견한 문제 |
|---|---|---|
| `day15/data_source/repository/ItemRepository.kt` | 전체 주석 처리 | `import` 가 없다. `repository/` 가 `data_source/` 아래에 들어가 있다. |
| `day15/data_source/repository/ItemRepositoryImpl.kt` | 전체 주석 처리 | 인터페이스와 시그니처가 다르다. `getItem(id: Long)` ↔ `getItems(id: String)`, `deleteItem(item: Item)` ↔ `deleteItem(id: String)`. `searchByNameDesc` 가 구현되지 않았다. |
| `day16_repository/repository/UserRepository*.kt` | 정상 | 없음. 생성자 주입, 인터페이스 타입 선언, 정렬·take 가공 모두 원칙대로다. |
| `src/test/kotlin/day16/UserRepositoryTest.kt` | 컴파일 불가 | ① 테스트 클래스가 `: UserDataSource` 를 상속한다. ② `org.junit.Before` (JUnit 4) 를 쓰는데 의존성은 JUnit Jupiter 뿐이다. ③ `User` 에 없는 `username` 필드를 쓴다. ④ `FakeUserDataSource` 가 테스트 파일 안에 같이 들어 있다. |
| `FakeItemDataSource`, `ItemRepositoryTest` | 없음 | 저장소 어디에도 없다. 5장의 템플릿이 그 역할을 한다. |

---

## 1. 네이밍 및 패키지 구조 규칙

### 1-1. 클래스 이름

| 역할 | 규칙 | 예시 |
|---|---|---|
| Repository 인터페이스 | `<모델명>Repository` | `ItemRepository`, `UserRepository` |
| Repository 구현체 | `<모델명>RepositoryImpl` | `ItemRepositoryImpl`, `UserRepositoryImpl` |
| DataSource 인터페이스 | `<모델명>DataSource` | `ItemDataSource` |
| DataSource 실제 구현체 | `<기술명><모델명>DataSource` | `FileItemDataSource`, `CsvStockDataSource` |
| DataSource 테스트 더블 | `Fake<모델명>DataSource` | `FakeItemDataSource`, `FakeUserDataSource` |
| 테스트 클래스 | `<구현체가 아닌 인터페이스명>Test` | `ItemRepositoryTest` (`ItemRepositoryImplTest` 가 아니다) |

- Repository 구현체는 기술명을 붙이지 않는다. Repository 는 기술을 모르기 때문에 `FileItemRepository` 같은 이름은 그 자체로 원칙 위반이다.
- 구현체가 하나뿐이라도 인터페이스를 만든다. 테스트에서 타입 선언을 인터페이스로 하기 위해서다.
- 테스트 클래스 이름은 인터페이스를 따른다. 테스트가 검증하는 것은 "Repository 의 약속" 이지 구현체의 내부가 아니다.

### 1-2. 메서드 이름

| 동작 | 이름 패턴 | 예시 |
|---|---|---|
| 전체 조회 | `get<모델명 복수>()` | `getItems()`, `getUsers()` |
| 단건 조회 | `get<모델명>(id)` → `모델?` | `getItem(id: Long): Item?` |
| 조건 조회 | `get<모델명 복수><조건>()` | `getItemsUnderPrice(maxPrice: Int)`, `getUsersTop10ByUserName()` |
| 검색 | `searchBy<필드>(query)` | `searchByName(query: String)` |
| 추가 | `add<모델명>(model)` | `addItem(item: Item)` |
| 삭제 | `delete<모델명>(id)` | `deleteItem(id: Long)` |

- 단건 조회는 `get<단수>`, 전체 조회는 `get<복수>` 다. day15 처럼 `getItems(id)` 와 `getItems()` 를 섞어 쓰지 않는다.
- `id` 파라미터 타입은 **모델의 `id` 타입과 똑같이** 쓴다. `Item.id` 가 `Long` 이면 `getItem(id: Long)`, `deleteItem(id: Long)` 이다. 모델의 id 타입을 바꾸면 Repository 의 모든 id 파라미터도 함께 바꾼다.
- 삭제는 모델 전체가 아니라 `id` 로 받는다. 호출하는 쪽이 모델 객체를 통째로 들고 있을 이유가 없다.
- 인터페이스에 쓴 메서드는 전부 구현체에 있어야 한다. 구현 안 할 메서드는 인터페이스에 적지 않는다.

### 1-3. 패키지 구조

```
src/main/java/com/survivalcoding/<dayXX>/
├── model/
│   └── Item.kt                     // @Serializable data class
├── datasource/
│   ├── ItemDataSource.kt           // 인터페이스
│   └── FileItemDataSource.kt       // 기술 구현체
└── repository/
    ├── ItemRepository.kt           // 인터페이스
    └── ItemRepositoryImpl.kt       // 구현체

src/test/kotlin/com/survivalcoding/<dayXX>/
├── datasource/
│   └── FakeItemDataSource.kt       // 테스트 더블 (테스트 소스셋에만 존재)
└── repository/
    └── ItemRepositoryTest.kt
```

- `repository/` 와 `datasource/` 는 **형제 패키지** 다. day15 처럼 `data_source/repository/` 로 중첩하지 않는다. Repository 는 DataSource 의 하위 개념이 아니라 DataSource 를 사용하는 상위 계층이다.
- 패키지 이름은 `datasource` 로 쓴다. 현재 day15 · day16 코드는 `data_source` 로 되어 있다. 새로 만드는 것부터 `datasource` 로 쓰고, 기존 것은 리네임한다.
- Fake 는 `src/main` 이 아니라 `src/test` 에 둔다. 배포 코드에 테스트 더블이 섞이지 않는다.
- 테스트 패키지는 메인 패키지를 그대로 따라간다. 현재 `UserRepositoryTest` 의 패키지 `day16` 은 메인의 `com.survivalcoding.day16_repository` 와 달라서 `internal` 접근도 안 되고 파일 위치만 봐서는 대상 코드를 찾기 어렵다.

---

## 2. 의존성 주입(DI) 원칙

> **Repository 는 DataSource 인터페이스 하나만 생성자로 받는다. 그 외에는 아무것도 모른다.**

### 2-1. 생성자 주입

```kotlin
// 좋은 예 (day16 UserRepositoryImpl 그대로)
class UserRepositoryImpl(
    private val dataSource: UserDataSource,     // 인터페이스 타입
) : UserRepository { ... }
```

```kotlin
// 나쁜 예
class ItemRepositoryImpl : ItemRepository {
    private val dataSource = FileItemDataSource()        // ① 내부에서 직접 생성. 테스트에서 못 바꾼다
}

class ItemRepositoryImpl(
    private val dataSource: FileItemDataSource,          // ② 구현체 타입으로 받음. Fake 를 못 넣는다
) : ItemRepository

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource = FileItemDataSource(),   // ③ 기본값으로 구현체를 숨김. 결국 File 에 의존
) : ItemRepository

class ItemRepositoryImpl(
    private val file: File,                              // ④ 기술 타입을 직접 받음
    private val json: Json,
) : ItemRepository
```

- 생성자 파라미터는 `private val dataSource: <모델명>DataSource` 한 줄이다. 기본값을 주지 않는다.
- DataSource 가 두 개 이상 필요하면 (예: 로컬 + 원격) 각각 인터페이스 타입으로 받는다. 그래도 기술 타입은 받지 않는다.

### 2-2. import 로 확인하는 방법

Repository 파일(인터페이스 · 구현체)의 `import` 에는 다음 두 종류만 있어야 한다.

```kotlin
import com.survivalcoding.day15.datasource.ItemDataSource   // 구현체 파일에만
import com.survivalcoding.day15.model.Item
```

아래가 보이면 책임이 잘못 들어온 것이다.

| import | 의미 |
|---|---|
| `java.io.File`, `java.nio.file.*` | 파일 조작이 Repository 로 올라왔다 |
| `kotlinx.serialization.*`, `Json` | 파싱이 Repository 로 올라왔다 |
| `java.net.http.*`, `retrofit2.*` | 통신이 Repository 로 올라왔다 |
| `java.sql.*` | DB 가 Repository 로 올라왔다 |
| `FileItemDataSource` 등 구현체 이름 | 구현체에 묶였다 |

### 2-3. 조립은 사용하는 쪽(`main`)에서

```kotlin
fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()      // 구현체 선택은 여기서만
    val repository: ItemRepository = ItemRepositoryImpl(dataSource)

    repository.getItems().forEach(::println)
}
```

- 변수는 **인터페이스 타입으로 선언** 한다. `val repository = ItemRepositoryImpl(...)` 처럼 추론에 맡기면 구현체에 묶인다.
- 저장 매체를 바꿀 때 바뀌는 줄은 `main` 의 첫 줄 하나뿐이어야 한다. Repository 파일을 열게 되면 어딘가 의존성이 새고 있는 것이다.

---

## 3. 비즈니스 로직과 책임 분리

> **저장 매체를 만지는 일은 전부 DataSource 에, 데이터를 가공하는 일은 전부 Repository 에.**

### 3-1. 책임 표

| 할 일 | DataSource | Repository |
|---|---|---|
| 파일 · DB · 네트워크 읽기/쓰기 | O | X |
| JSON · CSV 파싱, 타입 변환, 빈 줄 제거 | O | X |
| 전체 목록 그대로 돌려주기 (`loadItems`, `saveItems`) | O | 위임만 |
| 정렬 (`sortedBy`) | X | O |
| 필터링 (`filter`, `filterNot`) | X | O |
| 검색 (`contains`, `ignoreCase`) | X | O |
| 단건 찾기 (`find`) | X | O |
| 개수 제한, 상위 N (`take`) | X | O |
| 추가 · 삭제 후 저장 (읽기 → 바꾸기 → 쓰기) | 쓰기만 | 흐름 전체 |
| 비즈니스 규칙 (가격 상한, 중복 id 금지 등) | X | O |

- DataSource 인터페이스는 `load` / `save` 두 메서드로 충분하다. `loadItemsSortedByName()` 같은 메서드가 DataSource 에 생기면 가공이 아래로 내려간 것이다.
- 반대로 Repository 가 `file.readText()` 를 하면 저장 매체 조작이 위로 올라온 것이다.

### 3-2. 읽기 메서드 패턴

```kotlin
override fun getItems(): List<Item> =
    dataSource.loadItems().sortedBy { it.name }                  // 위임 + 가공

override fun getItem(id: Long): Item? =
    dataSource.loadItems().find { it.id == id }                  // 없으면 null, 예외 아님

override fun getItemsUnderPrice(maxPrice: Int): List<Item> =
    dataSource.loadItems().filter { it.price <= maxPrice }

override fun searchByName(query: String): List<Item> =
    dataSource.loadItems().filter { it.name.contains(query, ignoreCase = true) }
```

- 매 호출마다 `dataSource.load...()` 로 시작한다. Repository 는 상태를 들고 있지 않는다. (캐시가 필요해지면 그때 별도 규칙으로 정한다.)
- 단건 조회는 `?` 를 붙여 `null` 을 돌려준다. `first()` 로 예외를 던지지 않는다.
- 반환 타입은 `List`, 절대 `MutableList` 가 아니다. (datasource.md 3장과 같은 이유다.)
- `ignoreCase = true` 같은 "검색은 대소문자를 가리지 않는다" 류의 결정이 **비즈니스 규칙** 이고, 이것이 Repository 에 있어야 할 코드다.

### 3-3. 쓰기 메서드 패턴

```kotlin
override fun addItem(item: Item) {
    val current = dataSource.loadItems()               // 1. 읽고
    dataSource.saveItems(current + item)               // 2. 바꾼 뒤  3. 통째로 저장
}

override fun deleteItem(id: Long) {
    val current = dataSource.loadItems()
    dataSource.saveItems(current.filterNot { it.id == id })
}
```

- 쓰기는 항상 **읽기 → 바꾸기 → 전체 저장** 세 단계다. DataSource 에 `appendItem`, `deleteById` 를 추가하지 않는다. 그 순간 저장 매체마다 구현이 갈라진다.
- `toMutableList()` + `add()` 보다 `current + item` 같은 불변 연산을 우선한다. 가변 리스트가 필요한 경우에도 메서드 밖으로 나가지 않게 한다.
- 중복 id 금지, 가격 음수 금지 같은 규칙을 넣을 자리는 "바꾸기" 단계다.

---

## 4. 테스트 용이성 및 Test Double 원칙

> **Mock 라이브러리 대신 Fake 를 쓴다. Fake 는 인터페이스를 구현한 In-Memory 컬렉션이다.**

### 4-1. 왜 Mock 이 아니라 Fake 인가

| | Mock (Mockito, MockK) | Fake (직접 작성) |
|---|---|---|
| 추가 의존성 | 필요 | 없음. `build.gradle.kts` 에는 JUnit Jupiter 만 있다 |
| 동작 | "이 메서드가 호출되면 이걸 돌려줘" 를 테스트마다 지정 | 진짜 저장소처럼 넣은 대로 나온다 |
| 검증하는 것 | 호출 여부 · 횟수 (구현 방식에 묶임) | 결과 값 (구현을 바꿔도 테스트가 살아남는다) |
| 읽기 쉬움 | `every { } returns` 문법을 알아야 함 | 평범한 Kotlin 클래스 |
| 속도 | 빠름 | 빠름. 파일 I/O 가 없다 |

- Repository 테스트의 목적은 "정렬·필터·검색이 맞는가" 다. `loadItems()` 가 몇 번 불렸는지는 관심사가 아니다. 그래서 Fake 로 충분하다.
- 파일을 실제로 만들어 테스트하지 않는다. 테스트 순서에 따라 결과가 달라지고, 작업 디렉터리에 찌꺼기 파일이 남는다.

### 4-2. Fake 작성 규칙

```kotlin
// src/test/kotlin/.../datasource/FakeItemDataSource.kt
class FakeItemDataSource(
    initial: List<Item> = emptyList(),
) : ItemDataSource {

    private var items: List<Item> = initial

    override fun loadItems(): List<Item> = items

    override fun saveItems(items: List<Item>) {
        this.items = items
    }
}
```

1. DataSource **인터페이스를 구현** 한다. Repository 가 받는 타입이 인터페이스이므로 그대로 꽂힌다.
2. 상태는 `private var <복수형>: List<모델>` 하나다. `load` 는 그대로 돌려주고 `save` 는 통째로 바꾼다. 이보다 복잡하면 Fake 가 아니라 또 하나의 구현체다.
3. 초기 데이터는 생성자로 받거나, 테스트의 `setUp` 에서 `save...()` 로 넣는다.
4. 파일 · 네트워크 · 외부 라이브러리를 쓰지 않는다. import 는 모델과 인터페이스뿐이다.
5. **자기 파일** 에 둔다. 테스트 클래스 파일 안에 같이 넣으면 다른 테스트가 재사용할 수 없다. (현재 `FakeUserDataSource` 가 `UserRepositoryTest.kt` 안에 있다.)
6. 검증용 메서드가 필요하면 `loadItems()` 를 그대로 쓴다. `getSavedCount()` 같은 메서드를 Fake 에 추가하지 않는다.

### 4-3. 테스트 작성 규칙

```kotlin
class ItemRepositoryTest {                                  // ① 아무것도 상속하지 않는다

    private lateinit var fakeDataSource: FakeItemDataSource
    private lateinit var repository: ItemRepository         // ② 인터페이스 타입

    @BeforeEach                                             // ③ JUnit 5 (org.junit.jupiter.api)
    fun setUp() {
        fakeDataSource = FakeItemDataSource()
        fakeDataSource.saveItems(listOf(...))               // ④ 모델에 실제로 있는 필드만
        repository = ItemRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `getItems 는 이름 오름차순으로 정렬한다`() { ... }   // ⑤ 한글 백틱 이름: "메서드 는 무엇 한다"
}
```

| 번호 | 규칙 | 현재 `UserRepositoryTest` 의 위반 |
|---|---|---|
| ① | 테스트 클래스는 어떤 인터페이스도 구현하지 않는다 | `class UserRepositoryTest : UserDataSource` → 추상 메서드 미구현으로 컴파일 실패 |
| ② | Repository 변수는 인터페이스 타입으로 선언한다 | 지켰다 (`private lateinit var repository: UserRepository`) |
| ③ | `org.junit.jupiter.api.BeforeEach` / `Test` / `Assertions` 만 쓴다 | `import org.junit.Before` → JUnit 4 는 의존성에 없다 |
| ④ | 테스트 데이터는 모델의 실제 필드로만 만든다 | `User(username = ...)` → `User` 에는 `postId, id, name, email, body` 뿐 |
| ⑤ | 테스트 이름은 ``메서드명 은/는 기대동작``. 하나의 테스트는 하나의 동작 | 지켰다 |

추가 규칙:

- **Given / When / Then** 세 블록으로 쓴다. `setUp` 이 Given, 테스트 본문 첫 줄이 When, `assert` 가 Then 이다.
- 쓰기 메서드(`add`, `delete`)는 **Repository 로 다시 읽어서** 확인한다. `fakeDataSource.loadItems()` 로 직접 봐도 되지만, Repository 의 읽기 경로를 함께 검증하는 쪽이 낫다.
- 모든 공개 메서드마다 최소 한 개, 조건이 있는 메서드(`getItem`, `searchByName`)는 "있을 때 / 없을 때" 두 개를 쓴다.
- 테스트는 서로 독립이다. `@BeforeEach` 가 매번 새 Fake 를 만들기 때문에 한 테스트의 `add` 가 다른 테스트에 남지 않는다.
- 실행: `./gradlew test`. IDE 에서 `@Test` 옆 화살표로 하나씩 돌려도 된다.

---

## 5. 새로운 Repository 추가 템플릿

새 Repository 를 만들 때는 다음 순서를 따른다.

1. `model/` 모델, `datasource/` 인터페이스 + 구현체는 [datasource.md 4장](datasource.md) 대로 먼저 만든다.
2. `repository/<모델명>Repository.kt` 인터페이스를 만든다. import 는 모델만.
3. `repository/<모델명>RepositoryImpl.kt` 구현체를 만든다. 생성자는 DataSource 인터페이스 하나.
4. `src/test/.../datasource/Fake<모델명>DataSource.kt` 를 만든다.
5. `src/test/.../repository/<모델명>RepositoryTest.kt` 를 만들고 `./gradlew test` 가 통과하는지 확인한다.
6. `Main.kt` 에서 인터페이스 타입으로 조립한다.

아래는 `Item` 기준 전체 세트다. 모델명만 바꾸면 그대로 쓸 수 있다.

### 5-1. 모델과 DataSource (기존 코드, 참고용)

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
    fun getItems(): List<Item>                          // 이름 오름차순
    fun getItem(id: Long): Item?                        // 없으면 null
    fun getItemsUnderPrice(maxPrice: Int): List<Item>   // price <= maxPrice
    fun searchByName(query: String): List<Item>         // 대소문자 무시, 이름 오름차순
    fun searchByNameDesc(query: String): List<Item>     // 대소문자 무시, 이름 내림차순
    fun addItem(item: Item)
    fun deleteItem(id: Long)
}
```

- 메서드 옆 주석으로 **비즈니스 규칙** 을 한 줄씩 적는다. 테스트 이름이 여기서 나온다.

### 5-3. Repository 구현체

```kotlin
// repository/ItemRepositoryImpl.kt
package com.survivalcoding.day15.repository

import com.survivalcoding.day15.datasource.ItemDataSource
import com.survivalcoding.day15.model.Item

class ItemRepositoryImpl(
    private val dataSource: ItemDataSource,
) : ItemRepository {

    override fun getItems(): List<Item> =
        dataSource.loadItems().sortedBy { it.name }

    override fun getItem(id: Long): Item? =
        dataSource.loadItems().find { it.id == id }

    override fun getItemsUnderPrice(maxPrice: Int): List<Item> =
        dataSource.loadItems().filter { it.price <= maxPrice }

    override fun searchByName(query: String): List<Item> =
        dataSource.loadItems()
            .filter { it.name.contains(query, ignoreCase = true) }
            .sortedBy { it.name }

    override fun searchByNameDesc(query: String): List<Item> =
        dataSource.loadItems()
            .filter { it.name.contains(query, ignoreCase = true) }
            .sortedByDescending { it.name }

    override fun addItem(item: Item) {
        val current = dataSource.loadItems()
        dataSource.saveItems(current + item)
    }

    override fun deleteItem(id: Long) {
        val current = dataSource.loadItems()
        dataSource.saveItems(current.filterNot { it.id == id })
    }
}
```

### 5-4. Fake DataSource

```kotlin
// src/test/kotlin/com/survivalcoding/day15/datasource/FakeItemDataSource.kt
package com.survivalcoding.day15.datasource

import com.survivalcoding.day15.model.Item

class FakeItemDataSource(
    initial: List<Item> = emptyList(),
) : ItemDataSource {

    private var items: List<Item> = initial

    override fun loadItems(): List<Item> = items

    override fun saveItems(items: List<Item>) {
        this.items = items
    }
}
```

### 5-5. 테스트

```kotlin
// src/test/kotlin/com/survivalcoding/day15/repository/ItemRepositoryTest.kt
package com.survivalcoding.day15.repository

import com.survivalcoding.day15.datasource.FakeItemDataSource
import com.survivalcoding.day15.model.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemRepositoryTest {

    private lateinit var fakeDataSource: FakeItemDataSource
    private lateinit var repository: ItemRepository

    @BeforeEach
    fun setUp() {
        // Given: 가짜 DataSource 에 초기 데이터를 넣고
        fakeDataSource = FakeItemDataSource()
        fakeDataSource.saveItems(
            listOf(
                Item(id = 1L, name = "Keyboard", price = 30_000),
                Item(id = 2L, name = "apple", price = 1_000),
                Item(id = 3L, name = "Monitor", price = 250_000),
                Item(id = 4L, name = "Apple Watch", price = 400_000),
            )
        )
        // Given: 그 DataSource 를 주입해 Repository 를 만든다
        repository = ItemRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `getItems 는 이름 오름차순으로 정렬한다`() {
        val items = repository.getItems()

        assertEquals(4, items.size)
        assertEquals(listOf("Apple Watch", "Keyboard", "Monitor", "apple"), items.map { it.name })
    }

    @Test
    fun `getItem 은 id 가 일치하는 항목을 돌려준다`() {
        val item = repository.getItem(2L)

        assertEquals("apple", item?.name)
    }

    @Test
    fun `getItem 은 없는 id 면 null 을 돌려준다`() {
        assertNull(repository.getItem(999L))
    }

    @Test
    fun `getItemsUnderPrice 는 상한 이하만 돌려준다`() {
        val items = repository.getItemsUnderPrice(30_000)

        assertEquals(setOf(1L, 2L), items.map { it.id }.toSet())
    }

    @Test
    fun `searchByName 은 대소문자를 무시하고 오름차순으로 돌려준다`() {
        val items = repository.searchByName("apple")

        assertEquals(listOf("Apple Watch", "apple"), items.map { it.name })
    }

    @Test
    fun `searchByNameDesc 는 대소문자를 무시하고 내림차순으로 돌려준다`() {
        val items = repository.searchByNameDesc("apple")

        assertEquals(listOf("apple", "Apple Watch"), items.map { it.name })
    }

    @Test
    fun `searchByName 은 일치하는 것이 없으면 빈 리스트를 돌려준다`() {
        assertEquals(emptyList<Item>(), repository.searchByName("zzz"))
    }

    @Test
    fun `addItem 은 저장 후 다시 읽었을 때 포함된다`() {
        repository.addItem(Item(id = 5L, name = "Mouse", price = 20_000))

        assertEquals(5, repository.getItems().size)
        assertEquals("Mouse", repository.getItem(5L)?.name)
    }

    @Test
    fun `deleteItem 은 해당 id 만 제거한다`() {
        repository.deleteItem(1L)

        assertEquals(3, repository.getItems().size)
        assertNull(repository.getItem(1L))
    }
}
```

### 5-6. 사용처

```kotlin
// Main.kt
package com.survivalcoding.day15

import com.survivalcoding.day15.datasource.FileItemDataSource
import com.survivalcoding.day15.datasource.ItemDataSource
import com.survivalcoding.day15.model.Item
import com.survivalcoding.day15.repository.ItemRepository
import com.survivalcoding.day15.repository.ItemRepositoryImpl

fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()
    val repository: ItemRepository = ItemRepositoryImpl(dataSource)

    repository.addItem(Item(id = 1L, name = "Item 1", price = 1000))
    repository.addItem(Item(id = 2L, name = "Item 2", price = 2000))

    repository.getItems().forEach(::println)
}
```

---

## 6. 커밋 전 체크리스트

- [ ] 이름이 `<모델명>Repository` / `<모델명>RepositoryImpl` / `Fake<모델명>DataSource` 인가
- [ ] `repository/` 와 `datasource/` 가 형제 패키지인가. Fake 는 `src/test` 에 있는가
- [ ] 인터페이스의 모든 메서드가 구현체에 있고, 파라미터 타입(특히 `id`)이 모델과 같은가
- [ ] Repository 생성자가 `DataSource` 인터페이스 하나만 받고 기본값이 없는가
- [ ] Repository 파일의 import 에 `File`, `Json`, `Http`, `sql`, 구현체 이름이 없는가
- [ ] 정렬 · 필터 · 검색 · 비즈니스 규칙이 전부 Repository 에, 읽기 · 쓰기가 전부 DataSource 에 있는가
- [ ] 쓰기 메서드가 "읽기 → 바꾸기 → 전체 저장" 패턴인가
- [ ] 테스트 클래스가 아무것도 상속하지 않고, `org.junit.jupiter.api` 만 쓰는가
- [ ] 테스트 데이터가 모델의 실제 필드로만 만들어졌는가
- [ ] 공개 메서드마다 테스트가 있고 `./gradlew test` 가 통과하는가
