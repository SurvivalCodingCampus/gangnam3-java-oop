---
name: repository-pattern
description: Rules and templates for this project's Kotlin DataSource / DTO / Mapper / Repository / Fake / Test layering, covering both the local-file style (ItemDataSource → Repository) and the remote-API style (Ktor DataSource → Response<DTO> → Mapper → Repository). Use this whenever the user asks to create, review, fix, rename or refactor a Repository, RepositoryImpl, DataSource, DTO, Mapper (toModel), Fake DataSource, MockEngine, or any test in this repo, or mentions 리포지토리, 데이터소스, DTO, 매퍼, toModel, Response, 원격 API, Ktor, Fake, MockEngine, 테스트, 패키지 구조, DI, 책임 분리. Also use it when checking existing code against the project conventions, even if the user only says "원칙대로 고쳐줘" or "구조 확인해줘".
---

# Repository 패턴 작성 가이드

이 저장소의 `DataSource → (DTO → Mapper) → Repository → Test` 계층을 만들거나 고칠 때 따르는 규칙이다.
세부 규칙과 전체 코드 템플릿은 `references/` 아래 다섯 문서에 있다. 이 파일은 "언제 어떤 문서를 읽고, 무엇을 반드시 지키는지" 만 정리한다.

두 가지 구조가 있다. 어느 쪽인지 먼저 가린다.

| 구조          | 흐름                                                                     | DataSource 가 돌려주는 것 | 더 있는 계층    |
|---------------|--------------------------------------------------------------------------|---------------------------|-----------------|
| 로컬 파일     | `FileItemDataSource` → `List<Item>` → `ItemRepositoryImpl`               | 모델                      | 없음            |
| 원격 API      | `RemotePokemonDataSource` → `Response<PokemonDto>` → `PokemonRepositoryImpl` → `dto.toModel()` → `Pokemon` | DTO 를 담은 `Response` | `dto`, `mapper` |

## 참조 문서

| 문서                                                   | 언제 읽는가                                                                                                          |
|--------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| [references/datasource.md](references/datasource.md)   | DataSource 인터페이스 · 구현체 · `Response` 를 만들거나 고칠 때. 로컬/원격 비교 (0장), 네이밍, 캡슐화, 반환 타입 (3장), Ktor Remote 템플릿 (4-2), DB 템플릿 (4-3), MockEngine 테스트 (5장). |
| [references/dto.md](references/dto.md)                 | 원격 API 응답을 `@Serializable` DTO 로 옮길 때. 모델과 DTO 의 분리 이유 (0장), 필드 규칙 (2장), 중첩 분리 (3장), 새 DTO 순서 (5장). |
| [references/mapper.md](references/mapper.md)           | `fun <모델명>Dto.toModel()` 확장 함수를 만들거나 고칠 때. 변환 규칙 (2장), 누가 부르나 (4장), `ToModelTest` (5장).  |
| [references/repository.md](references/repository.md)   | Repository 인터페이스 · 구현체 · Fake · 테스트 · Main 조립을 만들거나 고칠 때. 원격 읽기 패턴 (3-3), 원격 Fake · `runTest` (4-4), `Item` 전체 세트 (5-1~5-6), `Pokemon` 세트 (5-7). |
| [references/test.md](references/test.md)               | 테스트 클래스 이름 · 위치, Fake 와 MockEngine 의 역할 분리 (3장), `runBlocking` vs `runTest` (4장), 계층별 테스트 패턴 (5장), assert · import 규칙 (6장). |

작업 범위에 맞는 문서만 읽는다. 예를 들어 Repository 의 정렬 로직만 고친다면 repository.md 3장만 보면 된다.
다섯 문서는 서로 상대 링크로 연결되어 있으므로, 한 문서가 다른 문서의 장을 가리키면 그 장을 이어서 읽는다.

문서 표기 규칙: 파일은 이름만 적는다 (경로 없음). 수업 날짜 패키지명은 본문에 쓰지 않고, 코드 발췌의 `package` · `import` 줄에서만 `<dayXX>` 로 적는다. 패키지는 `datasource` 로 통일해 적되, 실습 코드 일부의 실제 패키지명은 `data_source` 이므로 코드 발췌는 원본 그대로 둔다.

## 새 Repository 를 만드는 순서

**로컬 파일**
1. `model` 과 `datasource` (인터페이스 + 기술 구현체) 를 datasource.md 4장 템플릿대로 먼저 만든다.
2. `repository` 패키지에 `<모델명>Repository.kt` 인터페이스. import 는 모델만.
3. `repository` 패키지에 `<모델명>RepositoryImpl.kt` 구현체. 생성자는 `<모델명>DataSource` 인터페이스 하나.
4. 테스트 소스셋 `datasource` 패키지에 `Fake<모델명>DataSource.kt`. In-Memory `List` 하나를 들고 `load` / `save` 만 구현한다.
5. 테스트 소스셋 `repository` 패키지에 `<모델명>RepositoryImplTest.kt`. 공개 메서드마다 테스트 하나.
6. `Main.kt` 에서 인터페이스 타입으로 조립한다.
7. `./gradlew test` 를 실제로 돌려 통과를 확인한 뒤에 끝났다고 보고한다.

**원격 API** — 위 순서에 세 단계가 끼어든다.
- 1 앞에: 응답 JSON 을 한 번 받아 저장하고 (`jsonString`), `dto` 패키지에 `<모델명>Dto.kt` 를 만든다 (dto.md 5장). 모델은 **순수 코틀린** (`@Serializable` 없음).
- 1 에서: DataSource 는 `suspend fun get<모델명>(키): Response<<모델명>Dto>`. 구현체는 `HttpClient` 를 생성자로 받고 (`= HttpClientFactory.create()`), `Response.kt` 를 같은 패키지에 둔다 (datasource.md 4-2).
- 2 앞에: `mapper` 패키지에 `fun <모델명>Dto.toModel(): <모델명>` (mapper.md 6장).
- 3 에서: 구현체는 `suspend`, 본문은 `response.body ?: return null` → `dto.toModel()` → (가공), 전체를 `try/catch (Exception) → null` (repository.md 3-3).
- 4 에서: Fake 는 `jsonString` 을 decode 해 `Response` 에 담아 돌려준다 (repository.md 4-4).
- 5 에서: 테스트는 `(): Unit = runTest { }`. DataSource 테스트는 따로 `Remote<모델명>DataSourceTest` 에 `HttpClient(mockEngine)` 으로 (test.md 5-4). Mapper 테스트는 `ToModelTest` (mapper.md 5장).

repository.md 5장에 `Item` 기준 전체 세트(5-1~5-6)와 `Pokemon` 기준 원격 세트(5-7)가 있다. 처음부터 새로 쓰지 말고 그 템플릿에서 출발한다.

## 반드시 지키는 것

각 규칙의 이유는 참조 문서에 있다. 여기서는 위반이 잦은 것만 모았다.

**패키지와 이름**
- `datasource`, `dto`, `mapper`, `model`, `repository` 는 전부 **형제 패키지** 다. 하나를 다른 하나 아래에 넣지 않는다.
- 이름은 `<모델명>DataSource`, `<기술명><모델명>DataSource` (`File…`, `Remote…`), `<모델명>Dto`, `<모델명>Mapper.kt` 의 `toModel()`, `<모델명>Repository`, `<모델명>RepositoryImpl`, `Fake<모델명>DataSource`, `Response<T>`.
  Repository 에는 기술명을 붙이지 않는다. 인터페이스에는 `Remote` 를 붙이지 않는다.
- 테스트 클래스는 **`<검증 대상>Test`** — Repository · DataSource 는 구현체 이름을 쓴다 (`PokemonRepositoryImplTest`, `RemotePokemonDataSourceTest`). 확장 함수는 함수 이름 (`ToModelTest`).
- 테스트 패키지는 메인 패키지와 똑같이 맞춘다. Fake · `Mocks.kt` · `jsonString` 은 테스트 소스셋에, 자기 파일로 둔다.

**의존성**
- Repository 생성자는 `private val dataSource: <모델명>DataSource` 한 줄이다. 기본값을 주지 않고, 구현체 타입이나 `File` · `Json` · `HttpClient` 같은 기술 타입을 받지 않는다. `HttpClient` 를 받는 것은 DataSource 다 (기본값 허용).
- Repository 파일의 import 에 `java.io`, `kotlinx.serialization`, `java.net`, `io.ktor`, `java.sql`, `Response`, 구현체 이름이 보이면 책임이 잘못 올라온 것이다. 원격이면 `dto.<모델명>Dto` 와 `mapper.toModel` 두 줄만 더 있을 수 있다.
- DTO 파일의 import 는 `kotlinx.serialization` 두 줄뿐, Mapper 파일의 import 는 DTO 와 모델 두 줄뿐이다. 모델은 외부 라이브러리를 import 하지 않는다 (원격 구조).
- 구현체를 고르는 곳은 `main` 한 줄뿐이다. 변수는 인터페이스 타입으로 선언한다. `main` 은 모델만 받고 DTO · `Response` 를 모른다.

**책임 분리**
- DataSource 는 읽고 쓴다 (`load` / `save`, 파싱, 타입 변환, 원격이면 `bodyAsText` → `decodeFromString<DTO>` → `Response` 포장). Repository 는 가공한다 (정렬, 필터, 검색, 단건 찾기, 상위 N, 비즈니스 규칙, 원격이면 `body` null 판단 · `toModel()` 호출 · 예외 → `null`). Mapper 는 고르고 · 이름 바꾸고 · 기본값 채우기만 한다.
- 원격 DataSource 는 **DTO 까지만** 돌려준다. `body` 에 `String` 이나 모델을 넣지 않는다. 예외는 잡지 않고 Repository 로 올린다.
- DTO 는 서버 JSON 을 그대로 옮긴다. 모든 필드 `@SerialName` + nullable + `= null`, 중첩 객체마다 `@Serializable data class`, 가공 없음. `Json { ignoreUnknownKeys = true }` 와 짝이다.
- Mapper 는 `fun <모델명>Dto.toModel(): <모델명>` 최상위 확장 함수. 필드마다 `?:` 기본값 (`0`, `""`), `!!` 없음, 계산 · 조건 · 정렬 없음, `suspend` 아님. 부르는 곳은 Repository 구현체 한 곳뿐이다.
- 읽기 메서드는 매번 `dataSource.load...()` / `get...()` 로 시작한다. Repository 는 상태를 들고 있지 않는다. 단건 조회는 `?` 로 `null` 을 돌려준다. 반환은 `List`, `MutableList` 가 아니다.
- 쓰기 메서드는 **읽기 → 바꾸기 → 전체 저장** 세 단계다. DataSource 에 `appendItem`, `deleteById` 같은 메서드를 추가하지 않는다.
- `id` 파라미터 타입은 모델의 `id` 타입과 같다. 원격은 DataSource 가 받는 키와 같은 키를 쓴다 (`getPokemon(name: String)`). `suspend` 는 인터페이스 · 구현체 양쪽에 붙인다. 인터페이스의 모든 메서드는 구현체에 있어야 한다.

**테스트**
- **Repository 테스트는 Fake, DataSource 테스트는 Ktor `MockEngine`.** Mockito · MockK 는 쓰지 않는다. 테스트 의존성은 JUnit Jupiter, `kotlinx-coroutines-test`, `ktor-client-mock` 이다.
- 테스트 클래스는 아무것도 상속하지 않는다. `org.junit.jupiter.api` 의 `BeforeEach` / `Test` / `Assertions` / `assertNotNull` 만 쓴다 (JUnit 4 의 `org.junit.Before` 는 없다).
- `suspend` 를 부르는 테스트는 `(): Unit = runTest { }` 다. `runBlocking` 이 아니고, `: Unit` 을 빼지 않는다.
- 테스트 데이터는 모델에 실제로 있는 필드로만 만든다. 원격 Fake 의 `jsonString` 은 실제 응답 전문이다.
- 테스트 이름은 "무엇이 어떻게 되어야 한다" 문장. 코틀린은 백틱 한글 + `@file:Suppress("NonAsciiCharacters")`, 자바는 `@DisplayName`. `test()`, `getComments()` 같은 이름은 쓰지 않는다.
- `assertEquals(기대값, 실제값)` 순서를 지키고, 두 타입이 같은지 본다. `assert` 없는 테스트, 본문이 전부 주석인 테스트, `println` 만 있는 테스트는 두지 않는다.

## 기존 코드를 점검할 때

점검 요청이면 고치기 전에 먼저 아래 순서로 본다. 발견한 문제는 "파일 · 위반 규칙 · 참조 문서의 장" 세 가지로 보고한다.

1. 로컬 구조인지 원격 구조인지 (DataSource 반환 타입이 모델인지 `Response<DTO>` 인지)
2. 디렉터리 구조와 이름 (위의 "패키지와 이름")
3. 각 파일의 import 목록 — Repository (repository.md 2-2 표), DTO (dto.md 1-3), Mapper (mapper.md 3장), DataSource 인터페이스 (datasource.md 2장)
4. 생성자 시그니처와 `main` 의 조립 방식
5. 인터페이스 ↔ 구현체 메서드 시그니처 일치 여부 (`suspend` 포함)
6. DataSource 에 가공 로직이, Repository 에 I/O · 파싱이, Mapper 에 계산 · 조건이 섞여 있지 않은지 (repository.md 3-1 책임 표)
7. 원격이면 `body ?: return null` → `toModel()` → `try/catch → null` 순서인지 (repository.md 3-3)
8. 테스트가 컴파일되고 통과하는지. 컴파일이 안 되면 그 원인부터 적는다. 통과해도 `assert` 가 비어 있거나 타입이 다른 비교가 아닌지 본다 (test.md 7장에 알려진 문제 10개가 있다).

## 끝내기 전 체크리스트

각 문서 마지막 장의 체크리스트 (datasource.md 6장, dto.md 6장, mapper.md 7장, repository.md 6장, test.md 8장) 를 그대로 적용한다. 요약하면:

- [ ] 이름 · 패키지 구조가 규칙대로인가. 테스트는 `<구현체명>Test` 인가
- [ ] 생성자가 DataSource 인터페이스 하나만 받는가 (`HttpClient` 는 DataSource 가)
- [ ] Repository import 에 기술 타입 · `Response` · 구현체가 없는가 (원격은 `Dto` · `toModel` 만 더)
- [ ] 가공은 Repository 에, I/O · 파싱 · DTO 생성은 DataSource 에, 변환은 Mapper 에 있는가
- [ ] 원격이면 DTO 가 전부 nullable `@SerialName` 이고, 모델은 순수 코틀린이고, `toModel()` 에 `?:` 기본값이 있는가
- [ ] 쓰기가 읽기 → 바꾸기 → 전체 저장인가. 원격 읽기가 `body ?: return null` → `toModel()` → `catch → null` 인가
- [ ] 테스트가 Repository 는 Fake · DataSource 는 MockEngine 을 쓰고, 아무것도 상속하지 않고, JUnit 5 만 쓰고, `suspend` 는 `runTest` 인가
- [ ] `./gradlew test` 가 통과하는가
