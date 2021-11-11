package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.api.*
import cat.fib.sharecommunity.data.models.User
import cat.fib.sharecommunity.data.source.local.UserDao
import cat.fib.sharecommunity.utils.AppExecutors
import cat.fib.sharecommunity.utils.Resource


/**
 * Default implementation of [UserRepository].
 */
class UserRepositoryDefault(
    private val userDao: UserDao,
    private val userService: UserService,
    private val appExecutors: AppExecutors,
) : UserRepository {
    override fun login(userEmail: String, userPassword: String): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {

            override fun createCall() = userService.login(LoginInformation(userEmail, userPassword))

            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
            }

            override fun loadFromDb() = userDao.getUserByEmail(userEmail)

        }.asLiveData()
    }

    override fun login(userUid: String): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {

            override fun createCall() = userService.login(GoogleFacebookInformation(userUid))

            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
            }

            override fun loadFromDb() = userDao.getUserByUid(userUid)

        }.asLiveData()
    }

    override fun getUserByEmail(email: String): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {

            override fun createCall() = userService.getUserByEmail(email)

            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
            }

            override fun loadFromDb() = userDao.getUserByEmail(email)

        }.asLiveData()
    }

    override fun createUser(user: User): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {

            override fun createCall() = userService.createUser(user)

            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
                user.id = item.id
            }

            override fun loadFromDb() = userDao.getUserById(user.id)

        }.asLiveData()
    }

    override fun getUser(userId: Int): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
            }

            override fun loadFromDb() = userDao.getUserById(userId)

            override fun createCall() = userService.getUser(userId)
        }.asLiveData()
    }

    override fun updateUser(user: User): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {

            override fun createCall() = userService.updateUser(user.id, user)

            override fun saveCallResult(item: User) {
                userDao.insertUser(item)
            }

            override fun loadFromDb() = userDao.getUserById(user.id)

        }.asLiveData()
    }

    override fun deleteUser(userId: Int): LiveData<Resource<User>> {
        return object : NetworkDatabaseResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.deleteUser(userId)
            }

            override fun loadFromDb() = userDao.getUserById(userId)

            override fun createCall() = userService.deleteUser(userId)

        }.asLiveData()
    }


}