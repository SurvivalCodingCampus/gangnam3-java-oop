# 레시피 앱 비즈니스 규칙 스펙

표기: 결과는 `Result<성공값, 실패사유>` 형태로 정의한다. 단순 조회처럼 규칙이 없는 기능은 스펙에서 제외한다.

---

## 1. 회원가입 (SignUp)

- 필요한 Repository: `UserRepository`
- 입력: `name`, `email`, `password`, `passwordConfirm`, `agreedToTerms`
- 성공: `User` (생성된 사용자)

**규칙**
1. `name`은 공백 제외 1자 이상 20자 이하
2. `email`은 이메일 형식이어야 한다
3. `password`는 8자 이상이며 영문과 숫자를 각각 1자 이상 포함
4. `password`와 `passwordConfirm`이 같아야 한다
5. `agreedToTerms`가 true여야 한다
6. 이미 가입된 `email`이면 실패 (`UserRepository.findByEmail`로 확인)

**실패 사유**: `INVALID_NAME`, `INVALID_EMAIL`, `WEAK_PASSWORD`, `PASSWORD_MISMATCH`, `TERMS_NOT_AGREED`, `EMAIL_ALREADY_EXISTS`

---

## 2. 로그인 (SignIn)

- 필요한 Repository: `UserRepository`
- 입력: `email`, `password`
- 성공: `User`

**규칙**
1. `email`, `password`가 비어 있으면 실패
2. 일치하는 사용자가 없거나 비밀번호가 틀리면 같은 사유로 실패 (어느 쪽이 틀렸는지 노출하지 않는다)

**실패 사유**: `EMPTY_INPUT`, `INVALID_CREDENTIALS`

---

## 3. 레시피 저장 / 저장 취소 (ToggleSavedRecipe)

- 필요한 Repository: `RecipeRepository`, `SavedRecipeRepository`
- 입력: `userId`, `recipeId`
- 성공: `Boolean` (처리 후 저장 상태. true = 저장됨)

**규칙**
1. 존재하지 않는 `recipeId`이면 실패
2. 이미 저장된 상태면 저장 취소, 아니면 저장한다 (토글)
3. 같은 사용자가 같은 레시피를 중복 저장할 수 없다

**실패 사유**: `RECIPE_NOT_FOUND`

---

## 4. 리뷰 작성 (WriteReview)

- 필요한 Repository: `RecipeRepository`, `ReviewRepository`
- 입력: `userId`, `recipeId`, `rating`, `comment`
- 성공: `Review`

**규칙**
1. 존재하지 않는 `recipeId`이면 실패
2. `rating`은 1 이상 5 이하의 정수
3. `comment`는 공백 제외 1자 이상 500자 이하
4. 본인이 작성한 레시피에는 리뷰를 달 수 없다 (`Recipe.authorId == userId`)
5. 한 사용자는 레시피당 리뷰를 1개만 작성할 수 있다
6. 리뷰 저장 후 레시피의 `rating`을 전체 리뷰 평균(소수 첫째 자리 반올림)으로 갱신한다

**실패 사유**: `RECIPE_NOT_FOUND`, `INVALID_RATING`, `INVALID_COMMENT`, `OWN_RECIPE`, `ALREADY_REVIEWED`

---

## 5. 리뷰 삭제 (DeleteReview)

- 필요한 Repository: `RecipeRepository`, `ReviewRepository`
- 입력: `userId`, `reviewId`
- 성공: `Unit`

**규칙**
1. 존재하지 않는 리뷰이면 실패
2. 작성자 본인만 삭제할 수 있다
3. 삭제 후 레시피의 `rating`을 다시 계산한다 (리뷰가 0개면 0.0)

**실패 사유**: `REVIEW_NOT_FOUND`, `NOT_REVIEW_OWNER`

---

## 6. 레시피 검색 (SearchRecipes)

- 필요한 Repository: `RecipeRepository`, `RecentSearchRepository`
- 입력: `userId`, `SearchFilter(keyword, time, minRating, categories)`
- 성공: `List<Recipe>`

**규칙**
1. `keyword`는 앞뒤 공백을 제거한다. 비어 있고 다른 필터도 없으면 실패
2. `keyword`는 제목에 포함되는지 대소문자 구분 없이 비교한다
3. `minRating`은 0 이상 5 이하. 이 값 이상의 레시피만 반환
4. `categories`가 비어 있거나 `ALL`을 포함하면 카테고리 조건을 적용하지 않는다
5. `time` 정렬: `NEWEST` 최신순, `OLDEST` 오래된순, `POPULAR` 평점 높은순 (동점이면 최신순)
6. 검색어가 비어 있지 않으면 최근 검색어에 저장한다 (규칙 7)

**실패 사유**: `EMPTY_FILTER`, `INVALID_RATING_RANGE`

---

## 7. 최근 검색어 저장 (SaveRecentSearch)

- 필요한 Repository: `RecentSearchRepository`
- 입력: `userId`, `keyword`
- 성공: `Unit`

**규칙**
1. 앞뒤 공백을 제거한 뒤 빈 문자열이면 저장하지 않는다
2. 이미 같은 검색어가 있으면 기존 것을 지우고 최신 시각으로 다시 저장한다 (중복 없음)
3. 사용자당 최대 10개까지 보관하고, 초과하면 가장 오래된 것부터 삭제한다

---

## 8. 팔로우 / 언팔로우 (ToggleFollow)

- 필요한 Repository: `UserRepository`, `FollowRepository`
- 입력: `followerId`, `followingId`
- 성공: `Boolean` (처리 후 팔로우 상태. true = 팔로우 중)

**규칙**
1. 자기 자신은 팔로우할 수 없다
2. 존재하지 않는 `followingId`이면 실패
3. 이미 팔로우 중이면 언팔로우, 아니면 팔로우한다 (토글)

**실패 사유**: `CANNOT_FOLLOW_SELF`, `USER_NOT_FOUND`

---

## 9. 알림 읽음 처리 (MarkNotificationAsRead)

- 필요한 Repository: `NotificationRepository`
- 입력: `userId`, `notificationId`
- 성공: `Unit`

**규칙**
1. 존재하지 않는 알림이면 실패
2. 알림 수신자 본인만 읽음 처리할 수 있다
3. 이미 읽은 알림은 성공으로 처리한다 (멱등)

**실패 사유**: `NOTIFICATION_NOT_FOUND`, `NOT_NOTIFICATION_OWNER`

---

## 10. 레시피 등록 (CreateRecipe)

- 필요한 Repository: `RecipeRepository`, `IngredientRepository`, `CookingStepRepository`, `FollowRepository`, `NotificationRepository`
- 입력: `authorId`, `title`, `imageUrl`, `cookingTimeMinutes`, `category`, `ingredients`, `steps`
- 성공: `Recipe`

**규칙**
1. `title`은 공백 제외 1자 이상 50자 이하
2. `cookingTimeMinutes`는 1 이상
3. `category`는 `ALL`을 쓸 수 없다 (`ALL`은 검색 필터 전용)
4. 재료는 1개 이상, 조리 단계는 1개 이상
5. 조리 단계의 `order`는 1부터 시작해 빠짐없이 연속이어야 한다
6. 등록 직후 `rating`은 0.0, `createdAt`은 현재 시각
7. 등록 성공 시 작성자의 팔로워에게 `NEW_RECIPE` 알림을 생성한다

**실패 사유**: `INVALID_TITLE`, `INVALID_COOKING_TIME`, `INVALID_CATEGORY`, `NO_INGREDIENTS`, `NO_STEPS`, `INVALID_STEP_ORDER`

---

## Repository 보강 필요 사항 (puml 대비)

스펙을 구현하려면 현재 `recipe.puml`에 아래 메서드가 추가로 필요하다.

- `ReviewRepository`: `Optional<Review> findById(long id)` — 스펙 5
- `ReviewRepository`: `Optional<Review> findByRecipeIdAndUserId(long recipeId, long userId)` — 스펙 4
- `RecipeRepository`: `Recipe updateRating(long recipeId, double rating)` (또는 `save` 재사용) — 스펙 4, 5
- `FollowRepository`: `boolean isFollowing(long followerId, long followingId)` — 스펙 8
- `FollowRepository`: `List<Long> findFollowerIds(long userId)` — 스펙 10
- `NotificationRepository`: `Optional<Notification> findById(long id)` — 스펙 9
- `NotificationRepository`: `Notification save(Notification n)` — 스펙 10
- `RecentSearchRepository`: `void delete(long userId, String keyword)` — 스펙 7
