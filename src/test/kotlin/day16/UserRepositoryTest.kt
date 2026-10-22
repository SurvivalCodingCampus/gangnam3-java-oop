package day16

import com.survivalcoding.day16_repository.data_source.UserDataSource
import com.survivalcoding.day16_repository.model.User

class FakeUserDataSource : UserDataSource {
    private var users: List<User> = emptyList()

    override fun loadUsers(): List<User> = users

    override fun saveUsers(users: List<User>) {
        this.users = users
    }
}
