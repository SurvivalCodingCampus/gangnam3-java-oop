# Mapper 작성 원칙

포켓몬 API 실습의 `PokemonMapper.kt` 와 그것을 검증하는 `ToModelTest.kt`, 그것을 부르는 `PokemonRepositoryImpl.kt` 를 기준으로 정리한,
새로운 Mapper 를 만들 때 지켜야 할 필수 원칙이다.
DTO 쪽 규칙은 [dto.md](dto.md), DataSource 쪽 규칙은 [datasource.md](datasource.md), Repository 쪽 규칙은 [repository.md](repository.md),
테스트 규칙은 [test.md](test.md) 를 따른다. 이 문서는 `mapper` 패키지 안의 파일만 다룬다.
코드 발췌는 원본 그대로이되, `package` · `import` 줄의 수업 날짜 패키지명만 `<dayXX>` 로 바꿔 적었다.

> **한 줄 요약**
> Mapper 는 "DTO 에서 모델이 필요한 것만 **골라 담는 확장 함수**" 다.
> `null` 은 여기서 끝낸다. 모델 밖으로 `?` 가 새어 나가지 않게 하는 것이 Mapper 의 유일한 일이다.

---

## 0. Mapper 가 하는 일 — 47개 클래스에서 3개 필드로

```
PokemonDto (최상위 20개 필드 + 중첩 46개 클래스, 전부 nullable)
    │
    │  dto.toModel()        ← Mapper. 필요한 것만 고르고, null 은 기본값으로
    ▼
Pokemon (id, name, imageUrl — 3개 필드, 전부 non-null)
```

### 0-1. 모델 필드 하나하나가 DTO 어디서 오는가

| 모델 필드 (`Pokemon`) | 타입     | DTO 에서 가져오는 곳 (`PokemonDto`)   | DTO 쪽 타입                              | `null` 이면 |
|-----------------------|----------|---------------------------------------|------------------------------------------|-------------|
| `id`                  | `Int`    | `id`                                  | `Int?`                                   | `0`         |
| `name`                | `String` | `name`                                | `String?`                                | `""`        |
| `imageUrl`            | `String` | `sprites` → `frontDefault`            | `PokemonSprites?` → `String?` (두 단계)  | `""`        |

- DTO 의 최상위 필드 20개 중 **셋만** 쓴다. `baseExperience`, `height`, `weight`, `abilities`, `moves` ... 17개는 Mapper 가 건드리지 않는다.
- `imageUrl` 은 DTO 에 그 이름으로 존재하지 않는다. `sprites.frontDefault` 를 **모델에서 부르고 싶은 이름** 으로 바꿔 담은 것이다. 이름을 바꾸는 것은 Mapper 의 일이다.

### 0-2. 원본 전체

```kotlin
// PokemonMapper.kt  (원본 전체)
package com.survivalcoding.<dayXX>.mapper

import com.survivalcoding.<dayXX>.dto.PokemonDto
import com.survivalcoding.<dayXX>.model.Pokemon

fun PokemonDto.toModel(): Pokemon {
    return Pokemon(
        id = id ?: 0,
        name = name ?: "",
        imageUrl = sprites?.frontDefault ?: ""
    )
}
```

파일 전체가 **12줄** 이다. Mapper 는 이보다 길어질 이유가 거의 없다. 길어지면 가공(Repository 의 일)이 들어온 것이다.

---

## 1. 네이밍 및 위치 규칙

### 1-1. 이름

| 역할        | 규칙                                  | 예시                                   |
|-------------|---------------------------------------|----------------------------------------|
| 파일        | `<모델명>Mapper.kt`                   | `PokemonMapper.kt`                     |
| 함수        | `fun <모델명>Dto.toModel(): <모델명>` | `fun PokemonDto.toModel(): Pokemon`    |
| 호출        | `dto.toModel()`                       | `dto.toModel()` — "dto 를 모델로"      |
| 테스트 클래스 | `<함수명 첫 글자 대문자>Test`       | `ToModelTest` (파일 `ToModelTest.kt`)  |

- 함수 이름은 항상 `toModel` 이다. `toPokemon()`, `convert()`, `map()` 처럼 모델마다 다른 이름을 쓰지 않는다. 어느 DTO 든 `.toModel()` 만 치면 되게 한다.
- **클래스가 아니라 최상위 확장 함수** 다. `object PokemonMapper { fun map(dto) }` 나 `class PokemonMapper` 를 만들지 않는다.
- 테스트 클래스 이름이 파일명(`PokemonMapper`)이 아니라 **함수명**(`toModel`)을 따른 점에 주의한다. 검증 대상이 클래스가 아니라 함수 하나이기 때문이다. → [test.md](test.md)

### 1-2. 왜 확장 함수인가

| 방법                                  | 호출 모양                       | 문제                                                                 |
|---------------------------------------|---------------------------------|----------------------------------------------------------------------|
| DTO 안에 멤버 함수                    | `dto.toModel()`                 | DTO 가 `model` 패키지를 import 하게 된다. 방향이 거꾸로 (dto.md 1-3) |
| 별도 `object` / `class`               | `PokemonMapper.map(dto)`        | 객체를 하나 더 만들어야 하고, 읽을 때 주어가 바뀐다                  |
| **최상위 확장 함수** (실습 코드)      | `dto.toModel()`                 | 없음. DTO 파일은 그대로 두고, 호출은 멤버 함수처럼 읽힌다            |

- DTO 파일은 "서버 응답을 옮겨 적은 것" 이라 손대지 않는다는 원칙([dto.md](dto.md) 한 줄 요약)과 맞물린다. 변환 코드는 DTO 밖, `mapper` 패키지에 둔다.
- 확장 함수 안에서 `id`, `name`, `sprites` 를 `this.` 없이 바로 쓸 수 있다. 수신 객체가 `PokemonDto` 이기 때문이다.

### 1-3. 패키지

```
<dayXX>
├── dto/
│   └── PokemonDto.kt          // 입력           → dto.md
├── mapper/
│   └── PokemonMapper.kt       // 이 문서
├── model/
│   └── Pokemon.kt             // 출력
├── datasource/                //                → datasource.md
└── repository/
    └── PokemonRepositoryImpl.kt   // toModel() 을 부르는 유일한 곳  → repository.md
```

- `mapper` 는 `dto`, `model`, `datasource`, `repository` 와 **형제 패키지** 다. `dto/mapper` 나 `model/mapper` 아래에 넣지 않는다. Mapper 는 둘 사이에 서 있는 것이지 어느 한쪽 소속이 아니다.
- 모델 하나에 Mapper 파일 하나. `Pokemon` 과 `Item` 을 한 파일 `Mappers.kt` 에 몰아넣지 않는다.

---

## 2. 변환 규칙

> **고르고, 이름 바꾸고, 기본값 채운다. 그 이상은 하지 않는다.**

### 2-1. 네 가지 규칙

| # | 규칙                                               | 원본 코드                                 | 어기면                                                             |
|---|----------------------------------------------------|-------------------------------------------|--------------------------------------------------------------------|
| ① | **필요한 필드만** 고른다                            | 20개 중 `id`, `name`, `sprites` 셋        | 모델이 DTO 를 복제한 꼴이 되고, 서버가 바뀔 때 모델도 같이 깨진다  |
| ② | **`null` 은 여기서 끝낸다** — `?:` 로 기본값        | `id ?: 0`, `name ?: ""`                   | 모델이 nullable 이 되고, 쓰는 쪽마다 `?.` 가 붙는다                |
| ③ | 중첩은 `?.` 로 내려가고 마지막에 `?:` 한 번         | `sprites?.frontDefault ?: ""`             | 중간 객체가 `null` 일 때 NPE                                       |
| ④ | **가공 · 판단 금지** — 계산, 정렬, 필터, 조건 분기 없음 | 세 줄 전부 "꺼내서 담기" 뿐               | Repository 의 책임이 Mapper 로 내려온다 ([repository.md](repository.md) 3장) |

### 2-2. 허용 / 금지 표

| 하는 일                                        | Mapper | 예시                                          | 아니면 어디서                 |
|------------------------------------------------|--------|-----------------------------------------------|-------------------------------|
| 필드 고르기                                    | O      | `id`, `name`, `sprites` 만                    |                               |
| 이름 바꾸기                                    | O      | `frontDefault` → `imageUrl`                   |                               |
| `null` → 기본값                                | O      | `?: 0`, `?: ""`                               |                               |
| 중첩 객체에서 값 꺼내기                        | O      | `sprites?.frontDefault`                       |                               |
| 타입 맞추기 (`String` → `Int` 등)              | △      | 실습 코드에는 없음. 필요하면 Mapper 에서      |                               |
| 값 계산 (단위 변환, 합계)                      | X      | `height * 10`                                 | Repository                    |
| 문자열 가공 (대문자, trim)                     | X      | `name.uppercase()`                            | Repository                    |
| 조건 분기 (이 값이면 저렇게)                   | X      | `if (isDefault == true) ... else ...`         | Repository                    |
| 목록 정렬 · 필터                               | X      | `abilities.filter { ... }`                    | Repository                    |
| JSON 파싱                                      | X      | `Json.decodeFromString`                       | DataSource ([datasource.md](datasource.md)) |
| 통신 · 파일 읽기                               | X      | `httpClient.get`                              | DataSource                    |

### 2-3. 기본값을 고르는 기준

실습 코드가 정한 기본값은 둘이다.

| 타입     | 기본값 | 이유                                                       |
|----------|--------|------------------------------------------------------------|
| `Int`    | `0`    | "없음" 을 뜻하는 숫자로 가장 흔한 값                       |
| `String` | `""`   | 화면에 찍어도 아무것도 안 보이고, `isEmpty()` 로 걸러진다  |

- 기본값은 **테스트로 고정** 한다. `ToModelTest` 의 두 번째 테스트가 "`null` 이면 `0` 과 `""` 가 된다" 를 검증한다 (5장). 기본값을 바꾸면 테스트가 먼저 깨져서 알려 준다.
- 기본값 방식의 한계 — `id = 0` 이 "서버가 안 줌" 인지 "진짜 0" 인지 구분이 안 된다. 실습 코드는 이 한계를 받아들이고 단순함을 택했다. 구분이 꼭 필요한 모델이면 그때 규칙을 따로 정한다. (지금은 정하지 않는다.)

### 2-4. `?.` 와 `?:` 의 역할 분담

```kotlin
imageUrl = sprites?.frontDefault ?: ""
//         ──┬───  ───┬────────  ─┬─
//           │        │           └ 결과가 null 이면 "" (마지막에 딱 한 번)
//           │        └ sprites 가 null 이 아닐 때만 frontDefault 를 읽는다. null 이면 전체가 null
//           └ PokemonSprites? — DTO 의 중첩 객체. 서버가 sprites 를 안 줄 수 있다
```

- 중첩이 더 깊어도 같다. `sprites?.other?.officialArtwork?.frontDefault ?: ""` — `?.` 를 단계마다, `?:` 는 끝에 한 번.
- `!!` 는 쓰지 않는다. DTO 는 전부 nullable 이고([dto.md](dto.md) 2-1 ④), 그 `null` 을 받아 주는 곳이 Mapper 다. `!!` 를 쓰면 DTO 가 nullable 인 의미가 없어진다.

---

## 3. import 규칙

```kotlin
import com.survivalcoding.<dayXX>.dto.PokemonDto     // 입력
import com.survivalcoding.<dayXX>.model.Pokemon      // 출력
```

Mapper 파일의 import 는 **이 두 줄로 끝** 이다. 아래가 보이면 책임이 잘못 들어온 것이다.

| import                                   | 의미                                                   |
|------------------------------------------|--------------------------------------------------------|
| `kotlinx.serialization.*`, `Json`        | 파싱이 Mapper 로 들어왔다 → DataSource 의 일           |
| `io.ktor.*`, `java.net.*`                | 통신이 Mapper 로 들어왔다 → DataSource 의 일           |
| `...datasource.*`, `Response`            | Mapper 가 DataSource 를 안다. Mapper 는 DTO 만 받는다  |
| `...repository.*`                        | 방향이 거꾸로. Repository 가 Mapper 를 부르는 것이다   |
| `kotlinx.coroutines.*`                   | `toModel()` 은 `suspend` 가 아니다. 변환은 즉시 끝난다 |

- `toModel()` 에 `suspend` 를 붙이지 않는다. 통신은 DataSource 에서 이미 끝났고, Mapper 는 메모리 안의 객체를 옮겨 담을 뿐이다.

---

## 4. 누가 부르나 — 호출 위치

> **`toModel()` 을 부르는 곳은 Repository 구현체 한 곳뿐이다.**

```kotlin
// PokemonRepositoryImpl.kt  (원본 중 toModel() 을 부르는 부분)
import com.survivalcoding.<dayXX>.mapper.toModel

    override suspend fun getPokemon(name: String): Pokemon? {
        try {
            val response = dataSource.getPokemon(name)

            val dto: PokemonDto = response.body ?: return null

            return dto.toModel()

        } catch (ex: Exception) {
            return null
        }
    }
```

| 계층                 | `toModel()` 호출 | 이유                                                                                   |
|----------------------|------------------|----------------------------------------------------------------------------------------|
| DataSource           | X                | DataSource 는 DTO 까지만 만든다. 선생님 주석: "데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자" |
| **Repository 구현체** | **O**           | DTO 를 받자마자 모델로 바꾼다. 이 줄 아래로는 DTO 가 보이면 안 된다                   |
| Repository 인터페이스 | X               | 인터페이스는 `Pokemon?` 만 약속한다. DTO 도 Mapper 도 모른다                           |
| `main` / 화면        | X                | 모델만 받는다. `toModel` 을 import 할 일이 없다                                        |

- 확장 함수는 **import 해야 보인다.** `import com.survivalcoding.<dayXX>.mapper.toModel` 한 줄이 Repository 구현체 파일에 있어야 `dto.toModel()` 이 컴파일된다. 패키지가 다르면 IDE 자동 완성에도 안 뜰 수 있으니 직접 적는다.
- `response.body ?: return null` 이 먼저, `dto.toModel()` 이 다음이다. **body 가 `null` 인지는 Repository 가 판단** 하고, DTO 안의 필드가 `null` 인지는 Mapper 가 처리한다. 두 `null` 의 책임이 다르다.

| `null` 의 종류                  | 누가 처리        | 어떻게                        |
|---------------------------------|------------------|-------------------------------|
| `Response.body` 자체가 `null`   | Repository       | `?: return null` (모델 없음)  |
| DTO 안의 필드가 `null`          | Mapper           | `?: 0`, `?: ""` (기본값)      |

---

## 5. 테스트 — `ToModelTest`

Mapper 테스트는 **입력 DTO 하나 → 출력 모델 하나** 를 비교하는 가장 단순한 형태다. Fake 도 Mock 도 코루틴도 필요 없다.

```kotlin
// ToModelTest.kt  (원본 전체)
package com.survivalcoding.<dayXX>.mapper

import com.survivalcoding.<dayXX>.dto.PokemonDto
import com.survivalcoding.<dayXX>.model.Pokemon
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ToModelTest {
    @Test
    fun `Dto 가 모델로 잘 변환되어야 한다`() {
        val dto = PokemonDto(
            id = 1,
            name = "name",
        )

        val model: Pokemon = dto.toModel()

        assertEquals(dto.id, model.id)
        assertEquals(dto.name, model.name)
    }

    @Test
    fun `Dto 에 null 이 포함되면 기본값으로 설정되어야 한다`() {
        val dto = PokemonDto(
            id = null,
            name = null,
        )

        val model: Pokemon = dto.toModel()

        assertEquals(0, model.id)
        assertEquals("", model.name)
    }
}
```

### 5-1. 테스트 두 개가 검증하는 규칙

| 테스트                                              | 검증하는 규칙                 | Given (DTO)                  | Then (모델)                      |
|-----------------------------------------------------|-------------------------------|------------------------------|----------------------------------|
| `Dto 가 모델로 잘 변환되어야 한다`                   | 2-1 ① 값이 그대로 옮겨진다    | `id = 1`, `name = "name"`    | `model.id == 1`, `model.name == "name"` |
| `Dto 에 null 이 포함되면 기본값으로 설정되어야 한다` | 2-1 ② `null` 은 기본값이 된다 | `id = null`, `name = null`   | `model.id == 0`, `model.name == ""` |

### 5-2. 이 테스트에서 배울 것

1. **DTO 를 두 필드만으로 만들 수 있다.** `PokemonDto(id = 1, name = "name")` — 나머지 18개 필드는 기본값 `null` 이라 적지 않아도 된다. [dto.md](dto.md) 2-1 ④ 규칙이 테스트를 짧게 만든다.
2. **Mapper 테스트는 "모델 필드마다 하나의 assert"** 다. 모델 필드가 셋이면 assert 가 셋이어야 한다.
3. **정상 케이스 / `null` 케이스** 두 개가 기본 세트다. 모델 필드가 늘어도 이 두 개 안에서 assert 만 늘린다.
4. 현재 원본은 `id`, `name` 두 필드만 검증하고 **`imageUrl` 은 검증하지 않는다.** `sprites?.frontDefault` 가 두 단계 접근이라 오히려 깨지기 쉬운 자리인데 빠져 있다. 새 Mapper 를 만들 때는 중첩 필드도 반드시 넣는다.

```kotlin
// 원본에 없는 보강 예시 — imageUrl 까지 검증하려면 이렇게 덧붙인다
    @Test
    fun `sprites 가 null 이면 imageUrl 은 빈 문자열이어야 한다`() {
        val dto = PokemonDto(sprites = null)

        val model: Pokemon = dto.toModel()

        assertEquals("", model.imageUrl)
    }
```

- 테스트 클래스의 이름 · 위치 · 작성 규칙 전체는 [test.md](test.md) 를 따른다.

---

## 6. 새로운 Mapper 추가 템플릿

새 모델 ↔ DTO 쌍이 생기면 다음 순서를 따른다.

1. **모델 필드를 먼저 확정** 한다. `<모델명>.kt` 는 순수 코틀린, 전부 non-null. ([dto.md](dto.md) 0-1)
2. 모델 필드마다 **DTO 의 어느 경로에서 오는지** 0-1 같은 표로 적는다. 경로가 두 단계 이상이면 `?.` 체인이 된다.
3. `<모델명>Mapper.kt` 를 `mapper` 패키지에 만들고 `fun <모델명>Dto.toModel(): <모델명>` 을 적는다. import 는 DTO 와 모델 두 줄.
4. 필드마다 `<DTO 경로> ?: <기본값>` 한 줄. 숫자는 `0`, 문자열은 `""`.
5. `ToModelTest` 에 정상 케이스 하나, `null` 케이스 하나. 모델 필드마다 assert 하나.
6. Repository 구현체에서 `import ...mapper.toModel` 을 적고 `dto.toModel()` 을 부른다. ([repository.md](repository.md))

### 6-1. 뼈대

```kotlin
// <모델명>Mapper.kt
package com.survivalcoding.<dayXX>.mapper

import com.survivalcoding.<dayXX>.dto.<모델명>Dto
import com.survivalcoding.<dayXX>.model.<모델명>

fun <모델명>Dto.toModel(): <모델명> {
    return <모델명>(
        id = id ?: 0,                                  // Int?    → Int
        name = name ?: "",                             // String? → String
        imageUrl = sprites?.frontDefault ?: "",        // 중첩: ?. 로 내려가고 ?: 는 끝에 한 번
    )
}
```

### 6-2. 여러 건일 때

실습 코드는 단건(`PokemonDto` 하나)만 다룬다. DTO 가 목록으로 오면 Mapper 에 목록용 함수를 따로 만들지 않고, **부르는 쪽(Repository)에서** `map` 을 쓴다.

```kotlin
// Repository 구현체 안 — 실습 코드에는 없는 패턴
val models: List<Pokemon> = dtos.map { it.toModel() }
```

- `fun List<PokemonDto>.toModels()` 를 추가하면 Mapper 가 목록이라는 "모양" 까지 알게 된다. 단건 변환 하나만 두고 목록은 `map` 에 맡기는 쪽이 짧다.

---

## 7. 커밋 전 체크리스트

- [ ] 파일이 `<모델명>Mapper.kt` 이고 `mapper` 패키지에 있는가
- [ ] 함수가 `fun <모델명>Dto.toModel(): <모델명>` 꼴의 **최상위 확장 함수** 인가 (`object` / `class` 아님)
- [ ] import 가 DTO 와 모델 **두 줄뿐** 인가 (`Json`, Ktor, `datasource`, `repository`, `coroutines` 없음)
- [ ] 모델 필드마다 `?:` 기본값이 있고, `!!` 가 없는가
- [ ] 중첩 접근이 `?.` 체인이고 `?:` 는 마지막에 한 번인가
- [ ] 계산 · 문자열 가공 · 조건 분기 · 정렬 · 필터가 없는가 (있으면 Repository 로)
- [ ] `suspend` 가 아닌가
- [ ] 모델에 없는 필드를 억지로 넣지 않았고, DTO 의 안 쓰는 필드를 모델에 끌어오지 않았는가
- [ ] `ToModelTest` 에 정상 케이스 · `null` 케이스가 있고, 모델 필드마다 assert 가 있는가 (중첩 필드 포함)
- [ ] `toModel()` 을 부르는 곳이 Repository 구현체 한 곳뿐인가
