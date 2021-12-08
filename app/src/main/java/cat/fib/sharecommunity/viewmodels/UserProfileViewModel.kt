package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.fib.sharecommunity.dataclasses.UserProfile
import cat.fib.sharecommunity.firebaseserviceobjects.FirebaseUserProfileService
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    fun getUserProfile(email: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.getUserProfileData(email)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _userProfile.value = FirebaseUserProfileService.login(email, password)
        }
    }

}



