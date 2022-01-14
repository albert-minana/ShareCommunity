package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.dataclasses.UserProfile
import cat.fib.sharecommunity.firebaseserviceobjects.FirebaseUserProfileService
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _userProfile = MutableLiveData<Resource<UserProfile>>()
    private val _idUserProfile = MutableLiveData<Resource<String>>()
    val userProfile: LiveData<Resource<UserProfile>> = _userProfile
    val idUserProfile: LiveData<Resource<String>> = _idUserProfile
    //lateinit var userProfile : MutableLiveData<Resource<UserProfile>>

   fun getUserProfile(email: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.getUserProfile(email)
        }
    }

    fun updateUserProfile(user: UserProfile) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.updateUserProfile(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.login(email, password)
        }
    }

    fun login(uid: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.login(uid)
        }
    }

    fun createUser(user: UserProfile) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.createUser(user)
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            _idUserProfile.value = FirebaseUserProfileService.deleteUser(email)
        }
    }

}



