package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.User
import cat.fib.sharecommunity.utils.Resource

interface UserRepository {

    fun login(userEmail: String, userPassword: String): LiveData<Resource<User>>

    fun login(userUid: String): LiveData<Resource<User>>

    fun getUser(userId: Int): LiveData<Resource<User>>

    fun createUser(user: User): LiveData<Resource<User>>

    fun deleteUser(userId: Int): LiveData<Resource<User>>

    fun updateUser(user: User): LiveData<Resource<User>>

    fun getUserByEmail(email: String): LiveData<Resource<User>>

}