---
name: repository-pattern
description: Rules and templates for this project's Kotlin DataSource / Repository / Fake / Test layering (day15, day16 style). Use this whenever the user asks to create, review, fix, rename or refactor a Repository, RepositoryImpl, DataSource, Fake DataSource, or a Repository test in this repo, or mentions 리포지토리, 데이터소스, Fake, Repository 테스트, 패키지 구조, DI, 책임 분리. Also use it when checking existing day15/day16 code against the project conventions, even if the user only says "원칙대로 고쳐줘" or "구조 확인해줘".
---

# Repository 패턴 작성 가이드

이 저장소의 `DataSource → Repository → Test` 계층을 만들거나 고칠 때 따르는 규칙이다.
세부 규칙과 전체 코드 템플릿은 `references/` 아래 세 문서에 있다. 이 파일은 "언제 어떤 문서를 읽고, 무엇을 반드시 지키는지" 만 정리한다.

## 참조 문서

| 문서                                               | 언제 읽는가                                                                                               |
|----------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| [references/datasource.md](references/datasource.md) | DataSource 인터페이스 · 구현체를 만들거나 고칠 때. 네이밍, 캡슐화, 반환 타입, Remote/DB 템플릿 (4장).        |
| [references/repository.md](references/repository.md) | Repository 인터페이스 · 구현체 · Fake · 테스트 · Main 조립을 만들거나 고칠 때. 전체 세트 템플릿은 5장.      |
| [references/test.md](references/test.md)             | 테스트 클래스의 이름과 위치를 정할 때.                                                                    |

작업 범위에 맞는 문서만 읽는다. 예를 들어 Repository 의 정렬 로직만 고친다면 repository.md 3장만 보면 된다.
세 문서는 서로 상대 링크로 연결되어 있으므로, 한 문서가 다른 문서의 장을 가리키면 그 장을 이어서 읽는다.

## 새 Repository 를 만드는 순서

1. `model/` 과 `datasource/` (인터페이스 + 기술 구현체) 를 datasource.md 4장 템플릿대로 먼저 만든다.
2. `repository/<모델명>Repository.kt` 인터페이스. import 는 모델만.
3. `repository/<모델명>RepositoryImpl.kt` 구현체. 생성자는 `<모델명>DataSource` 인터페이스 하나.
4. `src/test/.../datasource/Fake<모델명>DataSource.kt`. In-Memory `List` 하나를 들고 `load` / `save` 만 구현한다.
5. `src/test/.../repository/<모델명>RepositoryTest.kt`. 공개 메서드마다 테스트 하나.
6. `Main.kt` 에서 인터페이스 타입으로 조립한다.
7. `./gradlew test` 를 실제로 돌려 통과를 확인한 뒤에 끝났다고 보고한다.

repository.md 5장에 `Item` 기준 전체 세트가 있다. 모델명만 바꾸면 그대로 쓸 수 있으므로 처음부터 새로 쓰지 말고 그 템플릿에서 출발한다.

## 반드시 지키는 것

각 규칙의 이유는 참조 문서에 있다. 여기서는 위반이 잦은 것만 모았다.

**패키지와 이름**
- `datasource/` 와 `repository/` 는 **형제 패키지** 다. 하나를 다른 하나 아래에 넣지 않는다. 패키지 이름은 `datasource` (언더스코어 없음).
- 이름은 `<모델명>DataSource`, `<기술명><모델명>DataSource`, `<모델명>Repository`, `<모델명>RepositoryImpl`, `Fake<모델명>DataSource`, `<인터페이스명>Test`.
  Repository 에는 기술명을 붙이지 않는다. 테스트는 `...ImplTest` 가 아니다.
- 테스트 패키지는 메인 패키지와 똑같이 맞춘다. Fake 는 `src/test` 에, 자기 파일로 둔다.

**의존성**
- Repository 생성자는 `private val dataSource: <모델명>DataSource` 한 줄이다. 기본값을 주지 않고, 구현체 타입이나 `File` · `Json` 같은 기술 타입을 받지 않는다.
- Repository 파일의 import 에 `java.io`, `kotlinx.serialization`, `java.net`, `java.sql`, 구현체 이름이 보이면 책임이 잘못 올라온 것이다. 그 코드는 DataSource 로 내린다.
- 구현체를 고르는 곳은 `main` 한 줄뿐이다. 변수는 인터페이스 타입으로 선언한다.

**책임 분리**
- DataSource 는 읽고 쓴다 (`load` / `save`, 파싱, 타입 변환). Repository 는 가공한다 (정렬, 필터, 검색, 단건 찾기, 상위 N, 비즈니스 규칙).
- 읽기 메서드는 매번 `dataSource.load...()` 로 시작한다. Repository 는 상태를 들고 있지 않는다. 단건 조회는 `?` 로 `null` 을 돌려준다. 반환은 `List`, `MutableList` 가 아니다.
- 쓰기 메서드는 **읽기 → 바꾸기 → 전체 저장** 세 단계다. DataSource 에 `appendItem`, `deleteById` 같은 메서드를 추가하지 않는다.
- `id` 파라미터 타입은 모델의 `id` 타입과 같다. 인터페이스의 모든 메서드는 구현체에 있어야 한다.

**테스트**
- Mock 라이브러리가 아니라 Fake 를 쓴다. 의존성은 JUnit Jupiter 뿐이다.
- 테스트 클래스는 아무것도 상속하지 않는다. `org.junit.jupiter.api` 의 `BeforeEach` / `Test` / `Assertions` 만 쓴다 (JUnit 4 의 `org.junit.Before` 는 없다).
- 테스트 데이터는 모델에 실제로 있는 필드로만 만든다. 테스트 이름은 한글 백틱으로 "`메서드 는 무엇 한다`" 꼴.

## 기존 코드를 점검할 때

점검 요청이면 고치기 전에 먼저 아래 순서로 본다. 발견한 문제는 "파일 · 위반 규칙 · 참조 문서의 장" 세 가지로 보고한다.

1. 디렉터리 구조와 이름 (위의 "패키지와 이름")
2. Repository 파일의 import 목록 (repository.md 2-2 의 표)
3. 생성자 시그니처와 `main` 의 조립 방식
4. 인터페이스 ↔ 구현체 메서드 시그니처 일치 여부
5. DataSource 에 가공 로직이, Repository 에 I/O 가 섞여 있지 않은지 (repository.md 3-1 책임 표)
6. 테스트가 컴파일되고 통과하는지. 컴파일이 안 되면 그 원인부터 적는다.

## 끝내기 전 체크리스트

repository.md 6장의 체크리스트를 그대로 적용한다. 요약하면:

- [ ] 이름 · 패키지 구조가 규칙대로인가
- [ ] 생성자가 DataSource 인터페이스 하나만 받는가
- [ ] Repository import 에 기술 타입 · 구현체가 없는가
- [ ] 가공은 Repository 에, I/O 는 DataSource 에 있는가
- [ ] 쓰기가 읽기 → 바꾸기 → 전체 저장인가
- [ ] 테스트가 Fake 를 쓰고, 아무것도 상속하지 않고, JUnit 5 만 쓰는가
- [ ] `./gradlew test` 가 통과하는가
