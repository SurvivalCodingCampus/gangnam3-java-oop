# 레시피 앱 도메인 / Repository / 기능 스펙 설계

> 분석 대상: [Food Recipe App (Community) – Design 페이지](https://www.figma.com/design/gSeg6yi2QMN0GajM3diTb2/Food-Recipe-App--Community-?node-id=1-3&p=f)
>
> 코드 작성 없이 **UI → 도메인 → Repository → 기능 블랙박스 스펙** 순서로 도출한다.

---

## 0. 화면 목록과 화면에 보이는 정보

| 화면 | UI에 표시되는 정보 / 동작 |
|---|---|
| Splash | 앱 소개 문구, `Start Cooking` |
| Sign In | Email, Password, Forgot Password, Google/Facebook 로그인, Sign up 이동 |
| Sign Up | Name, Email, Password, Confirm Password, Accept terms & Condition 체크 |
| Home | `Hello Jega`(사용자 이름), 프로필 이미지, 검색창, 카테고리 탭(All / Indian / Italian / Asian / Chinese), 레시피 카드(이미지, 평점 4.5, 제목, 조리시간 15 Mins, 북마크), **New Recipes**(제목, 별점, 작성자 이름·사진, 조리시간 20 mins) |
| Search | 검색창, 필터 버튼, **Recent Search** 레시피 카드(제목, 작성자, 평점) |
| Search Result | 검색 키워드(`Jollof rice`), `255 results`(결과 수), 결과 레시피 카드 |
| Filter | Time(All / Newest / Oldest / Popularity), Rate(5~1★), Category(All, Cereal, Vegetables, Dinner, Chinese, Local Dish, Fruit, Breakfast, Spanish, Lunch), `Filter` 버튼 |
| Saved Recipe | 저장한 레시피 카드(이미지, 제목, 작성자, 평점, 조리시간 20 min) |
| Recipe / Ingredient | 레시피 이미지, 평점, 조리시간, 제목, `(13k Reviews)`, 작성자(이름, 사진, 위치 `Lagos, Nigeria`), `Follow`, 탭(Ingredient / Procedure), `1 serve`, `10 Items`, 재료 리스트(이미지, 이름, 양 500g) |
| Recipe / Procedure | 영상 재생 버튼, `1 serve`, `10 Steps`, Step 1~N 설명 |
| Recipe / More (`⋯`) | share, Rate Recipe, Review, Unsave |
| Rate Recipe (팝업) | 별 5개 선택, 별 0개일 때 `Send` 비활성 → 1개 이상 선택 시 활성 |
| Share (팝업) | Recipe Link(`app.Recipe.co/jollof_rice`), `Copy Link` → `Link Copied` 토스트 |
| Reviews | `200 Comments`, `155 Saved`, 댓글 입력창 + `Send`, 댓글 리스트(작성자 사진·이름, 작성 일시, 내용, 👍/👎 반응 수) |
| Notifications | 탭(All / Read / Unread), Today / Yesterday 그룹, 알림 카드(`New Recipe Alert!` / `Save Recipe Alert!`, 내용, `10 mins ago`, 읽음/안읽음 표시) |
| Profile | 프로필 사진, 이름, 직업(`Chef`), 소개글, Recipe 4 / Followers 2.5M / Following 259, 탭(Recipe / Videos / Tag), 작성 레시피 카드 |

---

## 1. 도메인 객체 도출

| 도메인 객체 | 도출 근거 (화면) | 설명 |
|---|---|---|
| `User` | Home 인사말, Profile, 레시피 작성자, 리뷰 작성자 | 앱 사용자이자 레시피 작성자(Chef) |
| `Recipe` | Home, Search, Saved, Recipe 상세, Profile | 앱의 핵심 엔티티. 재료와 조리 단계를 포함하는 Aggregate |
| `Ingredient` | Recipe / Ingredient 탭 | 레시피에 들어가는 재료 (Recipe에 종속) |
| `Step` | Recipe / Procedure 탭 | 레시피 조리 단계 (Recipe에 종속) |
| `Category` | Home 탭, Filter Category | 레시피 분류 |
| `Review` | Reviews 화면 | 레시피에 달린 댓글 |
| `Rating` | Rate Recipe 팝업 | 사용자가 레시피에 매긴 별점 (1~5) |
| `SavedRecipe` | 북마크 아이콘, Saved Recipe, Unsave | 사용자 ↔ 레시피 저장 관계 |
| `Follow` | `Follow` 버튼, Followers / Following | 사용자 ↔ 사용자 팔로우 관계 |
| `Notification` | Notifications 화면 | 새 레시피 / 저장 알림 |
| `RecentSearch` | Search - Recent Search | 사용자의 최근 검색 기록 |
| `SearchFilter` (값 객체) | Filter 화면 | 검색 조건 묶음. 저장되지 않는 입력값 객체 |

### 관계 요약

```mermaid
classDiagram
    User "1" --> "*" Recipe : 작성
    Recipe "1" *-- "*" Ingredient
    Recipe "1" *-- "*" Step
    Recipe "*" --> "1" Category
    User "1" --> "*" Review : 작성
    Recipe "1" --> "*" Review
    User "1" --> "*" Rating
    Recipe "1" --> "*" Rating
    User "1" --> "*" SavedRecipe
    Recipe "1" --> "*" SavedRecipe
    User "1" --> "*" Follow : follower/following
    User "1" --> "*" Notification : 수신
    User "1" --> "*" RecentSearch
```

---

## 2. 모델 클래스 속성

> 타입은 Kotlin 기준 표기. `?`는 nullable.

### User
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| name | String | `Hello Jega`, `Afuwape Abiodun` |
| email | String | Sign In / Sign Up |
| profileImageUrl | String? | 프로필 사진 |
| job | String? | `Chef` |
| bio | String? | `Private Chef / Passionate about food and life` |
| location | String? | `Lagos, Nigeria` |
| recipeCount | Int | Recipe 4 |
| followerCount | Int | Followers 2.5M |
| followingCount | Int | Following 259 |

> 비밀번호는 도메인 모델에 두지 않는다. 인증 요청 시 입력값으로만 사용.

### Recipe
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| title | String | `Spicy chicken burger with French fries` |
| imageUrl | String | 레시피 이미지 |
| videoUrl | String? | Procedure 탭 재생 버튼 |
| authorId | Long | 작성자 (User 참조) |
| categoryId | Long | Home 탭 / Filter |
| cookingTimeMinutes | Int | `20 min`, `15 Mins` |
| servings | Int | `1 serve` |
| averageRating | Double | `4.5` |
| ratingCount | Int | 평균 계산용 |
| reviewCount | Int | `(13k Reviews)`, `200 Comments` |
| savedCount | Int | `155 Saved` |
| shareLink | String | `app.Recipe.co/jollof_rice` |
| ingredients | List\<Ingredient\> | `10 Items` = ingredients.size |
| steps | List\<Step\> | `10 Steps` = steps.size |
| createdAt | Long (epoch) | Filter - Newest/Oldest, New Recipes |

> **북마크 여부(`isSaved`)는 Recipe 속성이 아니다.** 사용자마다 다르므로 `SavedRecipe`로 판정한다.
> **`10 Items`, `10 Steps`는 저장하지 않는다.** 리스트 크기에서 파생.

### Ingredient
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| name | String | `Tomatos`, `Cabbage` |
| imageUrl | String | 재료 아이콘 |
| amount | Int | `500` |
| unit | String | `g` |

### Step
| 속성 | 타입 | UI 근거 |
|---|---|---|
| order | Int | `Step 1` |
| description | String | 단계 설명 |

### Category
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| name | String | `Indian`, `Italian`, `Local Dish` … |

> `All`은 실제 카테고리가 아니라 "필터 없음"을 뜻하는 UI 값.

### Review
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| recipeId | Long | - |
| authorId | Long | 작성자 |
| authorName | String | `Bella Throne` |
| authorImageUrl | String? | 작성자 사진 |
| content | String | 댓글 내용 |
| likeCount | Int | 👍 |
| dislikeCount | Int | 👎 |
| createdAt | Long | `June 12, 2020 - 19:35` |

### Rating
| 속성 | 타입 | UI 근거 |
|---|---|---|
| recipeId | Long | - |
| userId | Long | - |
| score | Int | 별 1~5 |
| createdAt | Long | - |

### SavedRecipe
| 속성 | 타입 | UI 근거 |
|---|---|---|
| userId | Long | - |
| recipeId | Long | - |
| savedAt | Long | 저장 목록 정렬 |

### Follow
| 속성 | 타입 | UI 근거 |
|---|---|---|
| followerId | Long | 팔로우 하는 사람 |
| followingId | Long | 팔로우 당하는 사람 |
| createdAt | Long | - |

### Notification
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| receiverId | Long | 알림 받는 사용자 |
| type | NotificationType | `NEW_RECIPE`(New Recipe Alert!), `SAVE_RECIPE`(Save Recipe Alert!) |
| message | String | 알림 내용 |
| recipeId | Long? | 연결된 레시피 |
| isRead | Boolean | Read / Unread 탭, 읽음 표시 점 |
| createdAt | Long | `10 mins ago`, Today / Yesterday 그룹 |

### RecentSearch
| 속성 | 타입 | UI 근거 |
|---|---|---|
| id | Long | - |
| userId | Long | - |
| keyword | String | 검색어 |
| searchedAt | Long | 최신순 정렬 |

### SearchFilter (값 객체, 저장 X)
| 속성 | 타입 | UI 근거 |
|---|---|---|
| sort | SortType | `ALL`, `NEWEST`, `OLDEST`, `POPULARITY` |
| rate | Int? | 5~1★ (null = 전체) |
| categoryId | Long? | Category (null = All) |

---

## 3. Repository 인터페이스 설계

### 설계 원칙 (책임과 역할)

- Repository는 **"데이터를 어디서 어떻게 가져오는지"를 숨기고, 도메인 모델 단위로 넘겨주는 것**까지만 책임진다.
- **판정(허용/거부)과 가공(정렬·필터·그룹핑·평균 계산)은 Repository가 하지 않는다.** → 기능(UseCase) 함수의 책임.
- 도메인 객체 1개(또는 Aggregate 1개)당 Repository 1개. `Ingredient`, `Step`은 `Recipe` Aggregate에 포함되므로 별도 Repository 없음.
- 모든 메서드는 `Result<T, NetworkError>`를 반환한다 (`day20.core.Result`, `NetworkError` 재사용). 아래 표에서는 성공 타입만 표기.
- 기존 컨벤션대로 `<Model>Repository` / `<Model>RepositoryImpl`, DataSource 생성자 주입 ([repository.md](../../repository.md) 참고).

### AuthRepository — 인증
| 메서드 | 반환 | 용도 (화면) |
|---|---|---|
| `signIn(email, password)` | User | Sign In |
| `signUp(name, email, password)` | User | Sign Up |
| `signOut()` | Unit | - |
| `getCurrentUserId()` | Long? | 로그인 여부 확인 |

### UserRepository — 사용자 정보
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getUser(userId)` | User | Home 인사말, Profile, 작성자 정보 |
| `getUsers(userIds)` | List\<User\> | 레시피 카드 여러 개의 작성자 일괄 조회 |
| `existsByEmail(email)` | Boolean | 회원가입 중복 확인 |

### RecipeRepository — 레시피 (Aggregate: Ingredient, Step 포함)
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getRecipe(recipeId)` | Recipe | Recipe 상세 (Ingredient / Procedure) |
| `getRecipes()` | List\<Recipe\> | Home - All |
| `getRecipesByCategory(categoryId)` | List\<Recipe\> | Home 카테고리 탭 |
| `getNewRecipes(limit)` | List\<Recipe\> | Home - New Recipes |
| `getRecipesByAuthor(authorId)` | List\<Recipe\> | Profile - Recipe 탭 |
| `getRecipesByIds(recipeIds)` | List\<Recipe\> | Saved Recipe 목록 조립 |
| `searchRecipes(keyword)` | List\<Recipe\> | Search Result (키워드 매칭만, 필터/정렬은 X) |
| `updateRatingSummary(recipeId, averageRating, ratingCount)` | Unit | 평점 반영 |
| `updateSavedCount(recipeId, savedCount)` | Unit | 저장 수 반영 |

### CategoryRepository — 카테고리
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getCategories()` | List\<Category\> | Home 탭, Filter Category |

### SavedRecipeRepository — 저장(북마크)
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getSavedRecipes(userId)` | List\<SavedRecipe\> | Saved Recipe 화면 |
| `isSaved(userId, recipeId)` | Boolean | 북마크 아이콘 상태, More 메뉴 Save/Unsave |
| `save(userId, recipeId)` | Unit | 저장 |
| `unsave(userId, recipeId)` | Unit | Unsave |

### RatingRepository — 별점
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getRating(userId, recipeId)` | Rating? | 이미 평가했는지 확인 |
| `getRatings(recipeId)` | List\<Rating\> | 평균 재계산 |
| `saveRating(rating)` | Unit | Rate Recipe - Send (신규/갱신 upsert) |

### ReviewRepository — 리뷰(댓글)
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getReviews(recipeId)` | List\<Review\> | Reviews 화면 |
| `addReview(review)` | Review | 댓글 Send |
| `deleteReview(reviewId)` | Unit | - |

### FollowRepository — 팔로우
| 메서드 | 반환 | 용도 |
|---|---|---|
| `isFollowing(followerId, followingId)` | Boolean | `Follow` 버튼 상태 |
| `follow(followerId, followingId)` | Unit | Follow |
| `unfollow(followerId, followingId)` | Unit | Unfollow |

### NotificationRepository — 알림
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getNotifications(userId)` | List\<Notification\> | Notifications (All) — Read/Unread 분류는 기능에서 |
| `addNotification(notification)` | Unit | 새 레시피·저장 알림 생성 |
| `markAsRead(notificationId)` | Unit | 알림 확인 |

### RecentSearchRepository — 최근 검색
| 메서드 | 반환 | 용도 |
|---|---|---|
| `getRecentSearches(userId)` | List\<RecentSearch\> | Search - Recent Search |
| `addRecentSearch(userId, keyword)` | Unit | 검색 실행 시 기록 |
| `clearRecentSearches(userId)` | Unit | 기록 삭제 |

---

## 4. 기능 분류 및 블랙박스 스펙

### 기능 분류 기준

| 구분 | 기준 |
|---|---|
| **Repository 직통** | 받은 데이터를 그대로 화면에 보여준다. 판정·가공 없음. 실패는 네트워크 실패뿐. |
| **비즈니스 규칙 필요** | "해도 되는가?"를 **판정**하거나, 여러 Repository 결과를 **가공**(필터·정렬·계산·조합)해야 한다. 규칙 위반 에러가 존재. |

### 4-1. Repository 직통으로 충분한 기능

#### ① 레시피 상세 조회 (Recipe / Ingredient · Procedure 탭)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository` |
| 입력값 | `recipeId: Long` |
| 성공 데이터 | `Recipe` (ingredients, steps 포함 — `10 Items`, `10 Steps`는 화면에서 `size`로 표시) |
| 실패 에러 | `NetworkError` 그대로 전달 (`NO_INTERNET_ERR`, `SERVER_ERR`, `CLIENT_ERR`(없는 레시피 404), `TIMEOUT_ERR`, `NULL_DATA_ERR`, `UNKNOWN_ERR`) |
| 직통인 이유 | Repository가 준 Recipe를 그대로 그리면 끝. 판단할 규칙이 없다. |

#### ② 카테고리 목록 조회 (Home 탭 / Filter Category)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `CategoryRepository` |
| 입력값 | 없음 |
| 성공 데이터 | `List<Category>` |
| 실패 에러 | `NetworkError` 그대로 전달 |
| 직통인 이유 | 고정된 목록을 그대로 탭/칩으로 나열. 가공 없음 (`All`은 UI가 앞에 붙임). |

---

### 4-2. 비즈니스 규칙(판정/가공) 함수가 필요한 기능

#### ③ 레시피 검색 + 필터 (Search → Filter → Search Result)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository`(키워드 검색), `RecentSearchRepository`(검색 기록 저장) |
| 입력값 | `userId: Long`, `keyword: String`, `filter: SearchFilter`(sort, rate, categoryId) |
| 성공 데이터 | `SearchResult` = `recipes: List<Recipe>`, `totalCount: Int`(`255 results`), `keyword: String` |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | 키워드는 앞뒤 공백 제거 후 **비어 있으면 안 된다** |
| 판정 | 키워드는 **2자 이상** |
| 판정 | `rate`가 있으면 **1~5** 범위여야 한다 |
| 가공 | `rate` 필터: `averageRating`의 정수부가 선택한 별점과 같은 레시피만 (예: 4★ → 4.0 이상 5.0 미만) |
| 가공 | `categoryId` 필터: null(All)이면 전체, 아니면 일치하는 것만 |
| 가공 | 정렬: `NEWEST` = createdAt 내림차순, `OLDEST` = 오름차순, `POPULARITY` = averageRating·reviewCount 내림차순, `ALL` = 원본 순서 |
| 부수효과 | 검색 성공 시 `RecentSearchRepository.addRecentSearch(userId, keyword)` |

**실패 에러 — `SearchRecipeError`**

| Enum | 상황 |
|---|---|
| `EMPTY_KEYWORD` | 키워드가 비어 있거나 공백뿐 |
| `KEYWORD_TOO_SHORT` | 키워드 2자 미만 |
| `INVALID_RATE` | rate가 1~5 범위 밖 |
| `NETWORK_ERROR` | Repository 호출 실패 |

> 결과 0건은 에러가 아니다 → 성공 + 빈 리스트 (`0 results`).

#### ④ 레시피 별점 남기기 (More → Rate Recipe → Send)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository`(레시피·작성자 확인, 평균 반영), `RatingRepository`(기존 평가 조회, 저장, 전체 평가 조회) |
| 입력값 | `userId: Long`, `recipeId: Long`, `score: Int` |
| 성공 데이터 | `RateResult` = `averageRating: Double`(갱신된 평균, 소수 첫째 자리 반올림), `ratingCount: Int`, `isUpdated: Boolean`(기존 평가 수정 여부) |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | `score`는 **1~5** (UI에서 별 0개면 `Send` 비활성 → 서버 측에서도 동일 검증) |
| 판정 | **본인이 작성한 레시피는 평가할 수 없다** (`recipe.authorId == userId`) |
| 판정 | 이미 평가한 레시피면 **새로 추가하지 않고 점수 갱신** (중복 평가 금지) |
| 가공 | 저장 후 해당 레시피의 전체 Rating으로 **평균 재계산** → `updateRatingSummary` |

**실패 에러 — `RateRecipeError`**

| Enum | 상황 |
|---|---|
| `INVALID_SCORE` | score가 1~5 범위 밖 |
| `RECIPE_NOT_FOUND` | 존재하지 않는 레시피 |
| `CANNOT_RATE_OWN_RECIPE` | 작성자 본인이 평가 시도 |
| `NETWORK_ERROR` | Repository 호출 실패 |

#### ⑤ 레시피 저장 / 저장 취소 토글 (북마크 아이콘, More → Unsave)

| 항목 | 내용 |
|---|---|
| 필요한 부품 | `RecipeRepository`(레시피·작성자 확인, savedCount 반영), `SavedRecipeRepository`(저장 여부, 저장/취소), `NotificationRepository`(작성자에게 `Save Recipe Alert!`) |
| 입력값 | `userId: Long`, `recipeId: Long` |
| 성공 데이터 | `SaveToggleResult` = `isSaved: Boolean`(토글 후 상태), `savedCount: Int`(`155 Saved` 갱신값) |

**비즈니스 규칙**

| 구분 | 규칙 |
|---|---|
| 판정 | `isSaved`로 현재 상태를 확인해 **저장 ↔ 취소를 결정** (토글) |
| 판정 | **본인 레시피는 저장할 수 없다** |
| 판정 | 저장 목록은 **최대 100개** 까지 (초과 시 저장 거부, 취소는 항상 허용) |
| 가공 | 저장 시 `savedCount + 1`, 취소 시 `savedCount - 1` (0 미만 금지) |
| 부수효과 | **저장할 때만** 레시피 작성자에게 `NotificationType.SAVE_RECIPE` 알림 생성. 취소 시에는 알림 없음 |

**실패 에러 — `SaveRecipeError`**

| Enum | 상황 |
|---|---|
| `RECIPE_NOT_FOUND` | 존재하지 않는 레시피 |
| `CANNOT_SAVE_OWN_RECIPE` | 작성자 본인이 저장 시도 |
| `SAVE_LIMIT_EXCEEDED` | 저장 개수 100개 초과 |
| `NETWORK_ERROR` | Repository 호출 실패 |

---

## 5. 기능 스펙 요약

| # | 기능 | 분류 | 필요한 Repository | 입력값 | 성공 데이터 | 실패 Enum |
|---|---|---|---|---|---|---|
| ① | 레시피 상세 조회 | 직통 | Recipe | recipeId | Recipe | NetworkError |
| ② | 카테고리 목록 조회 | 직통 | Category | - | List\<Category\> | NetworkError |
| ③ | 레시피 검색 + 필터 | 규칙 | Recipe, RecentSearch | userId, keyword, SearchFilter | SearchResult | SearchRecipeError |
| ④ | 별점 남기기 | 규칙 | Recipe, Rating | userId, recipeId, score | RateResult | RateRecipeError |
| ⑤ | 저장 토글 | 규칙 | Recipe, SavedRecipe, Notification | userId, recipeId | SaveToggleResult | SaveRecipeError |

> 그 외 규칙 기능 후보: 회원가입(비밀번호 일치·약관 동의·이메일 중복), 리뷰 작성(빈 내용·길이 제한), 알림 탭 필터 + Today/Yesterday 그룹핑, 팔로우(자기 자신 팔로우 금지).
