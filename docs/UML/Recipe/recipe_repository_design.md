# 레시피 앱 Repository / 기능 스펙 설계 (RecipeDTO_Claude.puml 기준)

> 기준 DTO: [UML/RecipeDTO_Claude.puml](RecipeDTO_Claude.puml)
> 코드 작성 없이 **DTO → 도메인 모델 → Repository → 기능 블랙박스 스펙 → 비판적 검토** 순서로 정리한다.
> 표기: Kotlin 시그니처 형태, 반환은 모두 `Result<T, NetworkError>` (`day20.core`) — 표에는 `T`만 적는다.

---

## 0. 전제

| 전제 | 내용 |
|---|---|
| 데이터 출처 | 서버 API. DataSource가 DTO를 받고, **Repository는 DTO → 도메인 모델로 변환해서** 넘긴다. 화면/UseCase는 DTO를 모른다. |
| 로그인 사용자 | 토큰으로 서버가 식별한다. 그래서 "내 것" 조회 메서드에는 `userId`를 넘기지 않는다 (puml 규칙과 동일). |
| 필터·정렬·페이징 | 서버가 쿼리 파라미터로 처리한다. Repository는 **조건을 전달만** 한다. |
| 평균 별점·저장 여부·작성자 정보 | 서버 응답(`rating`, `bookmarked`, `author`)에 이미 들어 있다. 클라이언트가 다시 계산하지 않는다. |

---

## 1. DTO → 도메인 모델

| 도메인 모델 | 원본 DTO | 비고 |
|---|---|---|
| `AuthToken` | `AuthTokenResponse` | accessToken, refreshToken, expiresIn, isNewUser |
| `Author` | `AuthorSummaryDto` | 카드·리뷰 작성자 |
| `RecipeSummary` | `RecipeCardDto` | Home / Search / Saved / Profile 카드 공용 |
| `Recipe` | `RecipeDetailResponse` (+ `RecipeAuthorDto`, `IngredientDto`, `StepDto`) | Aggregate. `Ingredient`, `Step`은 Recipe 안에만 존재 |
| `Home` | `HomeResponse` | me, categories, recipes, newRecipes |
| `Category` | `CategoryDto` | |
| `RecipeQuery` (값 객체) | `RecipeSearchRequest` | keyword, sort, minRating, categoryId |
| `RatingResult` | `RatingResponse` | myScore, averageRating |
| `BookmarkState` | `BookmarkResponse` | recipeId, isBookmarked |
| `Review`, `ReviewPage` | `ReviewDto`, `ReviewListResponse` | |
| `Notification` | `NotificationDto` | |
| `Profile` | `ProfileResponse` | |
| `FollowState` | `FollowResponse` | |
| `Page<T>` | `PageResponse<T>` | items, page, totalCount, hasNext |

---

## 2. Repository 인터페이스

### 책임과 역할

- **한다:** 데이터를 어디서 가져오는지 숨기고, DTO를 도메인 모델로 바꿔서 `Result`로 넘긴다. 실패는 `NetworkError`로만 표현한다.
- **안 한다:** "해도 되는가?" 판정(권한, 개수 제한, 입력값 범위), 여러 Repository 결과 조합, 정책 에러 생성. → 기능(UseCase)의 책임.
- 도메인 단위로 하나씩 둔다. `Ingredient`, `Step`은 `Recipe` Aggregate 소속이라 별도 Repository가 없다.

### AuthRepository — 인증
| 메서드 | 반환 | 화면 |
|---|---|---|
| `signIn(email: String, password: String)` | `AuthToken` | Sign In |
| `signUp(name: String, email: String, password: String, confirmPassword: String, termsAgreed: Boolean)` | `AuthToken` | Sign Up |
| `signInWithOAuth(provider: OAuthProvider, idToken: String)` | `AuthToken` | Google / Facebook 버튼 (로그인·가입 공용) |
| `signOut()` | `Unit` | - |
| `getCurrentUserId()` | `Long?` | 로그인 여부 / 본인 판정용 (로컬 토큰에서 읽음) |

### RecipeRepository — 레시피 (Aggregate)
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getHome()` | `Home` | Home |
| `getRecipe(recipeId: Long)` | `Recipe` | Recipe / Ingredient · Procedure |
| `searchRecipes(query: RecipeQuery, page: Int, size: Int)` | `Page<RecipeSummary>` | Search Result, Home 카테고리 탭 (`categoryId`만 채움) |
| `getUserRecipes(userId: Long, tab: ProfileTab, page: Int, size: Int)` | `Page<RecipeSummary>` | Profile - Recipe / Videos / Tag |
| `getShareLink(recipeId: Long)` | `String` | More → share |

### CategoryRepository — 카테고리
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getCategories()` | `List<Category>` | Filter Category |

### BookmarkRepository — 저장(북마크)
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getBookmarks(page: Int, size: Int)` | `Page<RecipeSummary>` | Saved Recipe |
| `getBookmarkCount()` | `Int` | 저장 한도 판정용 데이터 (판정은 안 함) |
| `addBookmark(recipeId: Long)` | `BookmarkState` | 북마크 아이콘 |
| `removeBookmark(recipeId: Long)` | `BookmarkState` | 북마크 아이콘, More → Unsave |

### RatingRepository — 별점
| 메서드 | 반환 | 화면 |
|---|---|---|
| `rate(recipeId: Long, score: Int)` | `RatingResult` | More → Rate Recipe → Send |

### ReviewRepository — 리뷰
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getReviews(recipeId: Long, page: Int, size: Int)` | `ReviewPage` | Reviews (`13k Comments`, `155 Saved` 포함) |
| `addReview(recipeId: Long, content: String)` | `Review` | Leave a comment → Send |
| `react(reviewId: Long, type: ReactionType)` | `Review` | 👍 / 👎 |

### NotificationRepository — 알림
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getNotifications(filter: NotificationFilter, page: Int, size: Int)` | `Page<Notification>` | Notifications All / Read / Unread |
| `markAsRead(notificationId: Long)` | `Unit` | 알림 탭 |

### UserRepository — 프로필
| 메서드 | 반환 | 화면 |
|---|---|---|
| `getProfile(userId: Long)` | `Profile` | Profile |

### FollowRepository — 팔로우
| 메서드 | 반환 | 화면 |
|---|---|---|
| `follow(userId: Long)` | `FollowState` | Recipe 상세 / Profile `Follow` |
| `unfollow(userId: Long)` | `FollowState` | 위와 동일 |

---

## 3. 기능 분류

| 구분 | 기준 | 해당 기능 |
|---|---|---|
| **Repository 직통** | 받은 데이터를 그대로 그린다. 판정·조합 없음. 실패는 `NetworkError`뿐. | ① 레시피 상세 조회, ② 저장한 레시피 목록 |
| **비즈니스 규칙 필요** | 호출 전에 "해도 되는가?"를 판정하거나, 여러 Repository를 조합한다. 규칙 실패가 존재. | ③ 레시피 검색, ④ 별점 남기기, ⑤ 북마크 토글 |

---

## 4. 기능별 블랙박스 스펙

### ① 레시피 상세 조회 — 직통 (UI → Repository)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository` |
| 입력값 | `recipeId: Long` |
| 성공 데이터 | `Recipe` (author, ingredients, steps, rating, reviewCount, bookmarked 포함. `10 Items` / `10 Steps`는 화면에서 `size`) |
| 실패 | `NetworkError` 그대로 (`NO_INTERNET_ERR`, `SERVER_ERR`, `CLIENT_ERR`(없는 레시피 404), `TIMEOUT_ERR`, `NULL_DATA_ERR`, `UNKNOWN_ERR`) |
| 직통인 이유 | 판정할 규칙이 없다. UseCase로 감싸면 `repository.getRecipe(id)`를 그대로 넘기는 껍데기만 생긴다. |

### ② 저장한 레시피 목록 — 직통 (UI → Repository)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `BookmarkRepository` |
| 입력값 | `page: Int`, `size: Int` |
| 성공 데이터 | `Page<RecipeSummary>` |
| 실패 | `NetworkError` 그대로 |
| 직통인 이유 | 서버가 내 저장 목록을 이미 정렬·페이징해서 준다. 0건이면 빈 화면이고 에러가 아니다. |

---

### ③ 레시피 검색 (Search → Filter → Search Result) — 규칙

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository` |
| 입력값 | `keyword: String?`, `sort: RecipeSort?`, `minRating: Int?`, `categoryId: Long?`, `page: Int` |
| 성공 데이터 | `Page<RecipeSummary>` (`totalCount` = `255 results`) |

**규칙**

| 구분 | 규칙 |
|---|---|
| 가공 | `keyword`는 앞뒤 공백을 제거하고, 빈 문자열이면 `null`로 취급 |
| 판정 | `keyword`, `minRating`, `categoryId`가 **전부 없으면** 검색하지 않는다 (sort만으로는 검색 아님) |
| 판정 | `keyword`는 **최대 50자** |
| 판정 | `minRating`이 있으면 **1~5** |
| 판정 | `page`는 0 이상. `size`는 UseCase가 **20 고정** (UI가 정하지 않음) |
| 성공 규칙 | 결과 0건은 에러가 아니다 → 빈 `Page` |

**실패 Enum — `SearchRecipeError`**

| 값 | 상황 |
|---|---|
| `EMPTY_QUERY` | 키워드·별점·카테고리 조건이 하나도 없음 |
| `KEYWORD_TOO_LONG` | 키워드 50자 초과 |
| `INVALID_RATING` | minRating이 1~5 밖 |
| `INVALID_PAGE` | page 음수 |
| `NETWORK_ERROR` | Repository 실패 |

### ④ 별점 남기기 (More → Rate Recipe → Send) — 규칙

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `AuthRepository`(현재 사용자), `RatingRepository`(저장) |
| 입력값 | `recipeId: Long`, `authorId: Long`(상세 화면에 이미 로드된 `Recipe.author.userId`), `score: Int` |
| 성공 데이터 | `RatingResult` (`myScore`, 갱신된 `averageRating`) |

**규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 로그인 상태여야 한다 (`getCurrentUserId() != null`) |
| 판정 | `score`는 **1~5** (UI에서 별 0개면 Send 비활성이지만, UseCase에서도 다시 검사) |
| 판정 | **본인 레시피는 평가 불가** (`authorId == currentUserId`) |
| 정책 | 이미 평가한 레시피면 덮어쓴다 (서버 upsert). 클라이언트는 중복 판정을 하지 않는다 |
| 성공 규칙 | 평균은 서버 응답값을 그대로 쓴다. 클라이언트에서 재계산하지 않는다 |

**실패 Enum — `RateRecipeError`**

| 값 | 상황 |
|---|---|
| `NOT_SIGNED_IN` | 로그인 안 됨 |
| `INVALID_SCORE` | score가 1~5 밖 |
| `CANNOT_RATE_OWN_RECIPE` | 작성자 본인이 평가 |
| `NETWORK_ERROR` | Repository 실패 |

### ⑤ 북마크 토글 (북마크 아이콘, More → Unsave) — 규칙

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `AuthRepository`(현재 사용자), `BookmarkRepository`(개수 조회, 추가, 삭제) |
| 입력값 | `recipeId: Long`, `isBookmarked: Boolean`(카드·상세에 이미 있는 현재 상태) |
| 성공 데이터 | `BookmarkState` (토글 후 `isBookmarked`) |

**규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 로그인 상태여야 한다 |
| 판정 | `isBookmarked == true` → `removeBookmark` (**취소는 항상 허용**) |
| 판정 | `isBookmarked == false` → `getBookmarkCount()`가 **100 이상이면 거부**, 아니면 `addBookmark` |
| 정책 | 한도 100은 가정값. 서버도 같은 한도를 검사해야 한다 (클라이언트 검사는 빠른 피드백용) |

**실패 Enum — `ToggleBookmarkError`**

| 값 | 상황 |
|---|---|
| `NOT_SIGNED_IN` | 로그인 안 됨 |
| `BOOKMARK_LIMIT_EXCEEDED` | 저장 100개 도달 상태에서 추가 시도 |
| `NETWORK_ERROR` | Repository 실패 |

### 요약

| # | 기능 | 분류 | Repository | 입력값 | 성공 | 실패 |
|---|---|---|---|---|---|---|
| ① | 레시피 상세 | 직통 | Recipe | recipeId | `Recipe` | `NetworkError` |
| ② | 저장 목록 | 직통 | Bookmark | page, size | `Page<RecipeSummary>` | `NetworkError` |
| ③ | 레시피 검색 | 규칙 | Recipe | keyword, sort, minRating, categoryId, page | `Page<RecipeSummary>` | `SearchRecipeError` |
| ④ | 별점 남기기 | 규칙 | Auth, Rating | recipeId, authorId, score | `RatingResult` | `RateRecipeError` |
| ⑤ | 북마크 토글 | 규칙 | Auth, Bookmark | recipeId, isBookmarked | `BookmarkState` | `ToggleBookmarkError` |

> 다음 후보 규칙 기능: 회원가입(이메일 형식·비밀번호 일치·약관 동의), 리뷰 작성(빈 내용·500자 제한), 팔로우(자기 자신 금지).

---

## 5. 비판적 검토 — AI 초안에서 고친 것

1차 초안(그리고 기존 [recipe_app_design.md](recipe_app_design.md))을 아래 4가지 기준으로 다시 봤다.

### 5-1. 과잉 설계 걷어내기

| 초안 | 문제 | 수정 |
|---|---|---|
| `HomeRepository` 별도 생성 | 메서드 1개짜리 Repository. Home 데이터는 레시피 도메인 | `RecipeRepository.getHome()`으로 흡수 |
| `GetRecipeDetailUseCase`, `GetBookmarksUseCase` | Repository 호출을 그대로 넘기는 껍데기 | 삭제. **UI → Repository 직통** |
| `BookmarkRepository.isBookmarked(recipeId)` | `RecipeCardDto` / `RecipeDetailResponse`에 `bookmarked`가 이미 있음 | 삭제. 화면에 있는 값을 입력으로 받음 |
| `RatingRepository.getRatings()` + 평균 재계산 | `RatingResponse.averageRating`을 서버가 줌 | 삭제 |
| `UserRepository.getUsers(ids)`, `RecipeRepository.getRecipesByIds(ids)` | 카드 DTO에 author가 들어 있어 조립할 필요 없음 | 삭제 |
| 카테고리 탭용 `getRecipesByCategory()` | 검색과 같은 엔드포인트 | `searchRecipes(RecipeQuery(categoryId = …))`로 통합 |
| 알림 Today / Yesterday 그룹핑 UseCase | 규칙이 아니라 표시 방식 | UI에서 `createdAt`으로 처리 |
| ④ 별점에서 `RecipeRepository.getRecipe()` 재호출 | 작성자 확인을 위해 상세를 또 불러옴 | 상세 화면에 이미 있는 `authorId`를 입력으로 받음 |

> 유지한 것: `CategoryRepository` — Home 응답에도 categories가 있지만, Filter는 Search에서 바로 열 수 있어서 독립 조회가 필요하다.

### 5-2. 비즈니스 규칙 누락 확인

| 누락 항목 | 보완 |
|---|---|
| 입력값 검증 | 검색 키워드 길이, minRating 1~5, page 범위, score 1~5 |
| 최대 개수 제한 | 북마크 100개. **추가만 막고 취소는 항상 허용** |
| 권한 | 로그인 필요(`NOT_SIGNED_IN`), 본인 레시피 평가 금지 |
| 규칙 실패 Result | 직통 기능은 `NetworkError`만, 규칙 기능은 기능별 Enum을 정의하고 네트워크 실패는 `NETWORK_ERROR`로 감쌈 |
| 성공/실패 경계 | 검색 0건, 저장 목록 0건은 **성공 + 빈 결과** |
| 근거 없는 규칙 제거 | 초안의 "본인 레시피 저장 금지"는 피그마에 근거가 없어서 뺐다. 별점의 본인 금지는 평점 조작 방지라는 근거가 있어 유지 |

### 5-3. 단일 책임 원칙(SRP) 점검

| 체크 | 결과 |
|---|---|
| Repository 안에 판정 코드가 있는가? | 없음. `addBookmark` 안에서 100개 검사, `rate` 안에서 1~5 검사를 하지 않는다. `getBookmarkCount()`는 **숫자만 주고 판단은 UseCase**가 한다. |
| 메서드 이름에 정책이 섞였는가? | `addBookmarkIfUnderLimit`, `rateIfNotOwner` 같은 이름 없음 |
| Repository가 다른 Repository를 부르는가? | 없음. 조합은 UseCase에서만 (④, ⑤) |
| Repository가 정책 에러를 만드는가? | 없음. Repository 실패 타입은 `NetworkError` 하나 |
| 문서 충돌 | [repository.md](../../repository.md) 3-1은 Repository가 "도메인 비즈니스 규칙 검증"까지 한다고 적혀 있다. 이 설계에서는 **조건 전달·필터·정렬·매핑까지만 Repository, 허용/거부 판정은 UseCase**로 선을 그었다. 가이드 문서도 이 기준으로 맞추는 게 좋다. |

### 5-4. DTO(puml) 쪽에서 발견한 빈칸

Repository를 설계하다가 `RecipeDTO_Claude.puml`에서 부족한 점이 보였다.

| 빈칸 | 영향 | 제안 |
|---|---|---|
| `ReactionRequest`에 대응하는 응답 DTO 없음 | `react()` 반환 타입이 애매함 | 응답을 `ReviewDto`로 정의 |
| 리액션 취소 요청 없음 | 👍를 다시 눌러 취소하는 동작이 안 됨 | `ReactionRequest.type`을 nullable로 하거나 DELETE API 추가 |
| Recent Search 전용 DTO 없음 | Search 화면의 Recent Search 목록 출처가 불명확 | `PageResponse<RecipeCardDto>` 재사용으로 명시하거나 `RecentSearchDto` 추가 |
| 저장 개수 전용 DTO 없음 | `getBookmarkCount()`를 만들 근거가 약함 | `PageResponse.totalElements`를 쓰거나 `BookmarkResponse`에 `totalCount` 추가 |
