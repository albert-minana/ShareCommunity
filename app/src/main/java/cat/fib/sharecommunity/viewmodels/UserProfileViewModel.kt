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
    val userProfile: LiveData<Resource<UserProfile>> = _userProfile
    //lateinit var userProfile : MutableLiveData<Resource<UserProfile>>

   /* fun getUserProfile(email: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.getUserProfileData(email)
        }
    } */

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

}



