# 테스트 작성 원칙

실습 코드의 테스트 소스셋에 있는 파일 11개 — 자바 `HeroTest` / `ClericTest` / `BookTest`, 코틀린 `BookTest`,
로컬 파일 실습의 `FakeCommentDataSource` / `CommentRepositoryImplTest`, 원격 API 실습의 `JsonPlaceHolderTodoDataSourceTest`,
포켓몬 실습의 `Mocks` / `FakePokemonDataSource` / `RemotePokemonDataSourceTest` / `ToModelTest` / `PokemonRepositoryImplTest` — 와
수업 자료 「Kotlin Test 이론」 을 기준으로 정리한, 새로운 테스트를 만들 때 지켜야 할 필수 원칙이다.
DataSource 쪽 규칙은 [datasource.md](datasource.md), DTO 는 [dto.md](dto.md), Mapper 는 [mapper.md](mapper.md), Repository 는 [repository.md](repository.md) 를 따른다.
이 문서는 테스트 소스셋 안의 파일만 다룬다.
코드 발췌는 원본 그대로이되, `package` · `import` 줄의 수업 날짜 패키지명만 `<dayXX>` 로 바꿔 적었다.

> **한 줄 요약**
> 테스트는 "인터페이스 자리에 가짜를 꽂고, **결과 값** 을 비교한다".
> Repository 는 Fake 로, DataSource 는 MockEngine 으로, 그 밖의 클래스는 가짜 없이 바로 부른다.

---

## 0. 현재 테스트 코드 분석 결과

| 파일                                  | 언어   | 검증 대상                          | 더블          | 상태      | 발견한 것                                                                                                   |
|---------------------------------------|--------|------------------------------------|---------------|-----------|-------------------------------------------------------------------------------------------------------------|
| `HeroTest.java`                       | Java   | `Hero.sleep()`                     | 없음          | 빈 테스트 | 본문 전체가 주석 처리. 아무것도 검증하지 않는데 **항상 통과** 한다                                           |
| `ClericTest.java`                     | Java   | `Cleric.setMp()`                   | 없음          | 정상      | 활성 테스트 1개 (`assertThrows`). 시나리오 2개는 주석 처리                                                   |
| `BookTest.java`                       | Java   | `Book` 의 `equals` · 정렬 · `clone` | 없음         | 일부 부족 | `cloneTest` 에 `assert` 가 없다. `println` 만 있어서 복사가 됐는지 검증이 안 된다                            |
| `BookTest.kt`                         | Kotlin | `Book` 이 `TangibleAsset` 인가      | 없음         | 정상      | `@file:Suppress("NonAsciiCharacters")` 로 한글 테스트 이름 경고를 끈 유일한 파일                              |
| `FakeCommentDataSource.kt`            | Kotlin | (더블)                             | Fake          | 정상      | 자기 파일에 있다. `List` 상태 + `load/save`                                                                 |
| `CommentRepositoryImplTest.kt`        | Kotlin | `CommentRepositoryImpl`            | Fake          | 정상      | `@BeforeEach` + Given/When/Then. 테스트 이름이 `getComments` 와 `getComments_returnsEmpty_whenPostIdNotExists` 로 두 스타일이 섞여 있다 |
| `JsonPlaceHolderTodoDataSourceTest.kt` | Kotlin | (없음)                            | MockEngine    | 대상 없음 | DataSource 를 만들지 않고 `HttpClient(mockEngine).get()` 만 부른다. MockEngine 이 테스트 클래스 **안에** 있어 재사용이 안 된다. 테스트 이름이 `test` |
| `Mocks.kt`                            | Kotlin | (더블)                             | MockEngine    | 정상      | 최상위 `val mockEngine` 으로 빼서 두 테스트가 공유한다. `MockEngine.Companion.invoke` import 는 없어도 된다  |
| `FakePokemonDataSource.kt`            | Kotlin | (더블)                             | Fake          | 정상      | `const val jsonString` (실제 응답 전문) + `delay(2.seconds)`                                                |
| `RemotePokemonDataSourceTest.kt`      | Kotlin | `RemotePokemonDataSource`          | MockEngine    | **실패**  | `assertEquals(jsonString, response.body)` — `String` 과 `PokemonDto` 비교. 타입이 달라 항상 실패한다         |
| `ToModelTest.kt`                      | Kotlin | `PokemonDto.toModel()`             | 없음          | 정상      | `id` · `name` 만 검증. `imageUrl` (중첩 접근) 이 빠져 있다                                                   |
| `PokemonRepositoryImplTest.kt`        | Kotlin | `PokemonRepositoryImpl`            | Fake          | 정상      | `runTest`. 안 쓰는 import 4줄, 주석 처리된 줄 1개                                                            |

---

## 1. 네이밍 규칙

### 1-1. 테스트 클래스: `<검증 대상>Test`

| 검증 대상의 종류                 | 규칙                                 | 실습 코드                                                              |
|----------------------------------|--------------------------------------|------------------------------------------------------------------------|
| 일반 클래스 (모델, 캐릭터 등)    | `<클래스명>Test`                     | `HeroTest`, `ClericTest`, `BookTest`                                   |
| Repository                       | **`<구현체명>Test`** — `...ImplTest` | `CommentRepositoryImplTest`, `PokemonRepositoryImplTest`               |
| DataSource                       | **`<구현체명>Test`**                 | `RemotePokemonDataSourceTest`                                          |
| 확장 함수 (Mapper)               | `<함수명 첫 글자 대문자>Test`        | `ToModelTest` (함수 `toModel`)                                         |

- Repository · DataSource 는 **인터페이스가 아니라 구현체 이름** 을 쓴다. `Impl` 이 실제로 동작하는 코드이고, 테스트가 검증하는 것도 그 코드다. 구현체가 둘 이상이면 (`RemotePokemonDataSource`, `FilePokemonDataSource`) 어느 것을 검증하는지 이름으로 바로 보인다.
- 테스트 안에서 **변수 선언은 인터페이스 타입** 으로 한다 (`val repository: PokemonRepository = PokemonRepositoryImpl(...)`). 이름은 구현체, 타입은 인터페이스 — 둘은 다른 얘기다.
- Mapper 는 클래스가 없고 확장 함수뿐이라 함수 이름을 따른다. `PokemonMapperTest` 가 아니라 `ToModelTest` 다. ([mapper.md 1-1](mapper.md))

| 잘못된 예                  | 왜                                                                  |
|----------------------------|---------------------------------------------------------------------|
| `PokemonRepositoryTest`    | 인터페이스 이름. 구현체가 여럿이면 어느 것인지 모른다               |
| `PokemonTest`              | 모델인지 Repository 인지 DataSource 인지 모른다                     |
| `Test1`, `MyTest`          | 무엇을 검증하는지 없다                                              |

### 1-2. 테스트 메서드 이름 — "무엇이 어떻게 되어야 한다"

실습 코드에 세 가지 스타일이 있다.

| 스타일                       | 실습 코드                                                                                     | 판정              |
|------------------------------|-----------------------------------------------------------------------------------------------|-------------------|
| 코틀린 백틱 + 한글 문장      | `` `포켓몬 데이터가 잘 들어와야 한다` ``, `` `Dto 에 null 이 포함되면 기본값으로 설정되어야 한다` ``, `` `Book 은 TangibleAsset 이다` `` | **기본** (코틀린) |
| 자바 메서드명 + `@DisplayName` | `@DisplayName("sleep 은 hp 를 100으로 만들어야 한다") void sleepTest()`, `@DisplayName("mp 타당성 검사") void MP_테스트()` | **기본** (자바)   |
| 영어 snake_case              | `getComments_returnsEmpty_whenPostIdNotExists`                                                | 허용하되 한 파일 안에서 섞지 않는다 |
| 메서드명 그대로 / 의미 없음  | `getComments()`, `test()`                                                                     | X — 무엇을 기대하는지 없다 |

- 문장 꼴: **`<대상> 은/는 <조건이면> <기대 결과>여야 한다`**. 테스트가 깨졌을 때 이름만 읽고 무엇이 틀렸는지 알 수 있어야 한다.
- 한 테스트 = 한 동작. `getComments 는 postId 가 일치하는 것만 돌려주고 없으면 빈 리스트다` 처럼 두 동작을 한 이름에 넣지 않는다. 둘로 나눈다.
- 코틀린 한글 백틱 이름은 IDE 가 "Non-ASCII characters" 경고를 낸다. 파일 맨 위에 `@file:Suppress("NonAsciiCharacters")` 를 붙여 끈다. (`BookTest.kt` 에만 있고 나머지 코틀린 테스트에는 빠져 있다.)
- 자바는 백틱 이름을 쓸 수 없으므로 메서드명은 영어 camelCase (`sleepTest`) 로 두고 `@DisplayName` 에 한글 문장을 적는다. `MP_테스트()` 처럼 메서드명에 한글을 넣는 것은 컴파일은 되지만 쓰지 않는다.

### 1-3. 테스트 더블 이름

| 더블              | 규칙                                          | 실습 코드                                           | 자세한 규칙                     |
|-------------------|-----------------------------------------------|-----------------------------------------------------|---------------------------------|
| Fake DataSource   | `Fake<모델명>DataSource`                      | `FakeCommentDataSource`, `FakePokemonDataSource`    | [datasource.md 1장](datasource.md) · [repository.md 4-2 · 4-4](repository.md) |
| Ktor MockEngine   | 최상위 `val mockEngine`, 파일은 `Mocks.kt`    | `Mocks.kt` 의 `mockEngine`                          | 이 문서 3-2                     |
| 응답 전문         | 최상위 `const val jsonString`                 | `FakePokemonDataSource.kt` 의 `jsonString`          | [dto.md 5장](dto.md)            |

- 응답이 둘 이상이 되면 `jsonString`, `mockEngine` 에 대상을 붙여 구분한다 (`pokemonJsonString`, `pokemonMockEngine`). 실습 코드는 응답이 하나라 붙이지 않았다.

---

## 2. 위치 규칙

- 테스트 클래스는 **테스트 소스셋** 에, 검증 대상과 **같은 패키지 이름** 으로 둔다.
  `PokemonRepositoryImpl` 이 `repository` 패키지에 있으면 `PokemonRepositoryImplTest` 도 테스트 소스셋의 `repository` 패키지다.
- 더블(Fake · MockEngine · jsonString)은 **자기 파일** 에, 그것이 흉내내는 대상과 같은 패키지에 둔다. 테스트 클래스 안에 넣지 않는다.
- 자바 테스트도 같다. `HeroTest` 는 `Hero` 와 같은 패키지다.

```
<dayXX>  (test)
├── datasource/
│   ├── FakePokemonDataSource.kt       // Fake + jsonString
│   ├── Mocks.kt                       // mockEngine
│   └── RemotePokemonDataSourceTest.kt
├── mapper/
│   └── ToModelTest.kt
└── repository/
    └── PokemonRepositoryImplTest.kt
```

### 같은 패키지에 두면 생기는 일

- **import 가 줄어든다.** `RemotePokemonDataSourceTest` 는 `PokemonDataSource`, `RemotePokemonDataSource`, `mockEngine`, `jsonString` 을 import 없이 쓴다. 전부 같은 패키지(`datasource`)에 있기 때문이다.
- `internal` 로 선언한 것에 접근할 수 있다. 패키지가 다르면 안 된다.
- 파일 위치만 봐도 어느 코드의 테스트인지 보인다.

### 더블을 자기 파일로 빼면 생기는 일

- `mockEngine` 은 `RemotePokemonDataSourceTest` 와 `PokemonRepositoryImplTest` (주석 처리된 줄) 두 곳에서 쓴다. `Mocks.kt` 로 뺐기 때문에 가능하다.
- 반대 예가 `JsonPlaceHolderTodoDataSourceTest` 다. `val mockEngine` 이 테스트 클래스 **안에** 있어서 다른 테스트가 쓸 수 없다.

---

## 3. 테스트 더블 — 무엇을 어디에 꽂나

> 수업 자료 「Kotlin Test 이론」: 테스트 더블은 "테스트를 진행하기 어려운 경우에 테스트가 가능하도록 만들어주는 객체. 영화 촬영 시 위험한 역할을 대신하는 스턴트 더블에서 비롯되었다." (2쪽)
> "모호한 경계를 가지므로 용어에 집착하지 말자" (3쪽)

### 3-1. 왜 필요한가 — 진짜 저장소 · 진짜 서버로 테스트하면

수업 자료 4쪽의 이유 세 가지 + 실습에서 겪는 것 하나.

| 문제                                      | 실습 코드에서                                                                 |
|-------------------------------------------|-------------------------------------------------------------------------------|
| 테스트 실행 속도가 느려진다               | 포켓몬 API 를 매번 진짜로 부르면 수백 ms. Fake 는 즉시                       |
| 예기치 않은 결과를 돌려주면 통과하던 테스트가 실패한다 | 서버가 `ditto` 의 데이터를 바꾸면 `assertEquals("ditto", ...)` 가 깨진다 |
| 가능한 모든 성공 · 실패 시나리오를 만들기 어렵다 | 진짜 서버에 "500 을 내 달라" 고 할 수 없다. MockEngine 은 `respond(status = ...)` 한 줄 |
| 저장장치가 망가지면 테스트를 할 수 없다 (5쪽) | `items.json` 이 없거나 깨지면 Repository 테스트까지 전부 실패                |

해결은 **"클래스 내부에서 다른 클래스를 생성하기보다 외부에서 받도록 수정"** (13쪽) 하고, **"테스트할 때 원하는 객체를 활용 — 인터페이스 활용"** (6쪽) 하는 것이다. 그래서 Repository 는 DataSource 인터페이스를, DataSource 는 `HttpClient` 를 생성자로 받는다.

### 3-2. 계층별로 무엇을 꽂나

| 검증 대상                        | 더블             | 바꿔 끼우는 자리                        | 실습 코드                                            | 규칙 문서                          |
|----------------------------------|------------------|-----------------------------------------|------------------------------------------------------|------------------------------------|
| **Repository**                   | **Fake**         | 생성자의 `DataSource` 인터페이스        | `FakeCommentDataSource`, `FakePokemonDataSource`     | [repository.md 4장](repository.md) |
| **DataSource (원격)**            | **MockEngine**   | 생성자의 `HttpClient` 안의 엔진         | `Mocks.kt` → `HttpClient(mockEngine)`                | [datasource.md 5장](datasource.md) |
| Mapper                           | 없음             | —                                       | `ToModelTest` 는 DTO 를 직접 만들어 넣는다            | [mapper.md 5장](mapper.md)         |
| 일반 클래스 (`Hero`, `Cleric`, `Book`) | 없음       | —                                       | 의존하는 것이 없으니 바로 `new` 해서 부른다           | 이 문서 5-1                        |

- 두 더블은 **바꿔 끼우는 깊이** 가 다르다. Fake 는 DataSource 를 통째로 바꾸고, MockEngine 은 DataSource 는 진짜를 두고 그 안의 통신 엔진만 바꾼다. 그래서 Repository 테스트에 MockEngine 을 꽂아도 돌아가지만 (`PokemonRepositoryImplTest` 의 주석 처리된 줄), DataSource 테스트에 Fake 를 꽂으면 DataSource 가 테스트에서 빠져 버린다.
- Mockito · MockK 같은 범용 Mock 라이브러리는 쓰지 않는다. (수업 자료 17 · 18쪽에 소개는 되지만 `build.gradle.kts` 에 없다.)

### 3-3. Fake — 두 가지 모양

**로컬 파일 Fake**: `List` 상태 하나와 `load` / `save`. ([repository.md 4-2](repository.md))

```kotlin
// FakeCommentDataSource.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

import com.survivalcoding.<dayXX>.model.Comment

class FakeCommentDataSource: CommentDataSource {
    private var comments: List<Comment> = emptyList()

    override fun loadComments(): List<Comment> {
        return comments
    }

    override fun saveComments(comments: List<Comment>) {
        this.comments = comments
    }
}
```

**원격 API Fake**: 상태 대신 저장해 둔 응답 전문 `jsonString` 을 DTO 로 decode. ([repository.md 4-4](repository.md) 에 원본 전체)

```kotlin
// FakePokemonDataSource.kt  (원본 중 클래스 부분)
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

| 규칙                                            | 로컬 Fake                     | 원격 Fake                                       |
|-------------------------------------------------|-------------------------------|-------------------------------------------------|
| 인터페이스를 구현한다                           | `: CommentDataSource`         | `: PokemonDataSource`                           |
| 상태                                            | `private var comments: List`  | 없음 (`jsonString` 상수)                        |
| 초기 데이터 넣는 법                             | 테스트 `setUp` 에서 `saveComments(...)` | 없음. 항상 같은 DTO                     |
| 외부 라이브러리                                 | 없음                          | `Json` (DTO 를 만들려면 decode 가 필요)         |
| `name` 파라미터                                 | —                             | 무시한다. 무엇을 넣어도 `ditto`                 |
| `delay(2.seconds)`                              | —                             | 네트워크 지연 흉내. `runTest` 에서는 즉시 (4장) |

### 3-4. MockEngine — "이 URL 이 오면 이 응답"

```kotlin
// Mocks.kt  (원본 전체)
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

| 줄                                              | 하는 일                                                                                   |
|-------------------------------------------------|-------------------------------------------------------------------------------------------|
| `MockEngine { request -> ... }`                 | 요청이 올 때마다 이 블록이 불린다. 진짜 네트워크는 없다                                   |
| `when (request.url.toString())`                 | URL 문자열로 분기. **DataSource 가 조립한 URL 이 맞는지** 여기서 같이 검증된다            |
| `respond(content = jsonString)`                 | 본문 `jsonString`, 상태 코드는 기본값 200. `FakePokemonDataSource.kt` 의 상수를 그대로 쓴다 |
| `else -> error("Unhandled ${request.url}")`     | 안 정한 URL 이 오면 **예외로 바로 알린다.** 조용히 빈 응답을 주면 왜 실패하는지 못 찾는다 |

- `import io.ktor.client.engine.mock.MockEngine.Companion.invoke` 는 IDE 가 자동으로 넣은 줄이다. `MockEngine { }` 은 `MockEngine` import 만으로 컴파일된다. 지워도 된다.
- 꽂는 법: `RemotePokemonDataSource(HttpClient(mockEngine))`. `HttpClient` 의 첫 인자가 엔진이다. DataSource 가 `HttpClient` 를 생성자로 받게 해 둔 이유가 이것이다. ([datasource.md 2장](datasource.md))

실패 응답 · 바이너리 응답을 흉내낼 때는 `respond` 의 나머지 인자를 쓴다. 다른 원격 실습의 테스트 안에 예가 있다.

```kotlin
// JsonPlaceHolderTodoDataSourceTest.kt  (원본 중 mockEngine 부분) — 상태 코드 · 헤더 · 바이트 본문까지 지정하는 예
    val mockEngine = MockEngine { request ->
        when (request.url.toString()) {
            "https://test.com/image1.png" -> {
                respond(
                    content = ByteReadChannel(byteArrayOf(1, 2, 3)),
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "image/png")
                )
            }

            "https://test.com/error.png" -> {
                respond(
                    content = ByteReadChannel("Error"),
                    status = HttpStatusCode.NotFound
                )
            }

            else -> {
                respond(
                    content = ByteReadChannel(byteArrayOf()),
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }
```

| `respond` 인자 | 뜻                       | 예                                           |
|----------------|--------------------------|----------------------------------------------|
| `content`      | 본문. `String` 또는 `ByteReadChannel` | `jsonString`, `ByteReadChannel("Error")` |
| `status`       | 상태 코드. 기본 `OK`     | `HttpStatusCode.NotFound`                    |
| `headers`      | 응답 헤더. 기본 없음     | `headersOf("Content-Type", "image/png")`     |

- 이 파일은 `mockEngine` 을 클래스 안에 두었고, DataSource 를 만들지 않은 채 `HttpClient(mockEngine).get()` 만 불러서 **검증 대상이 없다.** 모양은 참고하되 구조는 포켓몬 실습(`Mocks.kt` + `RemotePokemonDataSourceTest`)을 따른다.

---

## 4. 코루틴 테스트 — `runBlocking` 과 `runTest`

`suspend` 함수를 부르는 테스트는 코루틴 블록으로 감싸야 한다. 실습 코드에 두 가지가 있다.

|                   | `runBlocking`                                            | `runTest`                                                   |
|-------------------|----------------------------------------------------------|-------------------------------------------------------------|
| 의존성            | `kotlinx-coroutines-core` (메인에 이미 있다)             | `kotlinx-coroutines-test` (`testImplementation` 에 있다)    |
| import            | `kotlinx.coroutines.runBlocking`                         | `kotlinx.coroutines.test.runTest`                           |
| 시간              | **실제 시간.** `delay(2.seconds)` 면 정말 2초            | **가상 시간.** `delay` 를 즉시 건너뛴다                     |
| 실습 코드         | `RemotePokemonDataSourceTest`, `JsonPlaceHolderTodoDataSourceTest` | `PokemonRepositoryImplTest`                       |
| 쓰는 곳           | `main` (`fun main(): Unit = runBlocking { }`)            | **테스트**                                                  |

- 테스트는 **`runTest`** 로 통일한다. `FakePokemonDataSource` 에 `delay(2.seconds)` 가 있어서 `runBlocking` 으로 돌리면 테스트마다 2초씩 기다린다. `runTest` 는 0초다.
- `RemotePokemonDataSourceTest` 가 `runBlocking` 인 것은 MockEngine 에 `delay` 가 없어서 티가 안 날 뿐이다. 규칙은 `runTest` 다.

### 반환 타입 `: Unit` 을 적는 이유

```kotlin
fun `Pokemon 데이터가 잘 들어와야 한다`(): Unit = runTest {       // O  (PokemonRepositoryImplTest 원본)
fun `포켓몬 데이터가 잘 들어와야 한다`() = runBlocking {           // △ (RemotePokemonDataSourceTest 원본) — 마지막 줄이 assertEquals 라서 우연히 Unit
```

- `= runTest { ... }` 는 **블록의 마지막 식** 이 메서드의 반환값이 된다. 마지막 줄이 `assertEquals(...)` 면 `Unit` 이지만, 값을 돌려주는 식으로 끝나면 메서드가 값을 반환하게 된다.
- JUnit 5 는 **값을 반환하는 메서드를 테스트로 인정하지 않는다.** 그러면 그 테스트는 실행조차 안 되고, 실패도 안 하고, 아무 말도 없다.
- `: Unit` 을 적으면 마지막 줄이 무엇이든 반환값이 `Unit` 으로 고정된다. 항상 적는다.

---

## 5. 계층별 테스트 패턴

### 5-1. 일반 클래스 — 더블 없이 바로 부른다 (자바)

```java
// HeroTest.java  (원본 전체)
package com.survivalcoding.<dayXX>;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {

    @Test
    @DisplayName("sleep 은 hp 를 100으로 만들어야 한다")
    void sleepTest() {
        // given (준비)
//        Hero hero = new Hero();
//        hero.hp = 50;
//
//        hero = null;
//
//        // when (실행)
//        hero.sleep();
//
//        // then (검증)
//        assertEquals(100, hero.hp);
    }
}
```

- **Given / When / Then** 의 자바판이 `// given (준비)` `// when (실행)` `// then (검증)` 이다. 주석 세 줄을 먼저 적고 그 아래를 채운다.
- 본문이 전부 주석이라 이 테스트는 **아무것도 검증하지 않으면서 통과** 한다. 거짓 안심이다. 주석을 풀어 완성하거나, 못 하면 `@Disabled("이유")` 를 붙이거나, 지운다.
- `import java.util.Random` 은 쓰이지 않는다.

```java
// ClericTest.java  (원본 전체)
package com.survivalcoding.<dayXX>;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClericTest {

    @Test
    @DisplayName("mp 타당성 검사")
    void MP_테스트() {
        Cleric cleric = new Cleric("오준석");

        assertThrows(IllegalArgumentException.class, () -> cleric.setMp(-1));
    }

//    @RepeatedTest(20)
//    @DisplayName("시나리오 1: pray를 3초 하면 3~5가 회복되어야 함")
//    void pray_threeSeconds_recoversBetween3And5() {
//        Cleric cleric = new Cleric();
//        cleric.setMp(0); // 최대치 도달에 따른 제한을 피하기 위해 0으로 세팅
//
//        int recoveredMp = cleric.pray(3);
//
//        // 3초 + (0~2) 보정 = 회복량은 3 이상 5 이하
//        assertTrue(recoveredMp >= 3 && recoveredMp <= 5,
//                "회복량은 3~5 사이여야 합니다. 실제 회복량: " + recoveredMp);
//        assertEquals(recoveredMp, cleric.mp, "실제 mp 필드에도 회복량이 정확히 반영되어야 합니다.");
//    }
//
//    @Test
//    @DisplayName("시나리오 2: mp가 5일 때 selfAid를 하면 hp가 maxHp가 되어야 함")
//    void selfAid_whenMpIs5_recoversHpToMax() {
//        Cleric cleric = new Cleric();
//        cleric.hp = 10; // 체력이 깎인 상태
//        cleric.mp = 5;  // 정확히 소모량만큼 남은 상태
//
//        cleric.selfAid();
//
//        assertEquals(cleric.maxHp, cleric.hp, "hp가 maxHp로 회복되어야 합니다.");
//        assertEquals(0, cleric.mp, "mp는 5가 소모되어 0이 되어야 합니다.");
//    }
}
```

| 줄                                                             | 배울 것                                                                                     |
|----------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| `assertThrows(IllegalArgumentException.class, () -> cleric.setMp(-1))` | **예외가 나야 정상** 인 경우. 예외 종류와, 예외를 내는 코드를 람다로 넘긴다. 람다로 안 싸면 `assertThrows` 에 들어가기 전에 예외가 터진다 |
| `@RepeatedTest(20)` (주석)                                      | 결과에 **난수** 가 섞이면 한 번 통과로는 부족하다. 20번 돌려서 전부 범위 안인지 본다         |
| `assertTrue(조건, "메시지")` (주석)                             | 범위 검사는 `assertTrue`. 세 번째 인자 메시지가 실패 때 찍힌다                              |
| `assertEquals(recoveredMp, cleric.mp, "메시지")` (주석)         | 반환값과 **실제 필드** 둘 다 본다. 하나만 보면 반쪽 검증이다                                 |
| `void MP_테스트()`                                              | 메서드명에 한글. 1-2 대로 `mpTest` + `@DisplayName` 이 맞다                                 |
| `import ...RepeatedTest`                                        | 주석 안에서만 쓰여서 지금은 안 쓰는 import 다                                               |

```java
// BookTest.java  (원본 전체)
package com.survivalcoding.instance_basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    @DisplayName("제목과 출간일(예: 2024-01–01)이 같으면 같은 책으로 판단한다. 또한 List, Map, Set 등의 컬렉션에 넣어도 동일 객체로 판단한다.")
    void equalsTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-01 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Date date2 = sdf.parse("2024-01-01 13:00:00");

        System.out.println(date1);
        System.out.println(date2);

        System.out.println(date1.equals(date2));    // false

        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜
        Book book2 = new Book("생존코딩", date2, "구웃");        // 지금 날짜

        assertEquals(book1, book2);

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);

        assertEquals(1, books.size());
    }

    @Test
    @DisplayName("Book 인스턴스를 담고 있는 컬렉션에 대해 Collections.sort() 를 사용하면 출간일이 신상 순서대로 정렬된다")
    void sortTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-02 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Date date2 = sdf.parse("2024-01-01 13:00:00");

        List<Book> books = new ArrayList<>();

        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜
        Book book2 = new Book("생존코딩", date2, "구웃");        // 지금 날짜
        books.add(book1);
        books.add(book2);

        Collections.sort(books);

        assertEquals(book1, books.get(0));
    }

    @Test
    @DisplayName("clone() 메서드를 제공하고, 깊은 복사를 수행한다")
    void cloneTest() throws ParseException  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = sdf.parse("2024-01-02 14:30:00");    // 미친듯한 성능의 pc에서 무진장 빠르게 2개 코드가 실행되면서
        Book book1 = new Book("생존코딩", date1, "구웃");        // 지금 날짜

        Book book2 = book1.clone();

        System.out.println(date1);
    }
}
```

| 테스트        | 검증하는 약속                                   | 핵심 assert                                      | 비고                                                                 |
|---------------|-------------------------------------------------|--------------------------------------------------|----------------------------------------------------------------------|
| `equalsTest`  | `equals` 가 제목 · 출간일(날짜까지)로 판단한다  | `assertEquals(book1, book2)`                     | 시각이 달라도(14:30 vs 13:00) 같은 날이면 같은 책                    |
|               | `hashCode` 도 같이 맞춰서 `Set` 에 하나만 남는다 | `assertEquals(1, books.size())`                 | `equals` 만 고치고 `hashCode` 를 안 고치면 여기서 깨진다             |
| `sortTest`    | `Comparable` 로 최신 출간일이 앞에 온다         | `assertEquals(book1, books.get(0))`              | `date1` (01-02) 이 `date2` (01-01) 보다 앞                            |
| `cloneTest`   | `clone()` 이 깊은 복사다                        | **없음**                                         | `println` 뿐. 복사본을 바꿨을 때 원본이 안 바뀌는지 `assert` 가 있어야 한다 |

- `System.out.println` 은 검증이 아니다. 눈으로 보는 것은 사람이 매번 해야 하고, `./gradlew test` 는 그것을 읽지 않는다. 확인하고 싶은 값은 전부 `assert` 로 적는다.
- `@DisplayName` 에 긴 문장도 괜찮다. 깨졌을 때 이 문장이 리포트에 나온다.
- `throws ParseException` — 테스트 메서드도 검사 예외를 던질 수 있다. `try/catch` 로 감싸서 숨기지 않는다.

```kotlin
// BookTest.kt  (원본 전체)
@file:Suppress("NonAsciiCharacters")

package com.survivalcoding.kotlin_basic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BookTest {

    @Test
    fun `Book 은 TangibleAsset 이다`() {
        // ctrl + space
        val book: TangibleAsset = Book(
            name = "생존코딩",
            price = 1000,
            color = "red",
            isbn = "1111",
            weight = 30.0,
        )

        assertEquals("생존코딩", book.name)
    }
}
```

- `val book: TangibleAsset = Book(...)` — "`Book` 은 `TangibleAsset` 이다" 를 **타입 선언으로 검증** 한다. 상속 관계가 깨지면 이 줄이 컴파일 에러다. 컴파일이 곧 테스트다.
- 코틀린 테스트 파일 맨 위 `@file:Suppress("NonAsciiCharacters")` 의 자리가 여기다. `package` 보다 위다.

### 5-2. Mapper — 입력 하나, 출력 하나

DTO 를 직접 만들어 `toModel()` 을 부르고 모델 필드를 비교한다. 더블도 코루틴도 없다. 정상 케이스 하나, `null` 케이스 하나가 기본 세트다.
원본 전체와 규칙은 [mapper.md 5장](mapper.md).

```kotlin
// ToModelTest.kt  (원본 중 핵심)
        val dto = PokemonDto(
            id = 1,
            name = "name",
        )

        val model: Pokemon = dto.toModel()

        assertEquals(dto.id, model.id)
        assertEquals(dto.name, model.name)
```

### 5-3. Repository — Fake 를 꽂고 가공 결과를 본다

```kotlin
// CommentRepositoryImplTest.kt  (원본 전체)
package com.survivalcoding.<dayXX>.repository

import com.survivalcoding.<dayXX>.data_source.CommentDataSource
import com.survivalcoding.<dayXX>.data_source.FakeCommentDataSource
import com.survivalcoding.<dayXX>.model.Comment
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CommentRepositoryImplTest {
    private lateinit var dataSource: CommentDataSource
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakeCommentDataSource()
        dataSource.saveComments(
            listOf(
                Comment(
                    postId = 1,
                    id = 1,
                    name = "id labore ex et quam laborum",
                    email = "Eliseo@gardner.biz",
                    body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium",
                ),
                Comment(
                    postId = 1,
                    id = 2,
                    name = "quo vero reiciendis velit similique earum",
                    email = "Jayne_Kuhic@sydney.com",
                    body = "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et",
                ),
                Comment(
                    postId = 1,
                    id = 3,
                    name = "odio adipisci rerum aut animi",
                    email = "Nikita@garfield.biz",
                    body = "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione",
                ),
            )
        )

        commentRepository = CommentRepositoryImpl(dataSource)
    }

    @Test
    fun getComments() {
        val comments = commentRepository.getComments(postId = 1)

        assertEquals(3, comments.size)
        assertEquals(listOf(1, 2, 3), comments.map { it.id })
    }

    @Test
    fun getComments_returnsEmpty_whenPostIdNotExists() {
        val comments = commentRepository.getComments(postId = 2)

        assertTrue(comments.isEmpty())
    }

}
```

| 줄                                                      | 규칙                                                                                           |
|---------------------------------------------------------|------------------------------------------------------------------------------------------------|
| `private lateinit var dataSource: CommentDataSource`    | 더블도 **인터페이스 타입** 으로 선언. `FakeCommentDataSource` 로 선언하면 Fake 전용 메서드를 쓰고 싶어진다 |
| `@BeforeEach fun setUp()`                               | 테스트마다 **새 Fake, 새 Repository.** 한 테스트의 변경이 다음 테스트로 안 넘어간다 (독립성)   |
| `dataSource.saveComments(listOf(...))`                  | Given. Fake 에 데이터를 넣는 방법은 인터페이스의 `save` 다. Fake 에 별도 메서드를 만들지 않는다 |
| `Comment(postId = 1, id = 1, name = ..., email = ..., body = ...)` | 테스트 데이터는 **모델의 실제 필드** 로만. 없는 필드를 쓰면 컴파일 에러                 |
| `commentRepository.getComments(postId = 1)`             | When. Repository 의 공개 메서드 하나                                                           |
| `assertEquals(listOf(1, 2, 3), comments.map { it.id })` | Then. 개수만이 아니라 **어떤 것이 들어왔는지** `id` 목록으로 본다                              |
| `getComments(postId = 2)` → `assertTrue(comments.isEmpty())` | 조건이 있는 메서드는 **있을 때 / 없을 때** 두 테스트                                      |

- 테스트 이름 `getComments` 는 무엇을 기대하는지 없다. `` `getComments 는 postId 가 일치하는 것만 돌려준다` `` 로 바꾼다. 같은 파일의 `getComments_returnsEmpty_whenPostIdNotExists` 와 스타일도 맞춘다. (1-2)
- 원격 API Repository 테스트 (`PokemonRepositoryImplTest`) 는 여기에 `runTest` 와 원격 Fake 가 더해진 것이다. 원본 전체와 줄별 설명은 [repository.md 4-4](repository.md).

### 5-4. DataSource (원격) — MockEngine 을 꽂고 DTO 변환을 본다

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

| 줄                                                              | 규칙                                                                                   |
|-----------------------------------------------------------------|----------------------------------------------------------------------------------------|
| `RemotePokemonDataSource(HttpClient(mockEngine))`               | **진짜 DataSource** 에 가짜 엔진. DataSource 의 URL 조립 · `bodyAsText` · decode 가 전부 실행된다 |
| `val dataSource: PokemonDataSource = ...`                        | 인터페이스 타입 선언                                                                   |
| `dataSource.getPokemon("ditto")`                                | `mockEngine` 이 아는 URL (`.../pokemon/ditto`) 이 만들어져야 응답이 온다. 다른 이름이면 `error("Unhandled ...")` |
| `assertEquals(jsonString, response.body)`                       | **[발견한 문제]** `jsonString` 은 `String`, `response.body` 는 `PokemonDto?`. 타입이 달라 항상 실패한다 |

DataSource 가 돌려주는 것은 DTO 다 ([datasource.md 3-2](datasource.md)). 그러니 비교도 DTO 의 필드로 한다.

```kotlin
// 고친 예 (원본에 없음) — runTest, : Unit, DTO 필드 비교
    @Test
    fun `포켓몬 데이터가 잘 들어와야 한다`(): Unit = runTest {
        val dataSource: PokemonDataSource = RemotePokemonDataSource(HttpClient(mockEngine))

        val response = dataSource.getPokemon("ditto")

        assertEquals(200, response.statusCode)
        assertEquals(132, response.body?.id)
        assertEquals("ditto", response.body?.name)
    }
```

- `statusCode` 도 같이 본다. DataSource 가 상태 코드를 그대로 옮겨 담는지가 DataSource 의 약속이다.
- 실패 응답 테스트를 추가하려면 `Mocks.kt` 에 `"https://pokeapi.co/api/v2/pokemon/없는이름" -> respond(content = "Not Found", status = HttpStatusCode.NotFound)` 분기를 더하고, decode 예외가 나는지 `assertThrows` 로 본다. (원본에는 없다.)

---

## 6. 공통 작성 규칙

### 6-1. Given / When / Then

| 블록  | 뜻                 | 자바 주석 (HeroTest) | 코틀린에서는                                            |
|-------|--------------------|----------------------|---------------------------------------------------------|
| Given | 준비               | `// given (준비)`    | `@BeforeEach setUp()` 또는 테스트 본문 첫 블록          |
| When  | 실행               | `// when (실행)`     | 검증 대상 메서드 호출 **한 줄**                          |
| Then  | 검증               | `// then (검증)`     | `assert...` 줄들                                        |

- 세 블록 사이를 빈 줄로 띄운다. `CommentRepositoryImplTest`, `ToModelTest`, `PokemonRepositoryImplTest` 전부 이 모양이다.
- When 이 두 줄 이상이면 테스트가 두 개 섞인 것이다. 나눈다.

### 6-2. assert — 무엇으로 비교하나

| assert                                   | 쓰는 때                           | 실습 코드                                                     | 주의                                                        |
|------------------------------------------|-----------------------------------|---------------------------------------------------------------|-------------------------------------------------------------|
| `assertEquals(expected, actual)`         | 값 비교. 가장 기본                | `assertEquals(3, comments.size)`, `assertEquals("ditto", pokemon.name)` | **기대값이 앞, 실제값이 뒤.** 바꾸면 실패 메시지가 거꾸로 나온다 |
| `assertEquals(listOf(...), x.map { })`   | 목록의 내용 · 순서 비교           | `assertEquals(listOf(1, 2, 3), comments.map { it.id })`       | 순서가 상관없으면 `.toSet()` 으로 비교                      |
| `assertTrue(조건)` / `assertTrue(조건, 메시지)` | 참 · 거짓, 범위                | `assertTrue(comments.isEmpty())`, `assertTrue(recoveredMp >= 3 && ...)` | 메시지를 넣어야 왜 실패했는지 보인다                   |
| `assertNull(x)`                          | `null` 이어야 할 때               | ([repository.md 5-5](repository.md))                           |                                                             |
| `assertNotNull(x)` (코틀린용)            | `null` 이 아니어야 하고, 그 뒤에 `x.필드` 를 쓸 때 | `assertNotNull(pokemon)` 뒤 `pokemon.name`     | `org.junit.jupiter.api.assertNotNull` 을 import 해야 스마트 캐스트. `Assertions.assertNotNull` 은 안 된다 |
| `assertThrows(예외.class) { }`           | 예외가 나야 정상일 때             | `assertThrows(IllegalArgumentException.class, () -> cleric.setMp(-1))` | 예외 내는 코드를 람다로 싼다                       |
| `assertEquals(book1, book2)`             | 객체 동등성 (`equals`)            | `BookTest.equalsTest`                                         | `==` 가 아니라 `equals` 를 본다. `hashCode` 는 `Set` 으로 따로 |

- 타입이 다른 것끼리 `assertEquals` 하면 **컴파일은 되지만 항상 실패** 한다 (`RemotePokemonDataSourceTest`). 기대값과 실제값의 타입이 같은지 먼저 본다.
- 검증은 **결과 값** 으로 한다. "메서드가 몇 번 불렸는가" 는 보지 않는다. 구현을 바꿔도 테스트가 살아남게 하기 위해서다.

### 6-3. import

| 언어   | 허용                                                                 | 금지                                           |
|--------|----------------------------------------------------------------------|------------------------------------------------|
| Kotlin | `org.junit.jupiter.api.Test` / `BeforeEach` / `Assertions.*` / `assertNotNull`, `kotlinx.coroutines.test.runTest` | `org.junit.Test`, `org.junit.Before` (JUnit 4 — 의존성에 없다), `kotlinx.coroutines.runBlocking` (테스트에서) |
| Java   | `org.junit.jupiter.api.*`, `import static org.junit.jupiter.api.Assertions.*` | `org.junit.*` (JUnit 4)                    |

- 안 쓰는 import 는 커밋 전에 지운다. (`HeroTest` 의 `Random`, `ClericTest` 의 `RepeatedTest`, `PokemonRepositoryImplTest` 의 4줄, `Mocks.kt` 의 `Companion.invoke`)

### 6-4. 좋은 단위 테스트의 조건 — FRIEND (수업 자료 19쪽)

| 글자 | 조건                                | 실습 코드에서 지키는 방법                                           |
|------|-------------------------------------|---------------------------------------------------------------------|
| **F** | Fast (빠르고)                      | Fake · MockEngine 으로 파일 · 네트워크를 없앤다. `runTest` 로 `delay` 를 건너뛴다 |
| **R** | Reliable (믿을 수 있고)            | 진짜 서버 · 난수 · 현재 시각에 기대지 않는다. 난수가 있으면 `@RepeatedTest` |
| **I** | Independent (독립적인)             | `@BeforeEach` 로 매번 새로 만든다. 테스트 순서가 바뀌어도 결과가 같다 |
| **E** | Ease of Maintenance (유지 관리가 쉽고) | 이름이 문장이라 깨지면 바로 안다. 더블은 자기 파일에 있어 고칠 곳이 하나다 |
| **N** | Nearly compacted code (거의 압축적인 코드) | Given / When / Then 세 블록. When 은 한 줄                     |
| **D** | Dependencies should be less (의존성이 적어야 한다) | Mock 라이브러리 없음. JUnit · coroutines-test · ktor-client-mock 뿐 |

### 6-5. 실행과 의존성

- 실행: `./gradlew test`. IDE 에서 `@Test` 옆 화살표로 하나씩 돌려도 된다.
- `build.gradle.kts` 의 테스트 관련 줄 (원본 그대로):

```kotlin
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    testImplementation("io.ktor:ktor-client-mock:3.6.0")
```

```kotlin
tasks.test {
    useJUnitPlatform()
}
```

| 줄                                   | 역할                                                    |
|--------------------------------------|---------------------------------------------------------|
| `junit-bom` + `junit-jupiter`        | JUnit 5. `@Test`, `@BeforeEach`, `Assertions`           |
| `junit-platform-launcher`            | Gradle 이 테스트를 찾아 실행하는 데 필요                |
| `kotlinx-coroutines-test`            | `runTest`                                               |
| `ktor-client-mock`                   | `MockEngine`, `respond`                                 |
| `useJUnitPlatform()`                 | 없으면 JUnit 5 테스트가 실행되지 않는다                 |

---

## 7. 발견한 문제 정리 (코드는 고치지 않았다)

| # | 파일                              | 문제                                                        | 고치는 방향                                                       |
|---|-----------------------------------|-------------------------------------------------------------|-------------------------------------------------------------------|
| ① | `RemotePokemonDataSourceTest.kt`  | `String` 과 `PokemonDto` 비교 → 항상 실패                   | `response.body?.name` 등 DTO 필드로 비교 (5-4 고친 예)             |
| ② | `HeroTest.java`                   | 본문 전부 주석. 빈 테스트가 항상 통과                       | 완성하거나 `@Disabled` 또는 삭제                                   |
| ③ | `BookTest.java` `cloneTest`       | `assert` 없음, `println` 뿐                                  | 복사본 수정 후 원본 불변을 `assert`                                |
| ④ | `JsonPlaceHolderTodoDataSourceTest.kt` | 검증 대상(DataSource) 없음. `mockEngine` 이 클래스 안. 이름 `test` | `Mocks.kt` 로 빼고 `JsonPlaceHolderTodoDataSource` 를 만들어 검증 |
| ⑤ | `CommentRepositoryImplTest.kt`    | 테스트 이름 `getComments` — 기대 동작 없음. 스타일 혼용     | 백틱 한글 문장으로 통일                                            |
| ⑥ | `ToModelTest.kt`                  | `imageUrl` (중첩 접근) 미검증                               | `sprites = null` 케이스 추가 ([mapper.md 5-2](mapper.md))          |
| ⑦ | `PokemonRepositoryImplTest.kt`    | 안 쓰는 import 4줄, 주석 처리된 줄                          | 삭제                                                               |
| ⑧ | `RemotePokemonDataSourceTest.kt`, `JsonPlaceHolderTodoDataSourceTest.kt` | `runBlocking`, `: Unit` 없음          | `(): Unit = runTest { }`                                           |
| ⑨ | `ClericTest.java`                 | 메서드명 `MP_테스트` 한글. `RepeatedTest` import 미사용     | `mpTest` + `@DisplayName`. import 정리                            |
| ⑩ | 코틀린 테스트 전반                | `@file:Suppress("NonAsciiCharacters")` 가 `BookTest.kt` 에만 | 한글 백틱 이름을 쓰는 모든 파일 맨 위에                            |

---

## 8. 커밋 전 체크리스트

- [ ] 클래스 이름이 `<검증 대상>Test` 이고, Repository · DataSource 는 `<구현체명>Test` (`...ImplTest`, `Remote...Test`) 인가
- [ ] 테스트 소스셋의 **같은 패키지** 에 있는가. 더블(Fake · `Mocks.kt` · `jsonString`)은 자기 파일인가
- [ ] 메서드 이름이 "무엇이 어떻게 되어야 한다" 문장인가. 코틀린은 백틱 + `@file:Suppress("NonAsciiCharacters")`, 자바는 `@DisplayName` 인가
- [ ] 한 테스트에 When 이 한 줄인가. Given / When / Then 이 빈 줄로 나뉘는가
- [ ] Repository 테스트는 Fake, DataSource 테스트는 `HttpClient(mockEngine)` 인가. Mock 라이브러리가 없는가
- [ ] 더블 · Repository 변수가 **인터페이스 타입** 으로 선언됐는가
- [ ] `suspend` 를 부르는 테스트가 `(): Unit = runTest { }` 인가 (`runBlocking` 아님)
- [ ] `@BeforeEach` 로 테스트마다 새로 만드는가
- [ ] `assertEquals` 의 기대값이 앞, 실제값이 뒤이고, 두 타입이 같은가
- [ ] `assert` 없는 테스트, 본문이 전부 주석인 테스트, `println` 만 있는 테스트가 없는가
- [ ] 조건이 있는 메서드는 있을 때 / 없을 때 두 테스트가 있는가. 예외 케이스는 `assertThrows` 인가
- [ ] 테스트 데이터가 모델의 실제 필드로만 만들어졌는가. 원격은 `jsonString` 이 실제 응답 전문인가
- [ ] 안 쓰는 import · 주석 처리된 코드가 없는가
- [ ] `./gradlew test` 가 통과하는가
