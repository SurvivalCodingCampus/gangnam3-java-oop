# 테스트 작성 원칙

`day16_repository` 의 `UserRepositoryTest` 와 [repository.md](repository.md) 5-5 의 `ItemRepositoryTest` 템플릿을 기준으로 정리한,
새로운 테스트 클래스를 만들 때 지켜야 할 필수 원칙이다.
Fake DataSource 작성 규칙과 테스트 본문 작성 규칙은 [repository.md](repository.md) 4장을 따른다. 이 문서는 테스트 클래스의 이름과 위치만 다룬다.

---

## 1. 네이밍 규칙

### 테스트 클래스: `<인터페이스명>Test`

- 검증 대상 **인터페이스** 이름 뒤에 `Test` 를 붙인다. 구현체 이름 (`...Impl`) 은 쓰지 않는다.
- 테스트가 검증하는 것은 "Repository 의 약속" 이지 구현체의 내부가 아니기 때문이다.

| 검증 대상 인터페이스 | 테스트 클래스        | 잘못된 예                |
|----------------------|----------------------|--------------------------|
| `ItemRepository`     | `ItemRepositoryTest` | `ItemRepositoryImplTest` |
| `UserRepository`     | `UserRepositoryTest` | `UserRepositoryImplTest` |

### 테스트 더블: `Fake<모델명>DataSource`

- 이름 규칙은 [datasource.md](datasource.md) 의 네이밍 규칙을 따른다. 테스트 클래스 파일 안에 넣지 않고 자기 파일에 둔다.

---

## 2. 위치 규칙

- 테스트 클래스는 `src/test` 에, 검증 대상과 **같은 패키지 이름** 으로 둔다.
  `com.survivalcoding.day15.repository.ItemRepositoryImpl` 의 테스트는 `src/test/kotlin/com/survivalcoding/day15/repository/ItemRepositoryTest.kt` 다.
- 패키지가 다르면 `internal` 접근이 안 되고, 파일 위치만 봐서는 대상 코드를 찾기 어렵다. 전체 패키지 구조는 [repository.md](repository.md) 1-3 을 따른다.
