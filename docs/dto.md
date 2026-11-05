# DTO 작성 원칙

포켓몬 API 실습의 `PokemonDto.kt` 와 그것을 주고받는 `RemotePokemonDataSource` / `PokemonMapper` / `PokemonRepositoryImpl` 을 기준으로 정리한,
새로운 DTO 를 만들 때 지켜야 할 필수 원칙이다.
DataSource 쪽 규칙은 [datasource.md](datasource.md), DTO → 모델 변환 규칙은 [mapper.md](mapper.md), Repository 쪽 규칙은 [repository.md](repository.md),
테스트 규칙은 [test.md](test.md) 를 따른다. 이 문서는 `dto` 패키지 안의 파일만 다룬다.
코드 발췌는 원본 그대로이되, `package` · `import` 줄의 수업 날짜 패키지명만 `<dayXX>` 로 바꿔 적었다.

> **한 줄 요약**
> DTO 는 "서버가 준 JSON 을 코틀린 객체로 **그대로 옮겨 적은 것**" 이다. 빼지 않고, 고치지 않고, 계산하지 않는다.
> 고치는 일(변환)은 Mapper, 생각하는 일(가공)은 Repository 몫이다.

---

## 0. DTO 가 왜 필요한가 — 모델과 분리하는 이유

로컬 파일 실습(`Item`)까지는 DataSource 가 모델을 바로 돌려줬다. 원격 API 실습(`Pokemon`)부터는 **DTO** 가 사이에 들어간다.

```
로컬 파일 : 파일 JSON ──decode──▶ Item (모델)      ──▶ Repository ──▶ main
원격 API  : 서버 JSON ──decode──▶ PokemonDto (DTO) ──▶ Repository ──toModel()──▶ Pokemon (모델) ──▶ main
```

### 0-1. 모델과 DTO 비교 (포켓몬 API 실습 코드 기준)

| 구분                   | 모델 `Pokemon.kt`                   | DTO `PokemonDto.kt`                             |
|------------------------|-------------------------------------|-------------------------------------------------|
| 누가 모양을 정하나     | **우리 앱** (필요한 것만)           | **서버** (API 응답 구조 그대로)                 |
| 필드 수                | 3개 (`id`, `name`, `imageUrl`)      | 최상위 20개 + 중첩 data class 46개 (총 47개)    |
| nullable               | 아님 (`Int`, `String`)              | **전부** nullable + 기본값 `null`               |
| 외부 라이브러리        | **금지** — 순수 코틀린              | `kotlinx.serialization` 어노테이션 사용         |
| 어디서 쓰나            | Repository 반환값, `main`, 테스트   | DataSource ↔ Mapper 사이에서만                  |
| 서버 응답이 바뀌면     | 안 바뀐다 (Mapper 만 고친다)        | 같이 바뀐다                                     |
| 파일 크기              | 9줄                                 | 594줄                                           |

```kotlin
// Pokemon.kt  (원본 그대로)
package com.survivalcoding.<dayXX>.model

// 순수 코틀린 코드
// 외부 라이브러리 금지
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
)
```

### 0-2. 분리해서 얻는 것

1. **서버가 바뀌어도 앱이 안 깨진다.** JSON 키 이름이 바뀌면 DTO 와 Mapper 두 파일만 고친다. 모델을 쓰는 `main`, 화면, Repository 테스트는 그대로다.
2. **모델이 깨끗해진다.** `Pokemon` 은 `@Serializable` 도, `null` 도 없다. 쓰는 쪽에서 `?.` 를 안 붙여도 된다.
3. **필요한 것만 남긴다.** 서버는 20개 필드를 주지만 앱은 3개만 쓴다. 나머지 17개는 DTO 에서 멈춘다.
4. **테스트가 쉬워진다.** 모든 필드에 기본값이 있어서 `PokemonDto(id = 1, name = "name")` 처럼 필요한 두 개만 넣어 만들 수 있다. ([test.md](test.md) 의 `ToModelTest` 참고)

---

## 1. 네이밍 및 위치 규칙

### 1-1. 클래스 이름

| 역할             | 규칙                                           | 예시                                                      |
|------------------|------------------------------------------------|-----------------------------------------------------------|
| 최상위 DTO       | `<모델명>Dto`                                  | `PokemonDto` (모델이 `Pokemon` 이므로)                    |
| 중첩 DTO         | JSON 키의 **의미** 를 따라 짓는다. `Dto` 안 붙임 | `PokemonAbility`, `PokemonSprites`, `NamedApiResource`    |
| 반복되는 모양    | 공통 이름 하나로 **재사용**                    | `{ name, url }` 쌍은 전부 `NamedApiResource`              |

- `Dto` 접미사는 **최상위 하나에만** 붙인다. 중첩 클래스 46개 중 `Dto` 가 붙은 것은 없다.
- 중첩 클래스 이름은 "부모 이름 + 키 이름" 꼴이 기본이다. `PokemonSprites` → `SpriteOther` → `SpriteDreamWorld`, `SpriteHome`, `SpriteOfficialArtwork` ...
- 모양(필드 구성)이 같으면 이름을 하나로 쓰고, 필드가 하나라도 다르면 이름을 따로 만든다.
  `SpriteFrontDefaultIcon` (필드 1개) / `SpriteGenderIcon` (2개) / `SpriteShinyGenderIcon` (4개) 는 그래서 셋 다 따로 있다.

### 1-2. 파일과 패키지

```
<dayXX>
├── dto/
│   └── PokemonDto.kt          // 최상위 + 중첩 47개 전부 한 파일 (594줄)
├── mapper/
│   └── PokemonMapper.kt       // PokemonDto.toModel()           → mapper.md
├── model/
│   └── Pokemon.kt             // 순수 코틀린
├── datasource/
│   ├── PokemonDataSource.kt   // Response<PokemonDto> 를 돌려줌  → datasource.md
│   ├── RemotePokemonDataSource.kt
│   └── Response.kt
└── repository/
    ├── PokemonRepository.kt
    └── PokemonRepositoryImpl.kt                                   → repository.md
```

- **API 응답 하나 = DTO 파일 하나.** 최상위 DTO 와 그 안에 쓰이는 중첩 DTO 를 전부 같은 파일에 둔다. 파일을 쪼개면 "이 중첩 클래스가 어느 응답 것인지" 를 잃는다.
- `dto/` 는 `model/`, `mapper/`, `datasource/`, `repository/` 와 **형제 패키지** 다. `model/dto/` 처럼 모델 아래에 넣지 않는다. DTO 는 모델이 아니다.
- 문서에서는 패키지를 `datasource/` 로 통일해 적는다. (실제 포켓몬 실습 코드의 패키지명은 `data_source` 라서 아래 코드 발췌의 `package` 줄은 원본 그대로 `data_source` 다.)

### 1-3. import 는 두 줄뿐

```kotlin
// PokemonDto.kt  (원본 1~4줄)
package com.survivalcoding.<dayXX>.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
```

- DTO 파일의 import 는 `kotlinx.serialization` 두 개가 전부다. `model` 패키지도, Ktor 도, `Json` 도 import 하지 않는다.
- `model` 이 보이면 DTO 가 모델을 알게 된 것이고(방향이 거꾸로), `Json` 이 보이면 파싱이 DTO 로 들어온 것이다(DataSource 의 일).

---

## 2. 필드 작성 규칙

> **JSON 키 하나 = 프로퍼티 한 줄. 모든 프로퍼티는 `@SerialName` + nullable + `= null`.**

### 2-1. 다섯 가지 규칙

| # | 규칙                                                       | 실제 코드                                                      | 어기면                                                     |
|---|------------------------------------------------------------|----------------------------------------------------------------|------------------------------------------------------------|
| ① | 모든 data class 에 `@Serializable` 을 붙인다 (중첩 포함)   | 47개 전부 `@Serializable`                                      | 중첩 하나라도 빠지면 **컴파일 에러** (Serializer 를 못 찾음) |
| ② | `@SerialName("json_key")` 로 JSON 키를 적는다              | `@SerialName("base_experience") val baseExperience`            | 키 이름이 다르면 값이 `null` 로 들어온다                   |
| ③ | 코틀린 프로퍼티는 camelCase                                | `is_default` → `isDefault`, `front_default` → `frontDefault`   | snake_case 그대로 쓰면 코틀린 컨벤션 위반                  |
| ④ | 타입은 **전부 nullable + 기본값 `null`**                   | `val id: Int? = null`, `val sprites: PokemonSprites? = null`   | 서버가 키를 빼먹으면 파싱 전체가 예외로 죽는다             |
| ⑤ | 가공 금지 — 함수, 계산 프로퍼티, `init` 없음                | 47개 전부 필드만 있다                                          | 가공이 들어오면 Mapper / Repository 와 책임이 겹친다       |

- ②는 JSON 키와 프로퍼티 이름이 **같아도 붙인다.** 원본은 `@SerialName("id") val id`, `@SerialName("name") val name` 처럼 전부 붙였다. 규칙에 예외를 두지 않아야 빠뜨린 것을 바로 찾는다.
- ④가 `Json { ignoreUnknownKeys = true }` 와 짝이다. 방향이 반대인 두 상황을 각각 막는다.

  | 상황                                | 막는 장치                                | 어디에                           |
  |-------------------------------------|------------------------------------------|----------------------------------|
  | 서버가 키를 **덜** 준다 / `null` 을 준다 | `= null` 기본값                          | DTO (이 문서)                    |
  | 서버가 키를 **더** 준다                | `ignoreUnknownKeys = true`               | DataSource 의 `Json` 설정        |

### 2-2. JSON 타입 → 코틀린 타입 대응표

`jsonString` (테스트 쪽 `FakePokemonDataSource.kt` 에 들어 있는 실제 응답) 과 `PokemonDto.kt` 를 한 줄씩 맞춘 것이다.

| JSON 값         | 코틀린 타입                        | JSON (원본)                                              | DTO (원본)                                            |
|-----------------|------------------------------------|----------------------------------------------------------|-------------------------------------------------------|
| 정수            | `Int?`                             | `"id": 132`                                              | `@SerialName("id") val id: Int? = null`               |
| 문자열          | `String?`                          | `"name": "ditto"`                                        | `@SerialName("name") val name: String? = null`        |
| true / false    | `Boolean?`                         | `"is_default": true`                                     | `@SerialName("is_default") val isDefault: Boolean? = null` |
| 객체 `{ }`      | 별도 `@Serializable data class`    | `"species": { "name": "ditto", "url": "..." }`           | `@SerialName("species") val species: NamedApiResource? = null` |
| 배열 `[ ]`      | `List<T?>?`                        | `"abilities": [ { ... }, { ... } ]`                      | `@SerialName("abilities") val abilities: List<PokemonAbility?>? = null` |
| 빈 배열 `[]`    | `List<T?>?` 그대로                 | `"past_types": []`                                       | `@SerialName("past_types") val pastTypes: List<PastType?>? = null` |
| `null`          | 그 타입의 nullable                 | `"front_female": null`                                   | `@SerialName("front_female") val frontFemale: String? = null` |

- 배열은 **리스트도, 원소도** nullable 이다. `List<PokemonAbility?>?` — 바깥 `?` 는 "키가 없을 수 있다", 안쪽 `?` 는 "원소가 `null` 일 수 있다".
- `Long` 이 아니라 `Int` 를 썼다. PokeAPI 의 id 범위가 `Int` 로 충분하기 때문이다. 모델 `Pokemon.id` 도 `Int` 로 맞춰져 있다.
- 키 이름에 하이픈이 있어도 `@SerialName` 에 그대로 적는다. `@SerialName("official-artwork") val officialArtwork`.

### 2-3. 최상위 DTO 원본

```kotlin
// PokemonDto.kt  (원본 6~50줄)
@Serializable
data class PokemonDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("base_experience")
    val baseExperience: Int? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("is_default")
    val isDefault: Boolean? = null,
    @SerialName("order")
    val order: Int? = null,
    @SerialName("weight")
    val weight: Int? = null,
    @SerialName("abilities")
    val abilities: List<PokemonAbility?>? = null,
    @SerialName("past_abilities")
    val pastAbilities: List<PastAbility?>? = null,
    @SerialName("forms")
    val forms: List<NamedApiResource?>? = null,
    @SerialName("game_indices")
    val gameIndices: List<VersionGameIndex?>? = null,
    @SerialName("held_items")
    val heldItems: List<PokemonHeldItem?>? = null,
    @SerialName("location_area_encounters")
    val locationAreaEncounters: String? = null,
    @SerialName("moves")
    val moves: List<PokemonMove?>? = null,
    @SerialName("species")
    val species: NamedApiResource? = null,
    @SerialName("sprites")
    val sprites: PokemonSprites? = null,
    @SerialName("cries")
    val cries: PokemonCries? = null,
    @SerialName("stats")
    val stats: List<PokemonStat?>? = null,
    @SerialName("past_stats")
    val pastStats: List<PastStat?>? = null,
    @SerialName("types")
    val types: List<PokemonType?>? = null,
    @SerialName("past_types")
    val pastTypes: List<PastType?>? = null
)
```

- 프로퍼티 20개가 **JSON 의 최상위 키 순서 그대로** 다. 순서를 바꾸거나 빼지 않는다. 나중에 "이 키가 DTO 에 있나" 를 JSON 과 나란히 놓고 위에서부터 맞춰 볼 수 있어야 한다.
- 모델 `Pokemon` 이 실제로 쓰는 것은 이 중 `id`, `name`, `sprites` (→ `frontDefault`) 셋뿐이다. 나머지도 **지우지 않는다.** 지금 안 쓴다고 지우면 다음에 `height` 가 필요할 때 다시 JSON 을 열어야 한다.

---

## 3. 중첩 구조 분리 규칙

> **JSON 의 `{ }` 하나 = data class 하나. 깊이에 상관없이 계속 쪼갠다.**

### 3-1. 쪼개는 방법 — `abilities` 예시

JSON 에서 `abilities[0].ability.name` 까지 가는 길을 DTO 로 옮기면 세 클래스가 된다.

```json
// jsonString 원본 (FakePokemonDataSource.kt 16~33줄)
  "abilities": [
    {
      "is_hidden": false,
      "slot": 1,
      "ability": {
        "name": "limber",
        "url": "https://pokeapi.co/api/v2/ability/7/"
      }
    },
    {
      "is_hidden": true,
      "slot": 3,
      "ability": {
        "name": "imposter",
        "url": "https://pokeapi.co/api/v2/ability/150/"
      }
    }
  ],
```

```kotlin
// ① 배열의 원소 { is_hidden, slot, ability }  →  PokemonAbility   (원본 60~68줄)
@Serializable
data class PokemonAbility(
    @SerialName("ability")
    val ability: NamedApiResource? = null,
    @SerialName("is_hidden")
    val isHidden: Boolean? = null,
    @SerialName("slot")
    val slot: Int? = null
)

// ② 그 안의 { name, url }  →  NamedApiResource   (원본 52~58줄)
@Serializable
data class NamedApiResource(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)

// ③ 최상위에서는 List 로 받는다  (PokemonDto 안)
    @SerialName("abilities")
    val abilities: List<PokemonAbility?>? = null,
```

| JSON 경로                    | 코틀린 경로                                   | 타입                     |
|------------------------------|-----------------------------------------------|--------------------------|
| `abilities`                  | `PokemonDto.abilities`                        | `List<PokemonAbility?>?` |
| `abilities[0]`               | `PokemonAbility`                              | 객체 → 클래스            |
| `abilities[0].ability`       | `PokemonAbility.ability`                      | `NamedApiResource?`      |
| `abilities[0].ability.name`  | `NamedApiResource.name`                       | `String?`                |

### 3-2. 같은 모양은 하나로 — `NamedApiResource`

PokeAPI 는 "다른 리소스를 가리키는 링크" 를 전부 `{ "name": ..., "url": ... }` 꼴로 준다. 이 모양이 나올 때마다 클래스를 새로 만들지 않고 `NamedApiResource` 하나를 재사용한다.

```json
// 전부 같은 모양이다 (jsonString 원본)
  "species": {
    "name": "ditto",
    "url": "https://pokeapi.co/api/v2/pokemon-species/132/"
  },
  "forms": [
    {
      "name": "ditto",
      "url": "https://pokeapi.co/api/v2/pokemon-form/132/"
    }
  ],
```

`NamedApiResource` 가 쓰인 자리 (PokemonDto.kt 안에서):
`PokemonDto.forms`, `PokemonDto.species`, `PokemonAbility.ability`, `PastAbility.generation`, `VersionGameIndex.version`, `PokemonHeldItem.item`,
`PokemonHeldItemVersion.version`, `PokemonMove.move`, `PokemonMoveVersion.moveLearnMethod`, `PokemonMoveVersion.versionGroup`, `PokemonStat.stat` ...

### 3-3. 모양이 다르면 이름도 다르게 — `Sprite*` 계열

`sprites` 키 아래는 세대별 · 게임별로 이미지 URL 묶음이 다르게 생겼다. 그래서 30개가 넘는 `Sprite*` 클래스가 있다.

```kotlin
// 최상위 sprites  (원본 164~186줄)
@Serializable
data class PokemonSprites(
    @SerialName("front_default")
    val frontDefault: String? = null,          // ← Mapper 가 Pokemon.imageUrl 로 쓰는 값
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null,
    @SerialName("back_default")
    val backDefault: String? = null,
    @SerialName("back_shiny")
    val backShiny: String? = null,
    @SerialName("back_female")
    val backFemale: String? = null,
    @SerialName("back_shiny_female")
    val backShinyFemale: String? = null,
    @SerialName("other")
    val other: SpriteOther? = null,
    @SerialName("versions")
    val versions: SpriteVersions? = null
)

// sprites.other  (원본 188~198줄)
@Serializable
data class SpriteOther(
    @SerialName("dream_world")
    val dreamWorld: SpriteDreamWorld? = null,
    @SerialName("home")
    val home: SpriteHome? = null,
    @SerialName("official-artwork")
    val officialArtwork: SpriteOfficialArtwork? = null,
    @SerialName("showdown")
    val showdown: SpriteBaseAnimation? = null
)
```

필드 구성이 조금씩 다른 세 클래스. 필드가 하나라도 다르면 합치지 않는다.

```kotlin
// 원본 570~594줄
@Serializable
data class SpriteFrontDefaultIcon(
    @SerialName("front_default")
    val frontDefault: String? = null
)

@Serializable
data class SpriteGenderIcon(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null
)

@Serializable
data class SpriteShinyGenderIcon(
    @SerialName("front_default")
    val frontDefault: String? = null,
    @SerialName("front_female")
    val frontFemale: String? = null,
    @SerialName("front_shiny")
    val frontShiny: String? = null,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String? = null
)
```

### 3-4. PokemonDto.kt 의 data class 47개 전체 목록

| 묶음                      | 클래스                                                                                                                                                                                                                                                                                                                                                                               | 개수 |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------|
| 최상위                    | `PokemonDto`                                                                                                                                                                                                                                                                                                                                                                         | 1    |
| 공통 링크                 | `NamedApiResource`                                                                                                                                                                                                                                                                                                                                                                   | 1    |
| 능력 · 게임 · 아이템 · 기술 | `PokemonAbility`, `PastAbility`, `VersionGameIndex`, `PokemonHeldItem`, `PokemonHeldItemVersion`, `PokemonMove`, `PokemonMoveVersion`                                                                                                                                                                                                                                               | 7    |
| 울음소리 · 스탯 · 타입    | `PokemonCries`, `PokemonStat`, `PastStat`, `PokemonType`, `PastType`                                                                                                                                                                                                                                                                                                                | 5    |
| 스프라이트 (이미지)       | `PokemonSprites`, `SpriteOther`, `SpriteDreamWorld`, `SpriteHome`, `SpriteOfficialArtwork`, `SpriteArtworkVersions`, `SpriteArtworkGenI`, `SpriteArtworkGenII`, `SpriteDefaultOnly`, `SpriteVersions`, `SpriteGenI`, `SpriteClassicGb`, `SpriteClassicYellow`, `SpriteClassicJapan`, `SpriteGenII`, `SpriteGoldSilver`, `SpriteCrystal`, `SpriteGenIII`, `SpriteGenIIIBase`, `SpriteGenIIIAnimation`, `SpriteGenIV`, `SpriteGenIVItem`, `SpriteGenV`, `SpriteGenVIcons`, `SpriteGenVI`, `SpriteGenVII`, `SpriteGenVIII`, `SpriteGenIX`, `SpriteGenIXPlatform`, `SpriteBaseAnimation`, `SpriteFrontDefaultIcon`, `SpriteGenderIcon`, `SpriteShinyGenderIcon` | 33   |

- 세는 법: `grep -c "^data class" PokemonDto.kt` → `47`.

---

## 4. DTO 가 흐르는 길 — 어디까지 가도 되나

> **DTO 는 DataSource 에서 태어나 Mapper 에서 죽는다. 모델이 쓰이는 곳(`main`, 화면)까지 가면 안 된다.**

```
서버 JSON ──decodeFromString──▶ PokemonDto ──Response<PokemonDto>──▶ Repository ──dto.toModel()──▶ Pokemon ──▶ main
           └── RemotePokemonDataSource ──┘                                        └── PokemonMapper ──┘
```

### 4-1. 파일별 import 허용 표

| 파일                                 | `PokemonDto` import | 이유                                                                 |
|--------------------------------------|---------------------|----------------------------------------------------------------------|
| `PokemonDataSource.kt`               | **O**               | 인터페이스 반환 타입이 `Response<PokemonDto>` 다                     |
| `RemotePokemonDataSource.kt`         | **O**               | `json.decodeFromString<PokemonDto>()` 로 만든다                      |
| `PokemonMapper.kt`                   | **O**               | `PokemonDto.toModel()` 의 수신 객체다                                |
| `PokemonRepositoryImpl.kt`           | **O**               | `response.body` 를 `PokemonDto` 로 받아 `toModel()` 을 부른다        |
| `PokemonRepository.kt`               | **X**               | 인터페이스는 모델 `Pokemon?` 만 돌려준다                             |
| `Pokemon.kt`                         | **X**               | 모델은 외부 라이브러리도, DTO 도 모른다                              |
| `Main.kt`                            | **X**               | `main` 은 `Pokemon` 만 본다                                          |

### 4-2. 실제로 DTO 가 오가는 네 줄

```kotlin
// PokemonDataSource.kt  (원본 전체)
package com.survivalcoding.<dayXX>.data_source

import com.survivalcoding.<dayXX>.dto.PokemonDto

interface PokemonDataSource {
    suspend fun getPokemon(name: String): Response<PokemonDto>
}
```

```kotlin
// RemotePokemonDataSource.kt  (원본 중 DTO 를 만드는 부분)
    override suspend fun getPokemon(name: String): Response<PokemonDto> {
        val response = httpClient.get("$baseUrl/pokemon/$name")
        val jsonString = response.bodyAsText()
        return Response(
            statusCode = response.status.value,
            body = json.decodeFromString<PokemonDto>(jsonString)
        )
    }
```

```kotlin
// PokemonRepositoryImpl.kt  (원본 중 DTO 를 소비하는 부분)
            val response = dataSource.getPokemon(name)

            val dto: PokemonDto = response.body ?: return null

            return dto.toModel()
```

- DataSource 는 **문자열 → DTO** 까지만 한다. (선생님 주석: "데이터 관련 복잡한 거 최대한 다해서 DTO 만 돌려주자")
- Repository 는 DTO 를 **받자마자 모델로 바꾼다.** `dto.toModel()` 다음 줄부터는 DTO 가 보이면 안 된다.
- `Response<T>` 자체의 규칙은 [datasource.md](datasource.md) 에 있다.

---

## 5. 새로운 DTO 추가 템플릿

새 API 응답을 받게 되면 다음 순서를 따른다.

1. **응답 JSON 을 한 번 받아 저장한다.** 브라우저나 `.http` 파일로 요청해서 결과를 통째로 복사한다.
   이 문자열이 그대로 테스트의 `jsonString` 이 된다. (포켓몬 실습은 테스트 쪽 `FakePokemonDataSource.kt` 에 `const val jsonString` 으로 두었다.)
2. `<모델명>Dto.kt` 파일을 `dto` 패키지에 만들고 import 두 줄을 적는다.
3. **최상위 키부터** 위에서 아래로 한 줄씩 옮긴다. 키마다 `@SerialName`, 타입은 2-2 표, 전부 `? = null`.
4. 값이 `{ }` 이면 새 data class 를 **같은 파일 아래쪽** 에 만들고, 값이 `[ { } ]` 이면 `List<그클래스?>?` 로 받는다. 모양이 같은 객체는 하나로 재사용한다.
5. 모든 data class 에 `@Serializable` 이 붙었는지 위에서부터 센다.
6. `Json { ignoreUnknownKeys = true }.decodeFromString<...Dto>(jsonString)` 이 예외 없이 통과하는지 확인한다.
   `FakePokemonDataSource` 가 정확히 이 한 줄을 하므로, Fake 를 만들고 Repository 테스트를 돌리면 같이 확인된다.
7. 모델에 필요한 필드만 Mapper 로 꺼낸다. → [mapper.md](mapper.md)

### 5-1. 뼈대

```kotlin
// <모델명>Dto.kt
package com.survivalcoding.<dayXX>.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class <모델명>Dto(
    @SerialName("id")
    val id: Int? = null,                              // 정수
    @SerialName("some_key")                           // JSON 키는 그대로
    val someKey: String? = null,                      // 프로퍼티는 camelCase
    @SerialName("is_active")
    val isActive: Boolean? = null,                    // true / false
    @SerialName("owner")
    val owner: NamedApiResource? = null,              // { } → 별도 클래스
    @SerialName("tags")
    val tags: List<NamedApiResource?>? = null,        // [ { } ] → List<T?>?
)

@Serializable
data class NamedApiResource(                          // 중첩 클래스는 같은 파일 아래쪽에
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)
```

### 5-2. 의존성 (`build.gradle.kts` 원본 중 DTO 와 관련된 줄)

```kotlin
plugins {
    kotlin("plugin.serialization") version "2.4.21"
}

dependencies {
    // 직렬화
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}
```

- 플러그인이 없으면 `@Serializable` 이 아무 일도 하지 않는다. 라이브러리와 플러그인 **둘 다** 있어야 한다.
- `kotlin("jvm")` 과 `kotlin("plugin.serialization")` 의 버전은 **같게** 맞춘다. 현재 둘 다 `2.4.21` 이다. (원래 `jvm` 이 `2.4.20` 이었는데 2026-11-05 에 `2.4.21` 로 맞췄다.)

---

## 6. 커밋 전 체크리스트

- [ ] 최상위 클래스 이름이 `<모델명>Dto` 이고, 파일 `<모델명>Dto.kt` 가 `dto` 패키지에 있는가
- [ ] 중첩 클래스까지 **전부** 같은 파일에 있고, 전부 `@Serializable` 인가
- [ ] 모든 프로퍼티에 `@SerialName("json_key")` 가 있고, 프로퍼티 이름은 camelCase 인가
- [ ] 모든 프로퍼티가 nullable 이고 기본값이 `= null` 인가 (리스트는 `List<T?>?`)
- [ ] JSON 의 최상위 키 순서와 프로퍼티 순서가 같은가. 빠진 키가 없는가
- [ ] 같은 모양 `{ name, url }` 을 `NamedApiResource` 로 재사용했는가. 모양이 다른데 합치지 않았는가
- [ ] DTO 파일의 import 가 `kotlinx.serialization` 두 줄뿐인가 (`model`, `Json`, Ktor 없음)
- [ ] 함수 · 계산 프로퍼티 · `init` 이 없는가
- [ ] `PokemonDto` 를 import 하는 파일이 DataSource 인터페이스·구현체, Mapper, `PokemonRepositoryImpl.kt` 뿐인가 (`Pokemon.kt`, `Main.kt`, `PokemonRepository.kt` 에 없음)
- [ ] `decodeFromString` 이 저장해 둔 `jsonString` 으로 예외 없이 통과하는가
