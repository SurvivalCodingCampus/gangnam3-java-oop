---
name: repository
description: 이 프로젝트의 Repository/DataSource 계층 작성 규칙 (Ktor 네트워크 + MockEngine 테스트). 새 도메인의 Repository·DataSource·DTO·Mapper·테스트를 추가하거나 기존 것을 수정·리뷰할 때 사용.
---

# Repository 작성 규칙

## 네이밍
- Repository: `<Model>Repository` / `<Model>RepositoryImpl` (`I` 접두사 금지)
- DataSource: `<Model>DataSource` / `Remote<Model>DataSource`
- DTO: `<Model>Dto`, Mapper: `<Model>Mapper.kt` (확장 함수 `toModel()`, `toDto()`)
- 테스트: `<대상클래스명>Test`

## 패키지 (`com.survivalcoding.dayXX`)
```
src/main/kotlin/com/survivalcoding/dayXX/
├── model/           # 도메인 모델 (data class, non-null)
├── dto/             # @Serializable, 모든 필드 nullable, @SerialName
├── mapper/          # Dto.toModel() — null을 기본값으로 치환
├── datasource/      # inter/, impl/
└── repository/      # inter/, impl/
src/test/kotlin/dayXX/
└── <Model>RepositoryTest.kt
```
공통 타입은 새로 만들지 말고 기존 것을 import해서 재사용:
- `com.survivalcoding.day18.MyResponse`, `com.survivalcoding.day18.StatusCode`, `com.survivalcoding.day18.HttpClientFactory`
- `com.survivalcoding.day20.core.Result` — `kotlin.Result`와 이름이 겹치므로 반드시 명시적 import
- `com.survivalcoding.day20.core.NetworkError`

## 계층 책임
- **DataSource**: `HttpClient`로 요청하고 `MyResponse<Dto>`(statusCode, headers, body)를 반환. 성공(2xx)일 때만 body 역직렬화. 에러 해석·모델 변환은 하지 않음.
- **Repository**: 상태코드·예외를 `NetworkError`로 변환하고 `Dto.toModel()`로 매핑해 `Result<Model, NetworkError>` 반환. 예외를 밖으로 던지지 않음.
- Repository에 `HttpClient`, URL, `body<T>()` 등 HTTP 코드 금지.
- 모든 I/O 함수는 `suspend`.

## 의존성 주입
- `Remote<Model>DataSource(httpClient: HttpClient = HttpClientFactory.create(), baseUrl: String = "...")` — 테스트에서 클라이언트 교체 가능하게.
- `<Model>RepositoryImpl(dataSource: <Model>DataSource)` — 생성자 주입만, 내부 생성 금지.

## 에러 매핑 (Repository)
- 5xx → `SERVER_ERR`, 4xx → `CLIENT_ERR`, body null → `NULL_DATA_ERR`
- `SocketException` → `NO_INTERNET_ERR`, `HttpRequestTimeoutException`·`TimeoutCancellationException` → `TIMEOUT_ERR`, 그 외 → `UNKNOWN_ERR`
- catch 순서: 구체적인 예외 먼저, `Exception` 마지막.

## 테스트 (MockEngine)
- Ktor `MockEngine`으로 HTTP 응답을 흉내 내고, 실제 `Remote~DataSource` → `RepositoryImpl` 체인을 그대로 테스트. DataSource·Repository를 대체하는 가짜 구현체는 만들지 않음.
- 테스트 안에서 `HttpClient(engine) { }`를 만들고 실제 `HttpClientFactory`와 같은 플러그인(ContentNegotiation, HttpTimeout)을 설치.
- 응답에 `headersOf(HttpHeaders.ContentType, "application/json")` 필수.
- 테스트 함수는 `runTest { }`. 타임아웃 테스트는 `MockEngine.create { dispatcher = StandardTestDispatcher(testScheduler) }`로 가상 시간 사용.
- 필수 케이스: 성공(200), 4xx, 5xx, 네트워크 예외, 타임아웃, 그 외 예외.

전체 코드는 [templates.md](templates.md) 참고.

## 완료 전 체크리스트
- [ ] DTO 필드가 nullable이고 Mapper가 null을 처리하는가
- [ ] DataSource가 `MyResponse<Dto>`만 반환하고 에러 해석을 하지 않는가
- [ ] Repository가 `Result`를 반환하고 예외를 던지지 않는가
- [ ] DataSource는 HttpClient를, Repository는 DataSource 인터페이스를 생성자로 받는가
- [ ] MockEngine으로 성공·4xx·5xx·예외·타임아웃 케이스를 모두 테스트했는가
