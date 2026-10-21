package com.survivalcoding.`15day_dataSource`

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

// 1. 할 일 정보를 담는 데이터 클래스 (@Serializable 이름표를 붙여 JSON 변환이 가능하게 함)
@Serializable
data class Todo(
    val userId: Long,     // 작성자 ID
    val id: Long,         // 할 일 고유 번호
    val title: String,    // 할 일 내용
    val completed: Boolean // 완료 여부 (true/false)
)

// 2. 외부와 소통하기 위한 통로 인터페이스 (기술적 내용 숨김)
interface TodoDataSource {
    fun loadTodos(): List<Todo>           // 할 일 목록 불러오기
    fun saveTodos(todos: List<Todo>)     // 할 일 목록 저장하기
}

// 3. 파일(File)을 사용해서 Todo 데이터를 관리하는 실제 구현체 클래스
class FileTodoDataSource : TodoDataSource {

    // 데이터를 저장할 파일 객체 생성 (루트 폴더에 todos.json 파일 생성)
    private val file = File("todos.json")

    // 파일에서 JSON 데이터를 읽어와 코틀린 객체 리스트로 변환하는 함수
    override fun loadTodos(): List<Todo> {
        // 1단계: 파일이 아예 존재하지 않는다면 빈 리스트를 안전하게 리턴
        if (!file.exists()) {
            return emptyList()
        }

        // 2단계: 파일의 모든 내용을 문자열로 읽어옴
        val jsonText = file.readText()

        // 3단계: 읽어온 텍스트가 비어있다면 빈 리스트 리턴
        if (jsonText.isBlank()) {
            return emptyList()
        }

        // 4단계: JSON 문자열을 List<Todo> 객체로 역직렬화(복원)해서 리턴
        return Json.decodeFromString<List<Todo>>(jsonText)
    }

    // 코틀린 객체 리스트를 JSON 문자열로 바꿔 파일에 저장하는 함수
    override fun saveTodos(todos: List<Todo>) {
        // 1단계: Todo 리스트를 JSON 문자열로 직렬화(포장)
        val jsonText = Json.encodeToString(todos)

        // 2단계: 파일에 문자열을 덮어쓰기 방식으로 기록
        file.writeText(jsonText)
    }
}

// 4. 실행 테스트를 위한 메인 함수
fun main() {
    // 다형성을 활용해 인터페이스 타입으로 구현체 인스턴스 생성
    val dataSource: TodoDataSource = FileTodoDataSource()

    // 저장할 샘플 Todo 데이터 준비
    val sampleTodos = listOf(
        Todo(userId = 1L, id = 1L, title = "파일 입출력 과제 제출하기", completed = false),
        Todo(userId = 1L, id = 2L, title = "JSON 직렬화 개념 복습", completed = true),
        Todo(userId = 1L, id = 3L, title = "DataSource 인터페이스 작성", completed = true)
    )

    // 파일에 데이터 저장하기
    println("=== 데이터 저장 시도 ===")
    dataSource.saveTodos(sampleTodos)
    println("todos.json 파일 저장이 완료되었습니다.\n")

    // 파일에서 데이터 불러오기
    println("=== 저장된 데이터 불러오기 ===")
    val loadedTodos = dataSource.loadTodos()

    // 불러온 데이터를 보기 좋게 출력
    loadedTodos.forEach { todo ->
        val status = if (todo.completed) "[완료]" else "[미완료]"
        println("$status ID: ${todo.id} | ${todo.title} (작성자: User ${todo.userId})")
    }
}