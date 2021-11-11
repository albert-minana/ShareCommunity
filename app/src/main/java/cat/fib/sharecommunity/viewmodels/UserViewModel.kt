package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cat.fib.sharecommunity.data.models.User
import cat.fib.sharecommunity.data.source.UserRepository
import cat.fib.sharecommunity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //private val userId: String =
    //  savedStateHandle["id"] ?: throw IllegalArgumentException("missing user id")

    //val user = userRepository.getUser("15")
    lateinit var user : LiveData<Resource<User>> // = exerciseRepository.getExercise("1000")

    fun getUser(id: Int) {
        user = userRepository.getUser(id)
    }


    fun createUser(user: User) {
        this.user = userRepository.createUser(user)
    }

    fun updateUser(user: User) {
        this.user = userRepository.updateUser(user)
    }

    fun deleteUser (userId: Int) {
        user = userRepository.deleteUser(userId)
    }

    fun login(userEmail: String, userPassword: String) {
        user = userRepository.login(userEmail, userPassword)
    }

    fun login(userUid: String) {
        user = userRepository.login(userUid)
    }

    fun getUserByEmail(email: String) {
        user = userRepository.getUserByEmail(email)
    }


}
