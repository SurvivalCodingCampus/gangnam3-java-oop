# 레시피 앱 Repository 설계 / 기능 블랙박스 스펙

> **전제:** 필터, 정렬, 집계, 데이터 조합, 알림 생성은 모두 서버가 처리해서 내려준다. 앱은 입력값 검증과 규칙 판정만 한다.

## 1. Repository 인터페이스 설계

### 1-1. 책임과 역할

| 구분 | Repository가 한다 | Repository가 하지 않는다 → 기능 함수의 책임 |
|---|---|---|
| 조회 | ID·조건으로 도메인 모델을 찾아서 돌려준다. 필터·정렬은 조건으로 서버에 넘긴다 | 검색어 같은 입력값이 유효한지 판정한다 |
| 저장 | 도메인 모델을 추가·변경·삭제한다 | 이 동작을 해도 되는지 판정한다 (본인 레시피 평가 등) |
| 집계 | 서버가 계산한 개수·평균을 돌려준다 (`Recipe.rate`, `Recipe.savedCount` 등) | 없음. 앱에서 직접 집계하지 않는다 |
| 범위 | 서버가 가공한 결과를 도메인 모델로 돌려준다 | 없음. 데이터 조합과 알림 생성은 서버가 한다 |

- 도메인 객체 또는 Aggregate 1개당 Repository 1개를 둔다.
- 이름은 `<Model>Repository`로 짓고, 구현체는 `<Model>RepositoryImpl`로 짓는다. 구현체는 `<Model>DataSource`를 생성자로 주입받는다.
- 모델이 없는 저장·팔로우 관계는 예외로 `SavedRecipeRepository`, `FollowRepository`를 둔다.
- 저장 매체 입출력과 직렬화는 DataSource의 책임이다.
- 메서드는 `Result<T, NetworkError>`를 반환한다. 아래 표에는 성공 타입 `T`만 적는다. 로컬 저장값을 읽는 `getCurrentUserId()`만 예외로 `Long?`을 바로 반환한다.
- 메서드는 Figma 화면의 동작이나 2·3장의 기능에서 쓰이는 것만 둔다.

### 1-2. 도메인 객체별 Repository

| 도메인 객체 | Repository | 비고 |
|---|---|---|
| `User` | `UserRepository` | 인증은 `AuthRepository`로 분리 |
| `Recipe` | `RecipeRepository` | `Ingredient`, `Step` 포함 |
| `Ingredient`, `Step` | 없음 | `Recipe` Aggregate의 일부 |
| `Rating` | `RatingRepository` | |
| `Review` | `ReviewRepository` | |
| 저장 관계 (모델 없음) | `SavedRecipeRepository` | 앱은 요청만 보내고 서버가 관리한다 |
| 팔로우 관계 (모델 없음) | `FollowRepository` | 앱은 요청만 보내고 서버가 관리한다 |
| `Notification` | `NotificationRepository` | |
| `RecipeCategory`, `Rate`, `NotificationType` | 없음 | 고정 값 enum |

### 1-3. Slice\<T\> (무한 스크롤)

무한 스크롤로 이어서 불러온 조회 결과를 담는 공통 값 객체다. 도메인 모델이 아니고 저장되지 않는다.

| 속성 | 타입 | 의미 |
|---|---|---|
| content | List\<T\> | 이번에 불러온 항목 |
| nextCursor | String? | 다음 조회에 넘길 위치 (null = 마지막) |

- 입력의 `cursor: String?`는 이어서 불러올 위치다. 첫 조회는 null이고, 다음부터는 이전 결과의 `nextCursor`를 넘긴다.
- `size: Int`는 한 번에 불러올 항목 수다.
- 화면에 보여주는 목록은 모두 `Slice<T>`로 돌려준다.

### 1-4. 메서드

#### AuthRepository

- **책임:** 로그인, 회원가입, 현재 로그인 사용자 확인. 비밀번호는 입력값으로만 쓰고 `User`에 담지 않는다.
- **하지 않는 일:** 비밀번호 확인 일치, 약관 동의, 이메일 중복 판정.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `signIn(email, password)` | User | Sign In |
| `signInWithGoogle(idToken)` | User | Sign In - Google |
| `signInWithFacebook(accessToken)` | User | Sign In - Facebook |
| `signUp(name, email, password)` | User | Sign Up(기능 ⑤) |
| `getCurrentUserId()` | Long? | 로그인 여부 확인, 각 기능의 `userId` |

> 소셜 로그인 입력값은 Google `idToken`, Facebook `accessToken`으로 둔다 **(가정)**.

#### UserRepository

- **책임:** 사용자 정보 조회.
- **하지 않는 일:** 레시피 수, 팔로워 수 계산.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `getUser(userId)` | User | Home 인사말(기능 ②), Profile, 작성자 정보 |
| `existsByEmail(email)` | Boolean | Sign Up 이메일 중복 확인(기능 ⑤) |

#### RecipeRepository

- **책임:** `Ingredient`, `Step`을 포함한 `Recipe`를 조건별로 조회.
- **하지 않는 일:** 검색어 검증, 작성자 여부 판정.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `getRecipe(recipeId)` | Recipe | 재료·조리 탭(기능 ①), 작성자 확인(기능 ④) |
| `getRecipesByCategory(category, cursor, size)` | Slice\<Recipe\> | Home 카테고리 탭 (`All` 포함) |
| `getNewRecipes(cursor, size)` | Slice\<Recipe\> | Home - New Recipes |
| `getRecipesByAuthor(userId, cursor, size)` | Slice\<Recipe\> | Profile 레시피 목록 |
| `searchRecipes(keyword, filter, cursor, size)` | Slice\<Recipe\> | Search Result(기능 ③) |
| `getSearchCount(keyword, filter)` | Int | `255 results`(기능 ③). 첫 조회에서만 호출한다 |

> `Recipe.rate`는 서버가 `Rating.rate` 평균으로 계산한 값이다. 검색의 별점·카테고리 필터와 정렬도 서버가 처리한다.
> `userName`, `userImage`, `isBookmarked`, `reviewCount`, `savedCount`도 서버가 채운다. `isBookmarked`는 요청한 사용자 기준 값이다.

#### RatingRepository

- **책임:** 사용자가 레시피에 매긴 별점 저장.
- **하지 않는 일:** 본인 레시피 평가 판정.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `saveRating(rating)` | RateResult | Rate Recipe - Send(기능 ④). 신규·갱신은 서버가 처리한다 |

**반환 타입: `RateResult`**

| 속성 | 타입 | 의미 |
|---|---|---|
| averageRating | Double | 서버가 계산한 갱신 평균 |
| isUpdated | Boolean | 기존 평가를 수정했는지 여부 |

#### ReviewRepository

- **책임:** 레시피 리뷰 조회와 작성, 반응 수 반영.
- **하지 않는 일:** 빈 내용·길이 제한 판정.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `getReviews(recipeId, cursor, size)` | Slice\<Review\> | Reviews 화면 |
| `addReview(review)` | Review | Reviews - Send |
| `addLike(reviewId)` | Unit | 👍 |
| `addDislike(reviewId)` | Unit | 👎 |

> `Review`에는 개수만 있어서 누가 눌렀는지 알 수 없고, 같은 사용자의 중복 반응도 막을 수 없다.

#### SavedRecipeRepository

- **책임:** 저장한 레시피 목록 조회와 저장 관계 변경.
- **하지 않는 일:** 토글 방향 결정, 저장 알림 생성.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `getSavedRecipes(userId, cursor, size)` | Slice\<Recipe\> | Saved Recipe 화면 |
| `save(userId, recipeId)` | Int | 저장(저장 토글). 서버가 계산한 갱신 저장 수를 돌려준다 |
| `unsave(userId, recipeId)` | Int | Unsave(저장 토글). 서버가 계산한 갱신 저장 수를 돌려준다 |

#### FollowRepository

- **책임:** 사용자 간 팔로우 관계 변경.
- **하지 않는 일:** 자기 자신 팔로우 판정, 팔로우 알림 생성.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `follow(followerId, followingId)` | Unit | Follow |
| `unfollow(followerId, followingId)` | Unit | Unfollow |

#### NotificationRepository

- **책임:** 사용자의 알림 조회, 읽음 처리.
- **하지 않는 일:** Today/Yesterday 화면 그룹핑, 알림 생성.

| 메서드 | 반환 | 사용처 |
|---|---|---|
| `getNotifications(userId, isRead, cursor, size)` | Slice\<Notification\> | Notifications 화면. `isRead: Boolean?` (null = All) |
| `markAsRead(notificationId)` | Unit | 알림 확인 |

### 1-5. 설계에서 뺀 메서드

| 대상 | 이유 |
|---|---|
| `updateRatingSummary`, `updateSavedCount` | 평균 별점과 저장 수는 서버가 계산한다 |
| `getRatings` | 평균 별점을 앱에서 계산하지 않는다 |
| `getRecipes` | `getRecipesByCategory(All, cursor, size)`와 같다 |
| `isSaved`, `isFollowing`, `getRating` | 상태 확인은 서버가 모델과 응답에 담아 준다 |
| `getRecipeCount`, `getReviewCount`, `getSavedCount`, `getFollowerCount`, `getFollowingCount` | 개수는 서버가 모델에 담아 준다 |
| `getRecipesByIds`, `getUsers` | 데이터 조합은 서버가 한다 |
| `addNotification` | 알림은 서버가 만든다 |
| `RecentSearchRepository` | `architecture_Model.puml`에 `RecentSearch`가 없다 |
| `CategoryRepository` | `RecipeCategory`가 enum이다 |
| `signOut`, `deleteReview` | 화면 분석에 해당 동작이 없다 |
| 비밀번호 재설정 | `Forgot Password` 문구만 있고 이어지는 화면이 없다 |

---

## 2. Repository 직통 기능

### ① 레시피 재료·조리 순서 조회 (Recipe / Ingredient · Procedure 탭)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository.getRecipe` |
| 입력값 | `recipeId: Long` |
| 성공 데이터 | `Recipe` (`ingredients`, `steps`, `servings` 포함) |
| 규칙 실패 | 없음 |
| 직통인 이유 | 탭 내용이 `Recipe` 안에 모두 있다. `10 Items`, `10 Steps`는 리스트 크기를 그대로 표시한다. |

> 상단의 작성자 위치·팔로우 상태는 `Recipe`에 없으므로 이 기능의 범위가 아니다.

### ② 사용자 정보 조회 (Home 인사말)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `UserRepository.getUser` |
| 입력값 | `userId: Long` |
| 성공 데이터 | `User` (`Hello Jega`의 `name`, 프로필 `image`) |
| 규칙 실패 | 없음 |
| 직통인 이유 | `User` 값을 그대로 표시한다. 판단할 규칙이 없다. |

### 참고: 저장 / 저장 취소 토글 (북마크 아이콘, More → Unsave)

과제의 직통 기능 범위(1~2개) 밖이라 번호 없이 적는다.

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `SavedRecipeRepository.save` / `unsave` |
| 입력값 | `userId: Long`, `recipeId: Long` |
| 성공 데이터 | `Int` (서버가 계산한 `155 Saved` 갱신값) |
| 규칙 실패 | 없음 |
| 직통인 이유 | UI가 이미 가진 `Recipe.isBookmarked`를 보고 `save` 또는 `unsave`를 호출한다. 거부하는 규칙이 없다. |

---

## 3. 비즈니스 규칙이 필요한 기능

> 기능 함수의 실패는 규칙 실패 Enum과 Repository가 준 `NetworkError`를 구분해서 돌려준다. 아래 스펙에는 규칙 실패만 적고, 구체 타입은 구현 단계에서 정한다.

### ③ 레시피 검색 + 필터 (Search → Filter → Search Result)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository.searchRecipes` / `getSearchCount` (`cursor == null`인 첫 조회에서만) |
| 입력값 | `keyword: String`, `filter: SearchFilter`, `cursor: String?`, `size: Int` |
| 성공 데이터 | `SearchResult` |

**입력 타입: `SearchFilter`**

| 속성 | 타입 | 의미 |
|---|---|---|
| sort | SortType | `All` / `Newest` / `Oldest` / `Popularity` |
| rate | Rate? | 선택한 별점 (null = 전체) |
| category | RecipeCategory | 카테고리 (`All` = 전체) |

**성공 타입: `SearchResult`**

| 속성 | 타입 | 의미 |
|---|---|---|
| items | Slice\<Recipe\> | 이번에 불러온 검색 결과 카드 |
| totalCount | Int? | 필터 적용 후 전체 개수 (`255 results`). 이어서 불러올 때는 null |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 키워드는 앞뒤 공백 제거 후 비어 있으면 안 된다 |
| 판정 | 키워드는 2자 이상이어야 한다 **(가정)** |
| 판정 | 키워드는 50자 이하여야 한다 **(가정)** |

**규칙 실패 Enum: `SearchRecipeError`**

| Enum | 상황 |
|---|---|
| `EMPTY_KEYWORD` | 키워드가 비어 있거나 공백뿐이다 |
| `KEYWORD_TOO_SHORT` | 키워드가 2자 미만이다 |
| `KEYWORD_TOO_LONG` | 키워드가 50자를 넘는다 |

> 결과 0건은 실패가 아니다. 성공 + 빈 `Slice`(`0 results`)로 돌려준다.

### ④ 레시피 별점 남기기 (More → Rate Recipe → Send)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository.getRecipe`, `RatingRepository.saveRating` |
| 입력값 | `userId: Long`, `recipeId: Long`, `rate: Rate?` (null = 별을 선택하지 않음) |
| 성공 데이터 | `RateResult` (1-4 `RatingRepository` 참고) |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 별점을 선택해야 한다 (Figma: 별 0개면 `Send` 비활성) |
| 판정 | 본인이 작성한 레시피는 평가할 수 없다: `recipe.userId == userId` **(가정)** |

**규칙 실패 Enum: `RateRecipeError`**

| Enum | 상황 |
|---|---|
| `RATE_NOT_SELECTED` | 별점을 선택하지 않았다 |
| `CANNOT_RATE_OWN_RECIPE` | 작성자 본인이 평가를 시도했다 |

> 입력이 `Rate` enum이라 1~5 범위 검증은 필요 없다.

### ⑤ 회원가입 (Sign Up)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `UserRepository.existsByEmail`, `AuthRepository.signUp` |
| 입력값 | `name: String`, `email: String`, `password: String`, `confirmPassword: String`, `isTermsAccepted: Boolean` |
| 성공 데이터 | `User` |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 이름은 비어 있으면 안 된다 **(가정)** |
| 판정 | 이메일 형식이 올바라야 한다 **(가정)** |
| 판정 | 비밀번호는 비어 있으면 안 된다 **(가정)** |
| 판정 | 비밀번호와 비밀번호 확인이 일치해야 한다 |
| 판정 | 약관에 동의해야 한다 |
| 판정 | 이미 가입된 이메일은 쓸 수 없다. 입력값 판정을 모두 통과한 뒤 `existsByEmail`로 확인한다 |

**규칙 실패 Enum: `SignUpError`**

| Enum | 상황 |
|---|---|
| `EMPTY_NAME` | 이름이 비어 있거나 공백뿐이다 |
| `INVALID_EMAIL` | 이메일 형식이 올바르지 않다 |
| `EMPTY_PASSWORD` | 비밀번호가 비어 있다 |
| `PASSWORD_MISMATCH` | 비밀번호와 비밀번호 확인이 다르다 |
| `TERMS_NOT_ACCEPTED` | 약관에 동의하지 않았다 |
| `EMAIL_ALREADY_EXISTS` | 이미 가입된 이메일이다 |

### 스펙을 정의하지 않은 기능의 규칙 실패

과제의 규칙 기능 범위(2~3개) 밖이라 규칙 실패만 적는다.

| 기능 | Enum | 상황 |
|---|---|---|
| 리뷰 작성 | `EMPTY_CONTENT` | 내용이 비어 있거나 공백뿐이다 |
| 리뷰 작성 | `CONTENT_TOO_LONG` | 내용이 500자를 넘는다 **(가정)** |
| 팔로우 | `CANNOT_FOLLOW_SELF` | 자기 자신을 팔로우하려고 했다 |

---

## 4. 요약

| # | 기능 | 분류 | 필요한 Repository | 입력값 | 성공 데이터 | 규칙 실패 |
|---|---|---|---|---|---|---|
| ① | 재료·조리 순서 조회 | 직통 | Recipe | recipeId | Recipe | 없음 |
| ② | 사용자 정보 조회 | 직통 | User | userId | User | 없음 |
| ③ | 검색 + 필터 | 규칙 | Recipe | keyword, SearchFilter, cursor, size | SearchResult | SearchRecipeError |
| ④ | 별점 남기기 | 규칙 | Recipe, Rating | userId, recipeId, rate | RateResult | RateRecipeError |
| ⑤ | 회원가입 | 규칙 | User, Auth | name, email, password, confirmPassword, isTermsAccepted | User | SignUpError |
| 참고 | 저장 토글 | 직통 | SavedRecipe | userId, recipeId | Int | 없음 |
