# DataSource 작성 원칙

> 기준 코드: `src/main/java/com/survivalcoding/day15_datasource/`
> (`ItemDataSource.kt`, `FileItemDataSource.kt`, `model/Item.kt`, `Main.kt`)
> 새로운 DataSource를 추가할 때 이 문서의 원칙을 따른다.

## 현재 구조

```
day15_datasource/
├── Main.kt                      # 호출 측: 인터페이스 타입으로 구현체를 선언
├── data_source/
│   ├── ItemDataSource.kt        # 인터페이스
│   └── FileItemDataSource.kt    # 구현체 (파일 기술 사용)
└── model/
    └── Item.kt                  # 외부에 노출되는 데이터 모델
```

---

## 1. 네이밍 규칙

| 구분 | 규칙 | 현재 예시 |
|------|------|-----------|
| 인터페이스 | `{도메인}DataSource` | `ItemDataSource` |
| 구현체 | `{기술명}{도메인}DataSource` | `FileItemDataSource` (기술명: `File`) |
| 파일명 | 클래스명과 동일 | `ItemDataSource.kt`, `FileItemDataSource.kt` |
| 메서드명 | `load{도메인}s()`, `save{도메인}s()` | `loadItems()`, `saveItems()` |
| 패키지 | 인터페이스는 `data_source`, 모델은 `model` | `data_source/`, `model/` |

- 인터페이스 이름에는 기술명을 넣지 않는다. 기술명은 구현체 이름의 접두사로만 쓴다.

---

## 2. 캡슐화 원칙

- **인터페이스에 기술 세부사항을 노출하지 않는다.** 파일, JSON, DB, 네트워크, 직렬화 관련 타입과 import를 인터페이스 파일에 두지 않는다.
  - `ItemDataSource.kt`의 import는 `model.Item` 하나뿐이다.
- **기술 세부사항은 구현체 내부에 `private`으로 둔다.**
  - 파일 경로: `private val file = File("items.json")`
  - 직렬화 호출: `Json.decodeFromString`, `Json.encodeToString` 은 `FileItemDataSource` 안에만 있다.
- **호출 측은 구현체가 아니라 인터페이스 타입으로 선언한다.**
  - `val dataSource: ItemDataSource = FileItemDataSource()` (`Main.kt`)

---

## 3. 반환 타입 원칙

- 외부로 전달하는 값은 **모델 객체(`Item`) 또는 그 컬렉션(`List<Item>`)만** 사용한다.
  - `loadItems(): List<Item>`
  - `saveItems(items: List<Item>)`
- 인터페이스 시그니처에 `File`, JSON 문자열, 직렬화 객체 같은 기술 타입을 쓰지 않는다.
  - 저장 시에도 JSON 문자열이 아니라 `List<Item>`을 받는다. JSON 변환은 구현체가 한다.
- 데이터가 없는 경우의 현재 동작: 파일이 없거나 내용이 비어 있으면 `emptyList()`를 반환한다 (`FileItemDataSource.loadItems`).
- 모델 `Item`은 `@Serializable`이 붙은 `data class`다 (`model/Item.kt`).

---

## 4. 새 DataSource 템플릿

아래 `<...>` 부분을 실제 도메인과 기술명으로 바꿔 쓴다.
예: 도메인 `Item`, 기술 `File` → `FileItemDataSource`.

### 4-1. 인터페이스 (`data_source/<Domain>DataSource.kt`)

```kotlin
package com.survivalcoding.<프로젝트패키지>.data_source

import com.survivalcoding.<프로젝트패키지>.model.<Domain>

interface <Domain>DataSource {
    fun load<Domain>s(): List<<Domain>>
    fun save<Domain>s(items: List<<Domain>>)
}
```

### 4-2. 구현체 (`data_source/<Tech><Domain>DataSource.kt`)

```kotlin
package com.survivalcoding.<프로젝트패키지>.data_source

import com.survivalcoding.<프로젝트패키지>.model.<Domain>

class <Tech><Domain>DataSource: <Domain>DataSource {
    // 기술 세부사항은 private으로 숨긴다
    private val <resource> = <기술 리소스 생성>

    override fun load<Domain>s(): List<<Domain>> {
        // 기술 데이터를 읽고 모델 리스트로 변환해 반환한다
        TODO()
    }

    override fun save<Domain>s(items: List<<Domain>>) {
        // 모델 리스트를 기술 데이터로 변환해 저장한다
        TODO()
    }
}
```

### 4-3. 호출 측 (`Main.kt`)

```kotlin
val dataSource: <Domain>DataSource = <Tech><Domain>DataSource()
```

---

## 5. 새 DataSource 작성 체크리스트

- [ ] 인터페이스명은 `{도메인}DataSource`, 구현체명은 `{기술명}{도메인}DataSource`인가?
- [ ] 인터페이스 파일에 기술 패키지 import가 없는가?
- [ ] 파일 경로, 연결 객체, 직렬화 객체가 구현체의 `private` 멤버인가?
- [ ] 메서드의 인자와 반환값이 모델 또는 `List<모델>`인가?
- [ ] 호출 측이 인터페이스 타입으로 선언했는가?
