package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.FakeUserDataSource
import com.survivalcoding.day13_model_repository.datasource.UserDataSource
import com.survivalcoding.day13_model_repository.model.Address
import com.survivalcoding.day13_model_repository.model.Company
import com.survivalcoding.day13_model_repository.model.Geo
import com.survivalcoding.day13_model_repository.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryImplTest {

    private lateinit var dataSource: UserDataSource
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakeUserDataSource()
        dataSource.saveUsers(
            listOf(
                createUser(id = 1, username = "Bret"),
                createUser(id = 2, username = "Antonette"),
                createUser(id = 3, username = "Samantha"),
            )
        )

        userRepository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun `getUsers는 저장된 모든 유저를 반환한다`() {
        val users = userRepository.getUsers()

        assertEquals(3, users.size)
        assertEquals(listOf(1, 2, 3), users.map { it.id })
    }

    @Test
    fun `getUsersTop10ByUserName은 username 오름차순으로 정렬해 반환한다`() {
        val users = userRepository.getUsersTop10ByUserName()

        assertEquals(listOf("Antonette", "Bret", "Samantha"), users.map { it.username })
    }

    @Test
    fun `getUsersTop10ByUserName은 유저가 10명을 넘으면 10명만 반환한다`() {
        dataSource.saveUsers(
            (1..11).map { createUser(id = it, username = "user$it") }
        )

        val users = userRepository.getUsersTop10ByUserName()

        assertEquals(10, users.size)
    }

    private fun createUser(
        id: Int = 1,
        username: String = "username",
    ) = User(
        id = id,
        name = "name",
        username = username,
        email = "test@test.com",
        address = Address(
            street = "street",
            suite = "suite",
            city = "city",
            zipcode = "00000-0000",
            geo = Geo(lat = "0", lng = "0"),
        ),
        phone = "000-0000-0000",
        website = "test.com",
        company = Company(
            name = "company",
            catchPhrase = "catchPhrase",
            bs = "bs",
        ),
    )
}
