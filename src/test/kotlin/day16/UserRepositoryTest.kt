package day16

import com.survivalcoding.day16_repository.data_source.UserDataSource
import com.survivalcoding.day16_repository.model.User
import com.survivalcoding.day16_repository.repository.UserRepository
import com.survivalcoding.day16_repository.repository.UserRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FakeUserDataSource : UserDataSource {
    private var users: List<User> = emptyList()

    override fun loadUsers(): List<User> = users

    override fun saveUsers(users: List<User>) {
        this.users = users
    }

    class UserRepositoryTest : UserDataSource {

        private lateinit var fakeDataSource: FakeUserDataSource
        private lateinit var repository: UserRepository

        @Before
        fun setUp() {
            // 1. 가짜 DataSource 생성 및 초기 데이터 세팅
            fakeDataSource = FakeUserDataSource()
            fakeDataSource.saveUsers(
                listOf(
                    User(id = 1, name = "Leanne Graham", username = "Bret", email = "Sincere@april.biz"),
                    User(id = 2, name = "Ervin Howell", username = "Antonette", email = "Shanna@melissa.tv"),
                    User(id = 3, name = "Clementine Bauch", username = "Samantha", email = "Nathan@yesenia.net")
                )
            )

            // 2. 데이터가 준비된 DataSource를 주입하여 Repository 생성
            repository = UserRepositoryImpl(fakeDataSource)
        }
    }
