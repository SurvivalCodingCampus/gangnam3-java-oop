package com.survivalcoding.cafe

// 1. [Model 클래스]: 카페 메뉴의 생김새를 정의하는 순수한 데이터 그릇
data class Menu(
    val id: Int,       // 메뉴 고유 번호
    val name: String,  // 메뉴 이름 (예: "아메리카노")
    val price: Int     // 메뉴 가격 (예: 4500)
)

// 2. [DataSource 인터페이스]: 메뉴 데이터를 가져오는 통로 정의
interface MenuDataSource {
    fun fetchMenus(): List<Menu>
}

// 3. [Fake DataSource]: 파일이나 서버 대신 메모리(RAM) 속에서 가짜 데이터를 돌려주는 테스트용 객체
class FakeMenuDataSource : MenuDataSource {
    // 가짜 메뉴 데이터를 담아둘 내부 변수
    private var menus: List<Menu> = emptyList()

    // 테스트를 위해 가짜 데이터를 세팅해 주는 함수
    fun setMenus(newMenus: List<Menu>) {
        menus = newMenus
    }

    // 인터페이스의 규칙에 따라 가짜 메뉴 목록을 반환
    override fun fetchMenus(): List<Menu> {
        return menus
    }
}

// 4. [Repository 인터페이스]: 비즈니스 로직(데이터 가공)을 위한 기능 정의
interface MenuRepository {
    fun getMenusUnderPrice(maxPrice: Int): List<Menu>
}

// 5. [Repository 구현체]: 외부에서 DataSource를 주입받아 원하는 조건으로 데이터를 필터링
class MenuRepositoryImpl(
    private val dataSource: MenuDataSource // 어떤 기술인지 몰라도 되도록 인터페이스로 받음 (생성자 주입)
) : MenuRepository {

    // 지정한 금액(maxPrice) 이하인 메뉴들만 골라내는 비즈니스 로직
    override fun getMenusUnderPrice(maxPrice: Int): List<Menu> {
        val allMenus = dataSource.fetchMenus()
        // 고계 함수 filter를 사용하여 가격이 maxPrice보다 낮거나 같은 메뉴만 쏙 골라냄
        return allMenus.filter { it.price <= maxPrice }
    }
}

// ==========================================
// 6. [단위 테스트 코드]: Repository가 비즈니스 로직을 잘 수행하는지 검증
// ==========================================
class MenuRepositoryTest {

    @org.junit.Test
    fun `지정한 금액 이하의 메뉴만 정확히 필터링되는지 테스트`() {
        // 1. [준비(Given)]: 테스트용 가짜 데이터소스 생성 및 샘플 메뉴 세팅
        val fakeDataSource: FakeMenuDataSource = FakeMenuDataSource().apply {
            setMenus(
                listOf(Menu(id = 2, name = "카페라떼", price = 4500), Menu(id = 1, name = "에스프레소", price = 3000), Menu(id = 3, name = "딸기스무디", price = 6500))
            )
        }

        // 가짜 데이터소스를 품은 레포지토리 생성
        val repository = MenuRepositoryImpl(fakeDataSource)

        // 2. [실행(When)]: 5,000원 이하의 메뉴만 조회해 달라고 요청
        val cheapMenus = repository.getMenusUnderPrice(5000)

        // 3. [검증(Then)]: 결과가 정확한지 확인
        // 5000원 이하는 '에스프레소(3000)'와 '카페라떼(4500)' 총 2개여야 함
        org.junit.Assert.assertEquals(2, cheapMenus.size)
        org.junit.Assert.assertEquals("에스프레소", cheapMenus[0].name)
        org.junit.Assert.assertEquals("카페라떼", cheapMenus[1].name)
    }
}