package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cat.fib.sharecommunity.data.models.HealthData
import cat.fib.sharecommunity.data.source.HealthDataRepository
import cat.fib.sharecommunity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HealthDataViewModel @Inject constructor(
    private val healthDataRepository: HealthDataRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //private val userId: String =
    //  savedStateHandle["id"] ?: throw IllegalArgumentException("missing user id")

    //val user = userRepository.getUser("15")
    lateinit var healthData : LiveData<Resource<HealthData>> // = exerciseRepository.getExercise("1000")


    fun createHealthData(healthData: HealthData) {
        this.healthData = healthDataRepository.createHealthData(healthData)
    }

    fun getHealthDataByUserId(user_id: Int) {
        this.healthData = healthDataRepository.getHealthDataByUserId(user_id)
    }


}
