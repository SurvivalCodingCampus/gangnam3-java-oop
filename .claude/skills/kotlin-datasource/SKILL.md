---
name: kotlin-datasource
description: Kotlin에서 DataSource(데이터 소스) 인터페이스와 구현체를 만들 때 쓰는 작성 원칙과 템플릿. 모델 클래스, `<모델명>DataSource` 인터페이스, `File`/`Csv`/`Remote`/`Db` 같은 기술명이 붙은 구현체(예 FileItemDataSource), 테스트용 Fake DataSource를 만들거나 리뷰할 때 반드시 사용한다. 사용자가 "DataSource 만들어 줘", "데이터 소스 구현체 작성", "JSON/CSV 파일에서 읽어오는 클래스", "load/save 메서드", "Repository가 쓸 데이터 소스"처럼 말하거나 JSON 샘플을 주며 모델과 데이터 소스를 요청할 때도, "DataSource"라는 단어가 없더라도 이 스킬을 사용한다.
---

# Kotlin DataSource 작성 스킬

DataSource는 "데이터를 어디서, 어떤 형식으로 가져오는지"를 숨기고 **정제된 모델만** 바깥에 넘기는 계층이다.
이 원칙을 지키면 저장 기술(JSON 파일 → CSV → DB → API)이 바뀌어도 사용하는 쪽 코드는 첫 줄(구현체 생성)만 바뀐다.
아래 규칙은 모두 그 목적에서 나온 것이므로, 애매한 경우에는 "기술이 바뀌면 이 코드도 바뀌어야 하나?"를 기준으로 판단한다.

## 작업 순서

1. **기존 코드 확인**: 대상 패키지(예 `com.survivalcoding.day16_repository`)와 같은 프로젝트의 기존 DataSource(`data_source/`, `model/`)를 먼저 읽고 패키지명, 코드 스타일(콜론 앞 공백, trailing comma 등)을 맞춘다. 패키지명은 문서 예시가 아니라 **실제 폴더 경로**에서 정한다.
2. `model/<모델명>.kt` 에 `@Serializable data class` 모델을 만든다.
3. `data_source/<모델명>DataSource.kt` 에 인터페이스를 만든다. import 는 모델 하나뿐이어야 한다.
4. `data_source/<기술명><모델명>DataSource.kt` 에 구현체를 만든다.
5. 사용처(`Main.kt`, Repository 등)에서는 **인터페이스 타입으로** 선언한다.
6. 가능하면 `./gradlew compileKotlin` (테스트를 만들었다면 `./gradlew test`)으로 컴파일을 확인한다.

## 1. 네이밍

| 대상 | 규칙 | 예시 |
|---|---|---|
| 인터페이스 | `<모델명>DataSource` (접두사 `I`, 기술명 금지) | `ItemDataSource`, `CommentDataSource` |
| 구현체 | `<기술명><인터페이스명>` | `FileItemDataSource` |
| 메서드 | `load<모델명>s()`, `save<모델명>s(...)` | `loadComments()`, `saveComments(comments)` |
| 파일 | 클래스명과 동일 | `FileItemDataSource.kt` |
| 패키지 | 인터페이스/구현체는 `data_source`, 모델은 `model` | |

기술명 접두사는 한 단어로, 보고 바로 알 수 있는 것을 쓴다.

| 기술 | 접두사 |
|---|---|
| 로컬 파일(JSON) | `File` |
| 로컬 파일(CSV) | `Csv` |
| 원격 API | `Remote` 또는 `Api` |
| 데이터베이스 | `Db` 또는 제품명(`Sqlite`) |
| 테스트용 메모리 구현 | `Fake` |

## 2. 캡슐화

인터페이스는 **무엇을 주고받는지**만 말하고, **어떻게**는 전부 구현체 안에 숨긴다.

인터페이스(시그니처와 import)에 두면 안 되는 것:

- 파일: `File`, `Path`, 파일명 파라미터, `InputStream`
- DB: `Connection`, `ResultSet`, SQL 문자열
- 네트워크: `HttpClient`, `HttpResponse`, URL, 헤더
- 직렬화: `Json`, `JsonElement`, 원본 문자열, CSV 한 줄
- 외부 라이브러리 타입: `kotlinx.serialization`, JDBC, Ktor 등

구현체 안에서 지킬 것:

- 파일 경로, URL, 테이블명은 `private val` 로 둔다. 바꿀 필요가 있으면 **생성자 파라미터(기본값 포함)** 로 받고, 메서드 파라미터로는 받지 않는다.
- 외부 라이브러리 import 는 구현체 파일에만 둔다.
- 사용하는 쪽은 `val dataSource: ItemDataSource = FileItemDataSource()` 처럼 인터페이스 타입으로 선언한다. `val dataSource = FileItemDataSource()` 는 구현체에 묶인다.

## 3. 반환 타입

밖으로 나가는 것은 **정제된 모델 객체 또는 그 컬렉션**뿐이다.

| 상황 | 반환 타입 |
|---|---|
| 여러 건 조회 | `List<Model>` |
| 단건 조회(없을 수 있음) | `Model?` |
| 저장/삭제 | `Unit` |

금지: `String`(JSON/CSV 원문), `List<String>`, `List<Array<String>>`, `JsonElement`, `Map<String, Any>`, `ResultSet`, `HttpResponse`, `File`, `MutableList<Model>`(밖에서 수정하면 내부 상태가 깨진다).

반환 전 정제 규칙 — 구현체가 **파싱 → 모델 변환 → 정제**를 끝내고 돌려준다.

1. 파일이 없거나 비어 있으면 예외 대신 `emptyList()` 를 반환한다.
2. 헤더 줄, 빈 줄처럼 데이터가 아닌 것은 구현체 안에서 걸러낸다.
3. `trim()`, `toInt()`/`toLong()` 같은 변환은 모델에 넣기 전에 끝낸다.
4. 내부에서 `mutableListOf` 로 모았더라도 반환 타입은 `List` 로 선언한다.

## 4. 템플릿

`<...>` 를 실제 값으로 바꿔 쓴다. 모델 필드는 사용자가 준 JSON 샘플이 있으면 **키 이름과 타입을 그대로** 따른다(정수는 `Int`, 큰 id 가 예상되면 `Long`).

### 모델

```kotlin
package com.survivalcoding.<패키지>.model

import kotlinx.serialization.Serializable

@Serializable
data class <Model>(
    val id: Int,
    val name: String,
)
```

### 인터페이스

```kotlin
package com.survivalcoding.<패키지>.data_source

import com.survivalcoding.<패키지>.model.<Model>

interface <Model>DataSource {
    fun load<Model>s(): List<<Model>>
    fun save<Model>s(<model>s: List<<Model>>)
}
```

### JSON 파일 구현체 (`File`)

```kotlin
package com.survivalcoding.<패키지>.data_source

import com.survivalcoding.<패키지>.model.<Model>
import kotlinx.serialization.json.Json
import java.io.File

class File<Model>DataSource(
    private val file: File = File("<model>s.json"),
): <Model>DataSource {

    override fun load<Model>s(): List<<Model>> {
        if (!file.exists()) {
            return emptyList()
        }

        val jsonText = file.readText()
        if (jsonText.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<<Model>>>(jsonText)
    }

    override fun save<Model>s(<model>s: List<<Model>>) {
        val jsonText = Json.encodeToString(<model>s)
        file.writeText(jsonText)
    }
}
```

### CSV 파일 구현체 (`Csv`)

```kotlin
class Csv<Model>DataSource(
    private val file: File = File("<model>s.csv"),
): <Model>DataSource {

    override fun load<Model>s(): List<<Model>> {
        if (!file.exists()) {
            return emptyList()
        }

        return file.readLines()
            .drop(1)                      // 헤더 제거
            .filter { it.isNotBlank() }   // 빈 줄 제거
            .map { line ->
                val columns = line.split(",").map { it.trim() }
                <Model>(
                    id = columns[0].toInt(),
                    name = columns[1],
                )
            }
    }

    override fun save<Model>s(<model>s: List<<Model>>) {
        val lines = listOf("id,name") + <model>s.map { "${it.id},${it.name}" }
        file.writeText(lines.joinToString("\n"))
    }
}
```

### 테스트용 Fake 구현체 (`src/test/.../data_source/`)

Repository 테스트에서 파일 I/O 없이 데이터를 넣고 빼기 위해 쓴다.

```kotlin
class Fake<Model>DataSource: <Model>DataSource {
    private var <model>s: List<<Model>> = emptyList()

    override fun load<Model>s(): List<<Model>> = <model>s

    override fun save<Model>s(<model>s: List<<Model>>) {
        this.<model>s = <model>s
    }
}
```

### 사용처

```kotlin
fun main() {
    val dataSource: <Model>DataSource = File<Model>DataSource()
    // val dataSource: <Model>DataSource = Csv<Model>DataSource()

    dataSource.load<Model>s().forEach(::println)
}
```

## 5. 완료 전 체크리스트

작성을 마치면 아래를 확인하고, 사용자에게 결과를 알릴 때 지킨 원칙을 짧게 요약한다.

- [ ] 인터페이스명 `<모델명>DataSource`, 구현체명 `<기술명><모델명>DataSource` 인가?
- [ ] 인터페이스 파일의 import 가 모델 하나뿐인가?
- [ ] 경로/URL/직렬화 객체가 구현체의 `private` 멤버이거나 생성자 파라미터인가?
- [ ] 메서드 인자와 반환값이 모델, `List<모델>`, `모델?`, `Unit` 중 하나인가? (`MutableList` 아님)
- [ ] 데이터가 없을 때 `emptyList()` 를 반환하는가?
- [ ] 사용처가 인터페이스 타입으로 선언했는가?
- [ ] 다른 day 패키지의 클래스를 잘못 import 하지 않았는가?
