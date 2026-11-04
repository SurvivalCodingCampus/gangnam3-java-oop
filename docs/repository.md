# Repository 작성 원칙

로컬 파일 실습의 `ItemRepository` / `ItemRepositoryImpl`, `UserRepository` / `UserRepositoryImpl` / `FakeUserDataSource` / `UserRepositoryTest` 와
원격 API 실습(포켓몬)의 `PokemonRepository` / `PokemonRepositoryImpl` / `FakePokemonDataSource` / `PokemonRepositoryImplTest` 를 기준으로 정리한,
새로운 Repository 를 만들 때 지켜야 할 필수 원칙이다.
DataSource 쪽 규칙은 [datasource.md](datasource.md), DTO 쪽 규칙은 [dto.md](dto.md), DTO → 모델 변환 규칙은 [mapper.md](mapper.md),
테스트 클래스 쪽 규칙은 [test.md](test.md) 를 따른다. 이 문서는 그 위에 올라가는 Repository 계층만 다룬다.
코드 발췌는 원본 그대로이되, `package` · `import` 줄의 수업 날짜 패키지명만 `<dayXX>` 로 바꿔 적었다.
// 문서 참조는 카테고라이즈화 한다. 예) DataSource

> **한 줄 요약**
> DataSource 는 "읽고 쓴다", Repository 는 "가공한다", 테스트는 "Fake 로 바꿔 끼운다".
> 원격 API 가 들어오면 한 줄 더 — DataSource 는 "DTO 까지만", Repository 는 "DTO 를 모델로 바꿔서 가공한다".

---

## 0. 현재 코드 분석 결과

문서를 쓰기 전에 기준 코드를 점검한 결과다. 아래 원칙들은 이 문제들을 다시 만들지 않기 위한 것이다.

| 파일                                                   | 실습         | 상태           | 발견한 문제                                                                                                                                                                                                                   |
|--------------------------------------------------------|--------------|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ItemRepository.kt`                                    | 로컬 파일    | 전체 주석 처리 | `import` 가 없다. `repository` 패키지가 `datasource` 패키지 아래에 들어가 있다.                                                                                                                                               |
| `ItemRepositoryImpl.kt`                                | 로컬 파일    | 전체 주석 처리 | 인터페이스와 시그니처가 다르다. `getItem(id: Long)` ↔ `getItems(id: String)`, `deleteItem(item: Item)` ↔ `deleteItem(id: String)`. `searchByNameDesc` 가 구현되지 않았다.                                                     |
| `UserRepository.kt` / `UserRepositoryImpl.kt`          | 로컬 파일    | 정상           | 없음. 생성자 주입, 인터페이스 타입 선언, 정렬·take 가공 모두 원칙대로다.                                                                                                                                                      |
| `UserRepositoryTest.kt`                                | 로컬 파일    | 컴파일 불가    | ① 테스트 클래스가 `: UserDataSource` 를 상속한다. ② `org.junit.Before` (JUnit 4) 를 쓰는데 의존성은 JUnit Jupiter 뿐이다. ③ `User` 에 없는 `username` 필드를 쓴다. ④ `FakeUserDataSource` 가 테스트 파일 안에 같이 들어 있다. |
| `FakeItemDataSource`, `ItemRepositoryImplTest`         | 로컬 파일    | 없음           | 저장소 어디에도 없다. 5장의 템플릿이 그 역할을 한다.                                                                                                                                                                          |
| `PokemonRepository.kt` / `PokemonRepositoryImpl.kt`    | 원격 API     | 정상           | 없음. `suspend`, 생성자 주입, `Response.body ?: return null`, `dto.toModel()`, `try/catch → null` 전부 원칙대로다. (5-7)                                                                                                      |
| `FakePokemonDataSource.kt` / `PokemonRepositoryImplTest.kt` | 원격 API | 정상           | 없음. Fake 는 저장해 둔 `jsonString` 을 DTO 로 decode 해 돌려주고, 테스트는 `runTest` 로 돈다. 테스트 파일에 안 쓰는 import 4줄과 주석 처리된 줄 1개가 남아 있다. (4-4)                                                      |

---

## 1. 네이밍 및 패키지 구조 규칙

### 1-1. 클래스 이름

| 역할                  | 규칙                     | 예시                                                               |
|-----------------------|--------------------------|--------------------------------------------------------------------|
| Repository 인터페이스 | `<모델명>Repository`     | `ItemRepository`, `UserRepository`, `PokemonRepository`            |
| Repository 구현체     | `<모델명>RepositoryImpl` | `ItemRepositoryImpl`, `UserRepositoryImpl`, `PokemonRepositoryImpl` |

- DataSource 인터페이스 · 구현체 · Fake 의 이름 규칙은 [datasource.md](datasource.md) 의 네이밍 규칙을 따른다.
- 테스트 클래스의 이름 규칙은 [test.md](test.md) 의 네이밍 규칙을 따른다. 구현체를 검증하므로 `<구현체명>Test` — `PokemonRepositoryImplTest` 다.
- Repository 구현체는 기술명을 붙이지 않는다. Repository 는 기술을 모르기 때문에 `FileItemRepository`, `RemotePokemonRepository` 같은 이름은 그 자체로 원칙 위반이다.
- 구현체가 하나뿐이라도 인터페이스를 만든다. 테스트에서 타입 선언을 인터페이스로 하기 위해서다.

### 1-2. 메서드 이름

| 동작                 | 이름 패턴                          | 예시                                                             |
|----------------------|------------------------------------|------------------------------------------------------------------|
| 전체 조회            | `get<모델명 복수>()`               | `getItems()`, `getUsers()`                                       |
| 단건 조회            | `get<모델명>(id)` → `모델?`        | `getItem(id: Long): Item?`                                       |
| 단건 조회 (원격 API) | `suspend fun get<모델명>(키)` → `모델?` | `suspend fun getPokemon(name: String): Pokemon?`            |
| 조건 조회            | `get<모델명 복수><조건>()`         | `getItemsUnderPrice(maxPrice: Int)`, `getUsersTop10ByUserName()` |
| 검색                 | `searchBy<필드>(query)`            | `searchByName(query: String)`                                    |
| 추가                 | `add<모델명>(model)`               | `addItem(item: Item)`                                            |
| 삭제                 | `delete<모델명>(id)`               | `deleteItem(id: Long)`                                           |

- 단건 조회는 `get<단수>`, 전체 조회는 `get<복수>` 다. 로컬 파일 실습의 옛 코드처럼 `getItems(id)` 와 `getItems()` 를 섞어 쓰지 않는다.
- `id` 파라미터 타입은 **모델의 `id` 타입과 똑같이** 쓴다. `Item.id` 가 `Long` 이면 `getItem(id: Long)`, `deleteItem(id: Long)` 이다. 모델의 id
  타입을 바꾸면 Repository 의 모든 id 파라미터도 함께 바꾼다.
- 단건 조회의 키가 꼭 `id` 일 필요는 없다. 포켓몬 API 는 `/pokemon/{name}` 으로 조회하므로 `getPokemon(name: String)` 이다. **DataSource 가 받는 키와 같은 키** 를 쓴다.
- DataSource 메서드가 `suspend` 면 그것을 부르는 Repository 메서드도 `suspend` 다. 인터페이스와 구현체 양쪽에 붙인다.
- 삭제는 모델 전체가 아니라 `id` 로 받는다. 호출하는 쪽이 모델 객체를 통째로 들고 있을 이유가 없다.
- 인터페이스에 쓴 메서드는 전부 구현체에 있어야 한다. 구현 안 할 메서드는 인터페이스에 적지 않는다.

### 1-3. 패키지 구조

```
<dayXX>  (main)
├── model/
│   ├── Item.kt                     // 로컬 파일 실습: @Serializable data class
│   └── Pokemon.kt                  // 원격 API 실습: 순수 코틀린               → dto.md 0-1
├── dto/
│   └── PokemonDto.kt               // 원격 API 실습에만 있다                    → dto.md
├── mapper/
│   └── PokemonMapper.kt            // 원격 API 실습에만 있다                    → mapper.md
├── datasource/
│   ├── ItemDataSource.kt           // 인터페이스
│   ├── FileItemDataSource.kt       // 기술 구현체
│   ├── PokemonDataSource.kt
│   ├── RemotePokemonDataSource.kt
│   └── Response.kt                                                              → datasource.md
└── repository/
    ├── ItemRepository.kt           // 인터페이스
    ├── ItemRepositoryImpl.kt       // 구현체
    ├── PokemonRepository.kt
    └── PokemonRepositoryImpl.kt

<dayXX>  (test)
├── datasource/
│   ├── FakeItemDataSource.kt       // 테스트 더블 (테스트 소스셋에만 존재)
│   ├── FakePokemonDataSource.kt    // jsonString + Fake
│   └── Mocks.kt                    // Ktor MockEngine (DataSource 테스트용)     → test.md
├── mapper/
│   └── ToModelTest.kt                                                           → mapper.md 5장
└── repository/
    ├── ItemRepositoryImplTest.kt
    └── PokemonRepositoryImplTest.kt
```

- `repository` 와 `datasource` 는 **형제 패키지** 다. 로컬 파일 실습의 옛 코드처럼 `datasource/repository` 로 중첩하지 않는다. Repository 는 DataSource 의
  하위 개념이 아니라 DataSource 를 사용하는 상위 계층이다.
- `dto`, `mapper` 도 같은 높이의 형제다. 원격 API 를 쓰는 실습에만 생긴다.
- Fake 는 메인 소스셋이 아니라 **테스트 소스셋** 에 둔다. 배포 코드에 테스트 더블이 섞이지 않는다.
- 테스트 패키지는 메인 패키지를 그대로 따라간다. 로컬 파일 실습의 `UserRepositoryTest` 는 테스트 파일의 패키지가 메인 코드의 패키지와 달라서
  `internal` 접근도 안 되고 파일 위치만 봐서는 대상 코드를 찾기 어렵다.
- 문서에서는 패키지를 `datasource` 로 통일해 적는다. (실제 실습 코드 일부의 패키지명은 `data_source` 라서 아래 코드 발췌의 `package` · `import` 줄은 원본 그대로 `data_source` 다.)

---

## 2. 의존성 주입 (DI) 원칙

> **Repository 는 DataSource 인터페이스 하나만 생성자로 받는다. 그 외에는 아무것도 모른다.**

### 2-1. 생성자 주입

```kotlin
// 좋은 예 (로컬 파일 실습 UserRepositoryImpl 그대로)
class UserRepositoryImpl(
    private val dataSource: UserDataSource,     // 인터페이스 타입
) : UserRepository { ... }
```

```kotlin
// 좋은 예 (원격 API 실습 PokemonRepositoryImpl 그대로) — 원격이어도 받는 것은 똑같이 인터페이스 하나
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository { ... }
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

class PokemonRepositoryImpl(
    private val httpClient: HttpClient,                  // ⑤ 통신 객체를 직접 받음. HttpClient 는 DataSource 가 받을 것
) : PokemonRepository
```

- 생성자 파라미터는 `private val dataSource: <모델명>DataSource` 한 줄이다. 기본값을 주지 않는다.
- DataSource 가 두 개 이상 필요하면 (예: 로컬 + 원격) 각각 인터페이스 타입으로 받는다. 그래도 기술 타입은 받지 않는다.
- `HttpClient` 를 생성자로 받는 것은 **DataSource** 다 ([datasource.md](datasource.md)). Repository 는 그 DataSource 를 받는다. 한 단계 아래 것을 건너뛰어 받지 않는다.

### 2-2. import 로 확인하는 방법

Repository 파일 (인터페이스 · 구현체)의 `import` 에는 다음 종류만 있어야 한다.

```kotlin
// 로컬 파일 실습 — 두 종류
import com.survivalcoding.<dayXX>.datasource.ItemDataSource   // 구현체 파일에만
import com.survivalcoding.<dayXX>.model.Item
```

```kotlin
// 원격 API 실습 PokemonRepositoryImpl.kt — 네 종류 (원본 그대로)
import com.survivalcoding.<dayXX>.data_source.PokemonDataSource   // 구현체 파일에만
import com.survivalcoding.<dayXX>.dto.PokemonDto                  // 구현체 파일에만. body 의 타입을 적기 위해
import com.survivalcoding.<dayXX>.mapper.toModel                  // 구현체 파일에만. 확장 함수는 import 해야 보인다
import com.survivalcoding.<dayXX>.model.Pokemon
```

| 허용 import                | 어느 파일       | 이유                                                          |
|----------------------------|-----------------|---------------------------------------------------------------|
| `...model.<모델>`          | 인터페이스 · 구현체 | 돌려주는 것이 모델이다                                    |
| `...datasource.<DataSource 인터페이스>` | 구현체 | 생성자로 받는다                                           |
| `...dto.<모델>Dto`         | 구현체          | 원격 API 일 때만. `response.body` 를 받는 변수의 타입         |
| `...mapper.toModel`        | 구현체          | 원격 API 일 때만. DTO → 모델 변환 호출                        |

아래가 보이면 책임이 잘못 들어온 것이다.

| import                              | 의미                               |
|-------------------------------------|------------------------------------|
| `java.io.File`, `java.nio.file.*`   | 파일 조작이 Repository 로 올라왔다 |
| `kotlinx.serialization.*`, `Json`   | 파싱이 Repository 로 올라왔다      |
| `java.net.http.*`, `io.ktor.*`      | 통신이 Repository 로 올라왔다      |
| `java.sql.*`                        | DB 가 Repository 로 올라왔다       |
| `FileItemDataSource`, `RemotePokemonDataSource` 등 구현체 이름 | 구현체에 묶였다 |
| `...datasource.Response`            | `Response` 는 import 없이 쓴다 — `dataSource.getPokemon()` 의 반환 타입으로 받기만 하고 변수 타입에 적지 않는다 |

- `Response` 를 Repository 가 직접 만들거나 타입으로 선언하면 DataSource 의 포장 형식에 묶인다. `val response = dataSource.getPokemon(name)` 처럼 **타입 추론에 맡기고** `.body` 만 꺼낸다.

### 2-3. 조립은 사용하는 쪽 (`main`)에서

```kotlin
// 로컬 파일 실습
fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()      // 구현체 선택은 여기서만
    val repository: ItemRepository = ItemRepositoryImpl(dataSource)

    repository.getItems().forEach(::println)
}
```

```kotlin
// 원격 API 실습 Main.kt  (원본 전체) — suspend 를 부르려면 runBlocking 으로 감싼다
package com.survivalcoding.<dayXX>

import com.survivalcoding.<dayXX>.data_source.PokemonDataSource
import com.survivalcoding.<dayXX>.data_source.RemotePokemonDataSource
import com.survivalcoding.<dayXX>.model.Pokemon
import com.survivalcoding.<dayXX>.repository.PokemonRepository
import com.survivalcoding.<dayXX>.repository.PokemonRepositoryImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val dataSource: PokemonDataSource = RemotePokemonDataSource()
    val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

    val pokemon: Pokemon? = repository.getPokemon("pikachu")
    println(pokemon?.name ?: "이름 없음")
    println(pokemon?.imageUrl ?: "이미지 없음")
}
```

- 변수는 **인터페이스 타입으로 선언** 한다. `val repository = ItemRepositoryImpl(...)` 처럼 추론에 맡기면 구현체에 묶인다.
- 저장 매체를 바꿀 때 바뀌는 줄은 `main` 의 첫 줄 하나뿐이어야 한다. Repository 파일을 열게 되면 어딘가 의존성이 새고 있는 것이다.
- `main` 이 `runBlocking` 인 것은 **`suspend` 함수를 부르기 위해서** 다. `fun main(): Unit = runBlocking { ... }` — 반환 타입 `Unit` 을 적는 이유는 `runBlocking` 의 결과(마지막 식의 값)가 `main` 의 반환값이 되지 않게 하기 위해서다.
- `main` 은 `Pokemon?` 만 받는다. DTO 도, `Response` 도, Mapper 도 import 하지 않는다. `pokemon?.name ?: "이름 없음"` 처럼 **`null` 처리는 쓰는 쪽** 에서 한다.

---

## 3. 비즈니스 로직과 책임 분리

> **저장 매체를 만지는 일은 전부 DataSource 에, 데이터를 가공하는 일은 전부 Repository 에.**

### 3-1. 책임 표

| 할 일                                                | DataSource | Mapper | Repository |
|------------------------------------------------------|------------|--------|------------|
| 파일 · DB · 네트워크 읽기/쓰기                       | O          | X      | X          |
| JSON · CSV 파싱, 타입 변환, 빈 줄 제거               | O          | X      | X          |
| JSON → DTO decode (원격 API)                         | O          | X      | X          |
| `Response` 에 statusCode · body 담기 (원격 API)      | O          | X      | X          |
| 전체 목록 그대로 돌려주기 (`loadItems`, `saveItems`) | O          | X      | 위임만     |
| `Response.body` 가 `null` 인지 판단 (원격 API)       | X          | X      | O          |
| DTO → 모델 변환 (`dto.toModel()`) 호출 (원격 API)    | X          | 변환 자체 | 호출    |
| 통신 · 파싱 예외를 `null` 로 바꾸기 (원격 API)       | X          | X      | O          |
| 정렬 (`sortedBy`)                                    | X          | X      | O          |
| 필터링 (`filter`, `filterNot`)                       | X          | X      | O          |
| 검색 (`contains`, `ignoreCase`)                      | X          | X      | O          |
| 단건 찾기 (`find`)                                   | X          | X      | O          |
| 개수 제한, 상위 N (`take`)                           | X          | X      | O          |
| 추가 · 삭제 후 저장 (읽기 → 바꾸기 → 쓰기)           | 쓰기만     | X      | 흐름 전체  |
| 비즈니스 규칙 (가격 상한, 중복 id 금지 등)           | X          | X      | O          |

- DataSource 인터페이스는 `load` / `save` 두 메서드로 충분하다. `loadItemsSortedByName()` 같은 메서드가 DataSource 에 생기면 가공이 아래로 내려간 것이다.
- 반대로 Repository 가 `file.readText()` 를 하면 저장 매체 조작이 위로 올라온 것이다.
- 원격 API 에서는 **DTO 까지가 DataSource, DTO 부터가 Repository** 다. (선생님 주석: "데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자" / "로직만 볼거야. 기술 관심 없어 (역직렬화? 관심 없어)")
- Mapper 는 변환 "자체" 만 하고, 그것을 "언제 부를지" 는 Repository 가 정한다. ([mapper.md](mapper.md) 4장)

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

### 3-3. 읽기 메서드 패턴 — 원격 API (`suspend` · `Response` · DTO)

```kotlin
// PokemonRepositoryImpl.kt  (원본 중 메서드 부분)
    override suspend fun getPokemon(name: String): Pokemon? {
        try {
            val response = dataSource.getPokemon(name)           // ① suspend 호출. 타입은 추론에 맡긴다 (Response<PokemonDto>)

            val dto: PokemonDto = response.body ?: return null   // ② body 가 없으면 모델도 없다. 여기서 끝

            return dto.toModel()                                 // ③ DTO → 모델. 이 줄 아래로 DTO 는 안 보인다

        } catch (ex: Exception) {
            return null                                          // ④ 통신 · 파싱 예외는 전부 "없음" 으로
        }
    }
```

세 줄이 각각 한 가지 결정이다.

| 줄 | 결정                                   | 안 하면                                                                 |
|----|----------------------------------------|-------------------------------------------------------------------------|
| ①  | `Response` 를 변수 타입에 적지 않는다  | Repository 가 DataSource 의 포장 형식에 묶인다 (2-2)                    |
| ②  | `body ?: return null` — 빈 응답은 `null` | `body!!` 를 쓰면 빈 응답에서 NPE. 호출하는 쪽이 `try` 를 또 써야 한다 |
| ③  | `dto.toModel()` 을 **받자마자** 부른다 | DTO 가 Repository 아래쪽 코드로 퍼진다. 가공을 DTO 에 하게 된다         |
| ④  | `catch (ex: Exception) → null`         | 네트워크 끊김 · JSON 깨짐이 `main` 까지 예외로 올라온다                 |

- `suspend` 는 **인터페이스에도** 붙인다. `interface PokemonRepository { suspend fun getPokemon(name: String): Pokemon? }` — 구현체에만 붙이면 override 가 안 된다.
- `try/catch` 가 Repository 에 있는 이유: "실패하면 어떻게 할지" 는 **비즈니스 결정** 이다. 실습 코드는 "없는 것으로 친다(`null`)" 를 택했다. 재시도 · 기본 모델 · 예외 전파 중 무엇을 고를지는 앱마다 다르고, 그 결정을 두는 자리가 Repository 다.
- `catch (ex: Exception)` 으로 **전부** 잡는 것은 실습용 단순화다. `ex` 를 로그로 남기지 않아서 왜 `null` 이 됐는지 알 수 없다. 실제 앱에서는 최소한 예외 종류별로 나누거나 기록한다. (지금 규칙으로 정하지는 않는다.)
- 포켓몬 실습에는 정렬 · 필터가 없다. 단건 조회라 가공할 목록이 없기 때문이지, 원격 API 라서 가공을 안 하는 것이 아니다. 목록 API 가 생기면 3-2 와 똑같이 `dto.toModel()` 뒤에 `sortedBy`, `filter` 가 붙는다.

### 3-4. 쓰기 메서드 패턴

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
- 원격 API 실습에는 쓰기 메서드가 없다. (PokeAPI 는 읽기 전용이다.)

---

## 4. 테스트 용이성 및 Test Double 원칙

> **Repository 테스트는 Fake 로, DataSource 테스트는 Ktor MockEngine 으로. 테스트 더블은 인터페이스 자리에 꽂힌다.**

### 4-1. 어디에 무엇을 쓰나 — Fake 와 MockEngine 의 역할 분리

| 검증 대상          | 테스트 더블                  | 무엇을 바꿔 끼우나                         | 파일                              | 자세한 규칙                     |
|--------------------|------------------------------|--------------------------------------------|-----------------------------------|---------------------------------|
| **Repository**     | **Fake** (직접 작성)         | `DataSource` 인터페이스 → In-Memory 구현   | `FakeItemDataSource`, `FakePokemonDataSource` | 이 문서 4-2 · 4-4           |
| DataSource (원격)  | Ktor **MockEngine**          | `HttpClient` 의 엔진 → 정해진 응답을 돌려주는 엔진 | `Mocks.kt` (`mockEngine`)    | [test.md](test.md) · [datasource.md](datasource.md) |
| Mapper             | 없음                         | 입력 DTO → 출력 모델 비교뿐                | `ToModelTest`                     | [mapper.md](mapper.md) 5장      |

|             | Mock (Mockito, MockK)                                 | Fake (직접 작성)                                    | Ktor MockEngine                                      |
|-------------|-------------------------------------------------------|-----------------------------------------------------|------------------------------------------------------|
| 추가 의존성 | 필요                                                  | 없음                                                | `testImplementation("io.ktor:ktor-client-mock")` — `build.gradle.kts` 에 이미 있다 |
| 동작        | "이 메서드가 호출되면 이걸 돌려줘" 를 테스트마다 지정 | 진짜 저장소처럼 넣은 대로 나온다                    | "이 URL 이 오면 이 응답" 을 미리 적어 둔다           |
| 검증하는 것 | 호출 여부 · 횟수 (구현 방식에 묶임)                   | 결과 값 (구현을 바꿔도 테스트가 살아남는다)         | 응답 문자열 → DTO 변환이 맞는가                      |
| 읽기 쉬움   | `every { } returns` 문법을 알아야 함                  | 평범한 Kotlin 클래스                                | `MockEngine { request -> respond(...) }` 한 블록     |
| 속도        | 빠름                                                  | 빠름. 파일 I/O 가 없다                              | 빠름. 네트워크가 없다                                |
| 쓰는 곳     | 쓰지 않는다                                           | **Repository 테스트**                               | **DataSource 테스트**                                |

- Repository 테스트의 목적은 "정렬·필터·검색·`null` 처리가 맞는가" 다. `loadItems()` 가 몇 번 불렸는지, HTTP 가 실제로 나갔는지는 관심사가 아니다. 그래서 Fake 로 충분하다.
- DataSource 테스트의 목적은 "응답 문자열을 DTO 로 제대로 바꾸는가" 다. 그래서 응답 문자열을 정해 주는 MockEngine 이 맞다. Fake 를 쓰면 DataSource 자체가 빠져 버려서 검증할 것이 없다.
- Mockito · MockK 같은 범용 Mock 라이브러리는 쓰지 않는다. `build.gradle.kts` 에도 없다.
- 파일을 실제로 만들거나 진짜 서버를 호출해서 테스트하지 않는다. 테스트 순서에 따라 결과가 달라지고, 작업 디렉터리에 찌꺼기 파일이 남고, 네트워크가 끊기면 테스트가 깨진다.

### 4-2. Fake 작성 규칙 — 로컬 파일 (`load` / `save`)

```kotlin
// FakeItemDataSource.kt  (테스트 소스셋)
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
5. **자기 파일** 에 둔다. 테스트 클래스 파일 안에 같이 넣으면 다른 테스트가 재사용할 수 없다. (로컬 파일 실습의 `FakeUserDataSource` 가 `UserRepositoryTest.kt` 안에 있다.)
6. 검증용 메서드가 필요하면 `loadItems()` 를 그대로 쓴다. `getSavedCount()` 같은 메서드를 Fake 에 추가하지 않는다.

### 4-3. 테스트 작성 규칙

```kotlin
class ItemRepositoryImplTest {                              // ① 아무것도 상속하지 않는다

    private lateinit var fakeDataSource: FakeItemDataSource
    private lateinit var repository: ItemRepository         // ② 인터페이스 타입

    @BeforeEach                                             // ③ JUnit 5 (org.junit.jupiter.api)
    fun setUp() {
        fakeDataSource = FakeItemDataSource()
        fakeDataSource.saveItems(listOf(...))               // ④ 모델에 실제로 있는 필드만
        repository = ItemRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `getItems 는 이름 오름차순으로 정렬한다`() {
        ...
    }   // ⑤ 한글 백틱 이름: "메서드 는 무엇 한다"
}
```

| 번호 | 규칙                                                                   | 로컬 파일 실습 `UserRepositoryTest` 의 위반                                      |
|------|------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| ①    | 테스트 클래스는 어떤 인터페이스도 구현하지 않는다                      | `class UserRepositoryTest : UserDataSource` → 추상 메서드 미구현으로 컴파일 실패 |
| ②    | Repository 변수는 인터페이스 타입으로 선언한다                         | 지켰다 (`private lateinit var repository: UserRepository`)                       |
| ③    | `org.junit.jupiter.api.BeforeEach` / `Test` / `Assertions` 만 쓴다     | `import org.junit.Before` → JUnit 4 는 의존성에 없다                             |
| ④    | 테스트 데이터는 모델의 실제 필드로만 만든다                            | `User(username = ...)` → `User` 에는 `postId, id, name, email, body` 뿐          |
| ⑤    | 테스트 이름은 ``메서드명 은/는 기대동작``. 하나의 테스트는 하나의 동작 | 지켰다                                                                           |

추가 규칙:

- **Given / When / Then** 세 블록으로 쓴다. `setUp` 이 Given, 테스트 본문 첫 줄이 When, `assert` 가 Then 이다.
- 쓰기 메서드 (`add`, `delete`)는 **Repository 로 다시 읽어서** 확인한다. `fakeDataSource.loadItems()` 로 직접 봐도 되지만, Repository 의 읽기 경로를
  함께 검증하는 쪽이 낫다.
- 모든 공개 메서드마다 최소 한 개, 조건이 있는 메서드 (`getItem`, `searchByName`)는 "있을 때 / 없을 때" 두 개를 쓴다.
- 테스트는 서로 독립이다. `@BeforeEach` 가 매번 새 Fake 를 만들기 때문에 한 테스트의 `add` 가 다른 테스트에 남지 않는다.
- 실행: `./gradlew test`. IDE 에서 `@Test` 옆 화살표로 하나씩 돌려도 된다.
- 테스트 클래스의 이름 · 위치 · 코루틴 테스트 방법 등 테스트 전반의 규칙은 [test.md](test.md) 를 따른다.

### 4-4. 원격 API Repository 테스트 — Fake 는 `jsonString` 을, 테스트는 `runTest` 를

원격 API 의 Fake 는 4-2 와 모양이 다르다. 상태를 들고 있는 대신 **저장해 둔 응답 문자열을 DTO 로 decode 해서** 돌려준다.

```kotlin
// FakePokemonDataSource.kt  (원본 중 클래스 부분. 8~1442줄이 const val jsonString = """{ ... }""" — 실제 API 응답 전문)
package com.survivalcoding.<dayXX>.data_source

import com.survivalcoding.<dayXX>.dto.PokemonDto
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

const val jsonString = """{
  "id": 132,
  "name": "ditto",
  ...
}"""

class FakePokemonDataSource : PokemonDataSource {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPokemon(name: String): Response<PokemonDto> {
        delay(2.seconds)
        return Response(
            statusCode = 200,
            body = json.decodeFromString<PokemonDto>(jsonString),
        )
    }
}
```

| 4-2 (로컬 파일 Fake)                  | 4-4 (원격 API Fake)                                               |
|---------------------------------------|-------------------------------------------------------------------|
| 상태 `private var items: List<모델>`  | 상태 없음. `jsonString` 상수 하나                                 |
| `load` → 그대로, `save` → 통째로 교체 | `getPokemon(name)` → `name` 과 상관없이 항상 같은 DTO              |
| import 는 모델 + 인터페이스           | import 에 `Json` · `delay` 가 더 있다 — **DTO 를 만들려면 decode 가 필요** 하기 때문 |
| 동기                                  | `suspend` + `delay(2.seconds)` 로 네트워크 지연을 흉내낸다        |

- `jsonString` 은 **실제 서버 응답을 한 번 받아 통째로 붙여 넣은 것** 이다 (`"name": "ditto"`). 손으로 줄인 JSON 을 쓰면 DTO 의 필드가 실제 응답과 맞는지 확인이 안 된다. ([dto.md](dto.md) 5장 1번)
- `jsonString` 이 `const val` 로 파일 최상위에 있어서 같은 패키지의 다른 테스트 (`RemotePokemonDataSourceTest`, `Mocks.kt`) 도 같은 응답을 쓴다. Fake 와 MockEngine 이 **같은 데이터** 로 테스트한다.
- `delay(2.seconds)` 는 `runTest` 안에서는 **가상 시간** 으로 즉시 건너뛴다. `runBlocking` 으로 돌리면 정말 2초를 기다린다. 그래서 Repository 테스트는 `runTest` 다. ([test.md](test.md))

```kotlin
// PokemonRepositoryImplTest.kt  (원본 전체)
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.data_source.FakePokemonDataSource
import com.survivalcoding.<dayXX>.data_source.PokemonDataSource
import com.survivalcoding.<dayXX>.data_source.RemotePokemonDataSource
import com.survivalcoding.<dayXX>.data_source.mockEngine
import com.survivalcoding.<dayXX>.model.Pokemon
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class PokemonRepositoryImplTest {
    @Test
    fun `Pokemon 데이터가 잘 들어와야 한다`(): Unit = runTest {
//        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))
        val dataSource: PokemonDataSource = FakePokemonDataSource()

        val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

        val pokemon: Pokemon? = repository.getPokemon("ditto")

        assertNotNull(pokemon)
        assertEquals("ditto", pokemon.name)
    }
}
```

| 줄                                                        | 규칙                                                                                                        |
|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| `fun ...(): Unit = runTest { ... }`                       | `suspend` 함수를 부르는 테스트는 `runTest` 로 감싼다. `: Unit` 은 2-3 의 `main` 과 같은 이유                |
| `val dataSource: PokemonDataSource = FakePokemonDataSource()` | ② 인터페이스 타입으로 선언. 그래서 주석 처리된 윗줄처럼 `RemotePokemonDataSource(HttpClient(mockEngine))` 로 **한 줄만 바꿔** 끼울 수 있다 |
| `val repository: PokemonRepository = ...`                 | ② Repository 도 인터페이스 타입                                                                             |
| `assertNotNull(pokemon)` 다음 줄에서 `pokemon.name`       | `org.junit.jupiter.api.assertNotNull` (코틀린용) 은 통과하면 그 아래에서 `pokemon` 을 non-null 로 스마트 캐스트한다. `Assertions.assertNotNull` (자바용) 은 캐스트가 안 돼서 `pokemon?.name` 이나 `!!` 가 필요하다 |
| `@BeforeEach` 가 없다                                     | 테스트가 하나뿐이라 본문에서 바로 조립했다. 둘 이상이 되면 4-3 처럼 `setUp` 으로 올린다                    |

- 주석 처리된 `RemotePokemonDataSource(HttpClient(mockEngine))` 줄은 "DataSource 를 진짜 구현체 + MockEngine 으로 바꿔도 Repository 테스트가 그대로 돈다" 를 보여 준다. 이것이 인터페이스로 선언하는 이유다. 다만 **커밋할 때는 지운다.** 안 쓰는 import 4줄 (`RemotePokemonDataSource`, `mockEngine`, `io.ktor.client.*`, `runBlocking`) 도 함께 지운다.
- 원본은 "잘 들어온다" 하나뿐이다. 3-3 의 결정 ②·④ 를 고정하려면 "`body` 가 `null` 이면 `null`", "예외가 나면 `null`" 테스트가 더 있어야 한다. 그러려면 Fake 가 `null` 이나 예외를 돌려주는 경우를 만들 수 있어야 하므로, 새 Repository 를 만들 때는 Fake 생성자로 `body` 를 받게 한다. (원본에는 없는 보강이다.)

---

## 5. 새로운 Repository 추가 템플릿

새 Repository 를 만들 때는 다음 순서를 따른다.

1. `model` 모델, `datasource` 인터페이스 + 구현체는 [datasource.md 4장](datasource.md) 대로 먼저 만든다. 원격 API 면 `dto` ([dto.md](dto.md)) 와 `mapper` ([mapper.md](mapper.md)) 도 먼저다.
2. `repository` 패키지에 `<모델명>Repository.kt` 인터페이스를 만든다. import 는 모델만.
3. `repository` 패키지에 `<모델명>RepositoryImpl.kt` 구현체를 만든다. 생성자는 DataSource 인터페이스 하나.
4. 테스트 소스셋의 `datasource` 패키지에 `Fake<모델명>DataSource.kt` 를 만든다.
5. 테스트 소스셋의 `repository` 패키지에 `<모델명>RepositoryImplTest.kt` 를 만들고 `./gradlew test` 가 통과하는지 확인한다.
6. `Main.kt` 에서 인터페이스 타입으로 조립한다.

아래는 `Item` 기준 전체 세트(로컬 파일)와 `Pokemon` 기준 전체 세트(원격 API)다. 모델명만 바꾸면 그대로 쓸 수 있다.

### 5-1. 모델과 DataSource (기존 코드, 참고용)

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

### 5-2. Repository 인터페이스

```kotlin
// ItemRepository.kt
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.model.Item

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
// ItemRepositoryImpl.kt
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.datasource.ItemDataSource
import com.survivalcoding.<dayXX>.model.Item

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
// FakeItemDataSource.kt  (테스트 소스셋)
package com.survivalcoding.<dayXX>.datasource

import com.survivalcoding.<dayXX>.model.Item

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
// ItemRepositoryImplTest.kt  (테스트 소스셋)
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.datasource.FakeItemDataSource
import com.survivalcoding.<dayXX>.model.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemRepositoryImplTest {

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
package com.survivalcoding.<dayXX>

import com.survivalcoding.<dayXX>.datasource.FileItemDataSource
import com.survivalcoding.<dayXX>.datasource.ItemDataSource
import com.survivalcoding.<dayXX>.model.Item
import com.survivalcoding.<dayXX>.repository.ItemRepository
import com.survivalcoding.<dayXX>.repository.ItemRepositoryImpl

fun main() {
    val dataSource: ItemDataSource = FileItemDataSource()
    val repository: ItemRepository = ItemRepositoryImpl(dataSource)

    repository.addItem(Item(id = 1L, name = "Item 1", price = 1000))
    repository.addItem(Item(id = 2L, name = "Item 2", price = 2000))

    repository.getItems().forEach(::println)
}
```

### 5-7. 원격 API 세트 (Pokemon) — `suspend` · DTO · Mapper 가 들어갈 때

5-1 ~ 5-6 에서 달라지는 것만 적는다. 모델 · DTO · Mapper · DataSource 는 각 문서를 따른다.

| 달라지는 것             | 로컬 파일 (`Item`)                | 원격 API (`Pokemon`)                                        |
|-------------------------|-----------------------------------|-------------------------------------------------------------|
| 메서드                  | `fun`                             | `suspend fun` (인터페이스 · 구현체 둘 다)                   |
| DataSource 가 주는 것   | `List<Item>` (모델)               | `Response<PokemonDto>` (DTO)                                |
| 구현체 import           | DataSource + 모델                 | + `dto.PokemonDto` + `mapper.toModel`                       |
| 메서드 본문             | `load...()` 뒤에 가공             | `getPokemon()` → `body ?: return null` → `dto.toModel()` → (가공) |
| 실패 처리               | 없음 (파일은 있다고 가정)         | `try/catch (Exception) → null`                              |
| Fake                    | `List` 상태 + `load/save`         | `jsonString` decode + `delay`                               |
| 테스트                  | 동기                              | `runTest`                                                   |
| `main`                  | `fun main()`                      | `fun main(): Unit = runBlocking { ... }`                    |

```kotlin
// PokemonRepository.kt  (원본 전체)
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemon(name: String): Pokemon?
}
```

```kotlin
// PokemonRepositoryImpl.kt  (원본 전체)
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.data_source.PokemonDataSource
import com.survivalcoding.<dayXX>.dto.PokemonDto
import com.survivalcoding.<dayXX>.mapper.toModel
import com.survivalcoding.<dayXX>.model.Pokemon

// 먼저 짠다 (비즈니스 핵심)
// 로직만 볼거야. 기술 관심 없어(역직렬화? 관심 없어)
// 걸러내기 (filter)
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository {

    override suspend fun getPokemon(name: String): Pokemon? {
        try {
            val response = dataSource.getPokemon(name)

            val dto: PokemonDto = response.body ?: return null

            return dto.toModel()

        } catch (ex: Exception) {
            return null
        }
    }
}
```

- 선생님 주석 "먼저 짠다 (비즈니스 핵심)" — 원격 API 에서는 **Repository 를 DataSource 보다 먼저** 짠다. "무엇을 돌려줄지(`Pokemon?`)" 를 먼저 정하고, 그것을 만들기 위해 DataSource 에 "무엇을 달라고 할지(`Response<PokemonDto>`)" 를 정한다. 기술(통신 · 역직렬화)은 그 다음이다.
- Fake 와 테스트는 4-4 에 원본 전체가 있다. `main` 은 2-3 에 있다.

---

## 6. 커밋 전 체크리스트

- [ ] 이름이 `<모델명>Repository` / `<모델명>RepositoryImpl` / `Fake<모델명>DataSource` / `<모델명>RepositoryImplTest` 인가
- [ ] `repository` 와 `datasource` (원격이면 `dto`, `mapper` 도) 가 형제 패키지인가. Fake 는 테스트 소스셋에 있는가
- [ ] 인터페이스의 모든 메서드가 구현체에 있고, 파라미터 타입 (특히 `id`)이 모델과 같은가. `suspend` 가 인터페이스 · 구현체 양쪽에 있는가
- [ ] Repository 생성자가 `DataSource` 인터페이스 하나만 받고 기본값이 없는가 (`HttpClient` 아님)
- [ ] Repository 파일의 import 에 `File`, `Json`, `Http`, `ktor`, `sql`, `Response`, 구현체 이름이 없는가. 원격이면 `dto` · `mapper.toModel` 만 더 있는가
- [ ] 정렬 · 필터 · 검색 · 비즈니스 규칙이 전부 Repository 에, 읽기 · 쓰기 · 파싱 · DTO 생성이 전부 DataSource 에 있는가
- [ ] 쓰기 메서드가 "읽기 → 바꾸기 → 전체 저장" 패턴인가
- [ ] 원격이면 `body ?: return null` → `dto.toModel()` 순서이고, `toModel()` 아래로 DTO 가 안 보이는가. `try/catch → null` 이 있는가
- [ ] 테스트 클래스가 아무것도 상속하지 않고, `org.junit.jupiter.api` 만 쓰는가. `suspend` 를 부르면 `runTest` 인가
- [ ] 테스트 데이터가 모델의 실제 필드로만 만들어졌는가. 원격이면 Fake 의 `jsonString` 이 실제 응답 전문인가
- [ ] 테스트 파일에 안 쓰는 import 와 주석 처리된 코드가 없는가
- [ ] 공개 메서드마다 테스트가 있고 `./gradlew test` 가 통과하는가
