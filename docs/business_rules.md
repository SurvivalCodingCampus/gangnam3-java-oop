# 레시피 앱 핵심 비즈니스 규칙 (스펙 정의)

Figma `Food Recipe App (Community)` 화면을 기준으로, 기능별 목적·비즈니스 규칙·필요한 부품(Repository)·입력값·결과물을 블랙박스 관점에서 정의합니다.
모델과 Repository 인터페이스는 [architecture.puml](architecture.puml)을 따릅니다.

## 결과물 표현 원칙

| 실패 종류                     | 담당               | 표현                                         |
| :---------------------------- | :----------------- | :------------------------------------------- |
| 데이터 실패 (통신, 서버 오류) | Repository         | `Result<T, NetworkError>`                    |
| 규칙 실패 (검증, 제한, 권한)  | 비즈니스 규칙 함수 | `Result<성공 데이터, 기능별 규칙 에러 Enum>` |

- Repository는 데이터를 가져오고 저장하는 일만 하며, 판정 코드를 두지 않습니다.
- 데이터가 없거나(404) 로그인 정보가 맞지 않는 경우는 실패가 아니라 `Success(null)`로 돌려줍니다. `NetworkError`는 통신 오류와 서버 오류에만 씁니다.
- 수업에서는 Not Found를 Repository의 데이터 실패로 다루지만, 이 과제에서는 "없음"을 통신 실패와 구분하기 위해 `Success(null)`로 표현한다. 이렇게 하면 `NetworkError`는 통신·서버 오류에만 쓰이고, "없음"에 대한 판정(`RECIPE_NOT_FOUND` 등)은 규칙 함수가 맡는다.
- 비즈니스 규칙 함수는 Repository가 돌려준 `NetworkError`를 자기 에러 Enum의 `NETWORK_ERROR`로 바꿔서 돌려줍니다.
- 비즈니스 규칙은 이 문서에만 작성하고, 다이어그램에는 그리지 않습니다.

---

## 1. Repository 직통으로 충분한 기능

판정 없이 Repository가 돌려준 데이터를 그대로 화면에 보여주는 기능입니다. 별도 함수 없이 화면(UI)에서 Repository를 바로 호출합니다.

### 1.1 저장한 레시피 목록 조회

- **목적**: Saved recipes 화면에 사용자가 저장한 레시피 카드 목록을 보여준다.
- **필요한 부품**: `SavedRecipeRepository.getSavedRecipes(userId)`
- **입력값**: `userId: Int`
- **결과물**
  - 성공: `List<Recipe>` (저장한 레시피가 없으면 빈 리스트)
  - 실패: `NetworkError`

### 1.2 레시피 검색 및 필터링

- **목적**: Search recipes 화면에서 검색어와 Filter Search(Time / Rate / Category) 조건에 맞는 레시피를 보여준다.
- **필요한 부품**: `RecipeRepository.searchRecipes(keyword, filter)`
- **입력값**
  - `keyword: String`
  - `filter: RecipeFilter` (`sortType`, `rating?`, `category?`)
- **결과물**
  - 성공: `List<Recipe>` (조건에 맞는 레시피가 없으면 빈 리스트)
  - 실패: `NetworkError`
- 별점(1~5)과 카테고리는 화면의 칩으로만 고를 수 있어 잘못된 값이 들어올 수 없으므로, 검색 조건 처리는 데이터 조회(서버 검색)로 보고 Repository에 맡깁니다.

---

## 2. 비즈니스 규칙(판정/가공) 함수가 필요한 기능

### 2.1 회원가입

- **목적**: Sign up 화면(Create an account)에서 입력값을 검증하고 계정을 만든다.
- **비즈니스 규칙**
  1. 이름은 앞뒤 공백을 제거한 뒤 1~20자여야 한다. 아니면 `INVALID_NAME`.
  2. 이메일은 `아이디@도메인.최상위도메인` 형식이어야 한다. 아니면 `INVALID_EMAIL`.
  3. 비밀번호는 8~20자이며 영문과 숫자를 모두 포함해야 한다. 아니면 `INVALID_PASSWORD`.
  4. 비밀번호와 비밀번호 확인(Confirm Password)이 같아야 한다. 아니면 `PASSWORD_MISMATCH`.
  5. 약관(Accept terms & Condition)에 동의해야 한다. 아니면 `TERMS_NOT_ACCEPTED`.
  6. 이미 가입된 이메일로는 가입할 수 없다. 이미 있으면 `EMAIL_ALREADY_EXISTS`.
  7. 위 규칙을 모두 통과한 경우에만 계정 생성을 요청한다.
  8. 두 사람이 같은 이메일로 동시에 가입하는 경우는 이 함수가 막을 수 없으므로, 서버의 이메일 중복 금지 제약이 최종적으로 보장한다.
- **필요한 부품**
  - `AuthRepository.isEmailRegistered(email)`
  - `AuthRepository.signUp(name, email, password)`
- **입력값**
  - `name: String`
  - `email: String`
  - `password: String`
  - `confirmPassword: String`
  - `isTermsAccepted: Boolean`
- **결과물**
  - 성공: `User` (생성된 사용자)
  - 실패: `SignUpError`

```kotlin
enum class SignUpError {
    INVALID_NAME,
    INVALID_EMAIL,
    INVALID_PASSWORD,
    PASSWORD_MISMATCH,
    TERMS_NOT_ACCEPTED,
    EMAIL_ALREADY_EXISTS,
    NETWORK_ERROR,
}
```

### 2.2 레시피 저장 / 저장 취소 (북마크 토글)

- **목적**: 레시피 카드의 북마크 아이콘이나 상세 화면의 `Unsave` 메뉴로 레시피를 저장하거나 저장을 취소한다.
- **비즈니스 규칙**
  1. 로그인한 사용자만 저장할 수 있다. 아니면 `NOT_LOGGED_IN`.
  2. 존재하지 않는 레시피는 저장할 수 없다. 없으면 `RECIPE_NOT_FOUND`.
  3. 이미 저장한 레시피면 저장을 취소하고, 저장하지 않은 레시피면 저장한다.
  4. 저장할 수 있는 레시피는 최대 100개다. 100개를 저장한 상태에서 새로 저장하면 `SAVED_LIMIT_EXCEEDED`. 저장 취소는 개수와 관계없이 허용한다.
  5. 결과로 토글 이후의 저장 상태를 돌려준다.
  6. 북마크를 연속으로 눌러 요청이 겹치면 확인과 저장 사이에 상태가 바뀔 수 있으므로, 중복 저장 방지와 100개 한도는 서버가 최종적으로 보장한다. 화면은 요청이 끝날 때까지 버튼을 비활성화한다.
- **필요한 부품**
  - `AuthRepository.getCurrentUser()`
  - `RecipeRepository.getRecipe(recipeId)`
  - `SavedRecipeRepository.getSavedRecipes / saveRecipe / unsaveRecipe`
- **입력값**: `recipeId: Int`
- **결과물**
  - 성공: `Boolean` (토글 후 저장 상태, `true` = 저장됨)
  - 실패: `SaveRecipeError`

```kotlin
enum class SaveRecipeError {
    NOT_LOGGED_IN,
    RECIPE_NOT_FOUND,
    SAVED_LIMIT_EXCEEDED,
    NETWORK_ERROR,
}
```

### 2.3 레시피 별점 평가

- **목적**: 상세 화면의 `Rate Recipe` 팝업에서 레시피에 별점을 매긴다.
- **비즈니스 규칙**
  1. 로그인한 사용자만 평가할 수 있다. 아니면 `NOT_LOGGED_IN`.
  2. 별점은 1~5 사이 정수여야 한다. 별을 하나도 선택하지 않으면(0) `Send` 버튼이 비활성화되며, 범위를 벗어나면 `INVALID_SCORE`.
  3. 존재하지 않는 레시피는 평가할 수 없다. 없으면 `RECIPE_NOT_FOUND`.
  4. 자신이 작성한 레시피(`recipe.chef.id == userId`)는 평가할 수 없다. `CANNOT_RATE_OWN_RECIPE`.
  5. 사용자 1명은 레시피 1개당 별점 1개만 가진다. 이미 평가했다면 새 별점으로 덮어쓴다.
- **필요한 부품**
  - `AuthRepository.getCurrentUser()`
  - `RecipeRepository.getRecipe(recipeId)`
  - `RatingRepository.saveRating(rating)`
- **입력값**
  - `recipeId: Int`
  - `score: Int`
- **결과물**
  - 성공: `Rating` (저장된 별점)
  - 실패: `RateRecipeError`

```kotlin
enum class RateRecipeError {
    NOT_LOGGED_IN,
    INVALID_SCORE,
    RECIPE_NOT_FOUND,
    CANNOT_RATE_OWN_RECIPE,
    NETWORK_ERROR,
}
```

---

## 3. 기타 정책

### 3.1 최근 검색 보관 개수

- 최대 10개까지 보관하며, 넘으면 가장 오래된 항목부터 삭제한다.
- 성공/실패를 판정하지 않는 저장 방식이므로 비즈니스 규칙 함수를 두지 않고, `RecentSearchRepository` 구현체가 보장한다.

### 3.2 리뷰 작성

Reviews 화면의 `Leave a comment` 입력란에서 리뷰를 작성할 때 지켜야 할 규칙입니다.

1. 로그인한 사용자만 작성할 수 있다. 아니면 `NOT_LOGGED_IN`.
2. 앞뒤 공백을 제거한 내용이 비어 있으면 안 된다. 비어 있으면 `EMPTY_CONTENT`.
3. 내용은 최대 500자까지 쓸 수 있다. 넘으면 `CONTENT_TOO_LONG`.

```kotlin
enum class WriteReviewError {
    NOT_LOGGED_IN,
    EMPTY_CONTENT,
    CONTENT_TOO_LONG,
    NETWORK_ERROR,
}
```
