# DataSource 작성 원칙

## 1. 현재 코드에서 확인한 구조

`docs/TIL/2026-10-21.md`의 `ItemDataSource`, `FileItemDataSource`는 패턴을 설명하기 위한 이름이다. 현재 저장소의 실제 코드는 다음과 같다.

| 역할 | 실제 코드 | 책임 |
| --- | --- | --- |
| 인터페이스 | `TodoDataSource` | `loadTodos(): List<Todo>`, `saveTodos(todos: List<Todo>)` 계약 정의 |
| 파일 구현체 | `FileTodoDataSource` | 파일 읽기·쓰기와 JSON 직렬화·역직렬화 |
| 인터페이스 | `StockDataSource` | `loadStockListings(): List<StockListing>` 계약 정의 |
| CSV 구현체 | `CsvStockDataSource` | CSV 파일 읽기, 헤더 제외, 필드 파싱 및 모델 생성 |

`FileTodoDataSource`는 파일 경로를 `private`으로 숨기고, 읽은 JSON을 `List<Todo>`로 변환해서 반환한다. 저장할 때도 호출자는 `List<Todo>`만 전달하며 JSON 변환이나 파일 쓰기를 직접 수행하지 않는다. `CsvStockDataSource` 역시 문자열 행 대신 `StockListing` 목록을 반환한다. 새로운 구현체도 이 경계를 유지한다.

## 2. 네이밍 규칙

- **인터페이스는 `<대상 모델/데이터명>DataSource`로 명명한다.** 예: `ItemDataSource`, `TodoDataSource`, `StockDataSource`.
- **구현체는 `<기술명/출처 접두사><인터페이스명>`으로 명명한다.** 예: `FileItemDataSource`, `RemoteItemDataSource`, `DbItemDataSource`. 기술을 구체적으로 구분해야 한다면 `CsvStockDataSource`, `SqliteItemDataSource`처럼 작성한다.
- 인터페이스 이름에는 `File`, `Json`, `Http`, `Sqlite` 같은 기술명을 붙이지 않는다. 기술 선택은 구현체 이름에서 드러낸다.
- 메서드는 데이터에 대한 작업을 표현한다. `loadItems()`, `saveItems(items)`처럼 작성하고 `readJsonFile()`, `executeSql()` 같은 기술 중심 이름은 인터페이스에서 피한다.

현재 패키지 구조에 맞추면 인터페이스는 `datasource`, 구현체는 `datasourceImpl`, 반환 모델은 `model`에 둔다.

## 3. 캡슐화 원칙

인터페이스는 **어떤 데이터를 주고받는지**만 정의하고, **어디에서 어떤 방식으로 처리하는지**는 구현체 내부에 숨긴다.

| 기술 세부사항 | 숨겨야 할 내용 |
| --- | --- |
| 파일 | 경로, `File`/`Path`, Reader/Writer, 문자 인코딩 |
| DB | 연결, SQL, 테이블, 커서, `ResultSet`, DB 엔티티 |
| 네트워크 | URL, 인증, HTTP 클라이언트, 요청·응답 객체, 상태 코드 처리 |
| 직렬화 | JSON/CSV 원문, 파서, 직렬화 설정, 전송용 DTO |

구현체의 필드와 보조 함수는 가능한 한 `private`으로 둔다. 경로·클라이언트·DB 연결 등을 구현체 생성자로 주입하는 것은 가능하지만, 공통 인터페이스의 메서드 인자나 반환 타입에 노출하지 않는다. 생성자 주입은 구성과 테스트를 쉽게 하고, 주입받은 의존성은 비공개 필드로 보관한다.

호출자는 인터페이스 타입에 의존한다. 실제 구현체 선택과 연결은 애플리케이션 구성 지점에서 수행한다. 구현체가 교체되어도 호출자의 데이터 처리 코드를 바꿀 필요가 없어야 한다.

## 4. 반환 타입 원칙

- 조회 결과는 `Todo`, `StockListing` 같은 **정제된 모델 객체 또는 그 컬렉션**으로 반환한다. 단건 조회의 부재는 계약에 따라 `Todo?` 등으로 표현한다.
- JSON 문자열, CSV 행, HTTP 응답, DB 커서, DTO나 DB 엔티티를 그대로 반환하지 않는다. 구현체 안에서 파싱·검증·모델 변환을 끝낸다.
- 여러 건은 `List<T>` 같은 읽기 전용 컬렉션 타입으로 공개한다. Kotlin의 `List`가 깊은 불변성을 보장하는 것은 아니므로 내부 가변 컬렉션을 공유한다면 필요에 따라 복사해서 반환한다.
- 저장 메서드는 모델이나 모델 컬렉션을 입력받는다. 반환할 데이터가 없다면 현재 `saveTodos()`처럼 `Unit`을 사용한다.
- 빈 데이터와 처리 실패를 구분한다. 현재 파일 구현은 공백 파일에 빈 목록을 반환하지만 파일 누락·잘못된 JSON은 예외가 발생한다. 새 구현체도 실패를 무조건 빈 목록으로 바꾸지 말고, 빈 결과·오류·저장 동작의 의미를 공통 계약에 맞춘다.

현재 `Todo`는 `@Serializable` 모델을 그대로 사용한다. 지금처럼 모델을 직접 직렬화할 수도 있지만, API 응답이나 DB 구조가 모델과 달라지면 구현체 내부의 DTO/행 객체를 별도로 두고 모델로 변환한다. 인터페이스에는 직렬화 라이브러리 타입을 추가하지 않는다.

## 5. 새로운 DataSource 템플릿

아래는 기존 `Todo`와 `TodoDataSource` 계약을 사용하는 구조 예시다. `Item`에 적용하려면 모델과 메서드 이름을 `Item`, `loadItems`, `saveItems`로 바꾼다. API·DB 접근 어댑터는 실제 사용할 라이브러리로 별도 구현해야 하며, 아래 코드는 네트워크나 DB를 직접 실행하는 완성 구현은 아니다.

### 공통 인터페이스

```kotlin
// datasource/TodoDataSource.kt — 기존 계약 유지
interface TodoDataSource {
    fun loadTodos(): List<Todo>
    fun saveTodos(todos: List<Todo>)
}
```

### Remote API 구현체

```kotlin
// datasourceImpl/RemoteTodoDataSource.kt
// Todo와 TodoDataSource는 기존 패키지에서 import한다.

// 구현 계층 전용 전송 객체: 공통 DataSource 계약에 노출하지 않는다.
internal data class TodoDto(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,
) {
    fun toModel(): Todo = Todo(userId, id, title, completed)
}

// 실제 어댑터에서 HTTP 요청, 인증, JSON 처리와 응답 검증을 담당한다.
internal interface TodoApi {
    fun fetchTodos(): List<TodoDto>
    fun replaceTodos(todos: List<TodoDto>)
}

internal class RemoteTodoDataSource(
    private val api: TodoApi,
) : TodoDataSource {
    override fun loadTodos(): List<Todo> =
        api.fetchTodos().map { it.toModel() }

    override fun saveTodos(todos: List<Todo>) {
        api.replaceTodos(todos.map {
            TodoDto(it.userId, it.id, it.title, it.completed)
        })
    }
}
```

`TodoApi`는 구현 계층의 보조 계약이다. 상위 호출자는 `TodoDataSource`를 사용하므로 HTTP 응답이나 `TodoDto`를 알 필요가 없다. 실제 API가 지원하는 저장 방식이 기존 계약과 맞는지 확인한 후 연결한다.

### DB 구현체

```kotlin
// datasourceImpl/DbTodoDataSource.kt
// Todo와 TodoDataSource는 기존 패키지에서 import한다.

internal data class TodoRow(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,
) {
    fun toModel(): Todo = Todo(userId, id, title, completed)
}

// 실제 어댑터에서 SQL, 연결·커서 관리 및 트랜잭션을 담당한다.
internal interface TodoTable {
    fun selectAll(): List<TodoRow>
    fun replaceAll(rows: List<TodoRow>)
}

internal class DbTodoDataSource(
    private val table: TodoTable,
) : TodoDataSource {
    override fun loadTodos(): List<Todo> =
        table.selectAll().map { it.toModel() }

    override fun saveTodos(todos: List<Todo>) {
        table.replaceAll(todos.map {
            TodoRow(it.userId, it.id, it.title, it.completed)
        })
    }
}
```

위 템플릿의 `replaceTodos`와 `replaceAll`은 전체 목록 저장을 가정한다. 기존 파일 구현처럼 목록 전체를 대체하는 의미를 유지하려면, DB 어댑터는 필요 시 트랜잭션으로 전체 대체를 보장해야 한다. 추가·수정만 하는 동작으로 몰래 바꾸지 않는다.

두 템플릿은 현재의 동기식 계약을 따른다. 비동기가 필요하면 공통 인터페이스와 호출부, 모든 구현체의 계약을 함께 검토한다. 특정 구현체 때문에 공통 반환 타입을 HTTP 응답이나 DB 전용 타입으로 바꾸지 않는다.

### 호출부

```kotlin
fun loadCompletedTodos(dataSource: TodoDataSource): List<Todo> =
    dataSource.loadTodos().filter { it.completed }

// 구성 지점에서 구현체를 선택한다.
val dataSource: TodoDataSource = FileTodoDataSource()
val completedTodos = loadCompletedTodos(dataSource)
// RemoteTodoDataSource(api) 또는 DbTodoDataSource(table)로 교체해도
// loadCompletedTodos 함수는 그대로 사용한다.
```

## 6. 작성 후 확인

- 인터페이스는 데이터 이름, 구현체는 기술명 접두사로 구분했는가?
- 파일·DB·네트워크·직렬화 세부사항이 공통 계약에 드러나지 않는가?
- 호출자는 정제된 모델과 컬렉션만 주고받는가?
- 새 구현체도 빈 결과, 오류, 저장 의미에 대한 같은 계약을 지키는가?
- 구현체를 교체해도 인터페이스에 의존하는 호출부를 유지할 수 있는가?
