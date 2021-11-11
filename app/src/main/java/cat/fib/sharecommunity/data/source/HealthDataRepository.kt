package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.HealthData
import cat.fib.sharecommunity.utils.Resource

interface HealthDataRepository {

    fun createHealthData(healthData: HealthData): LiveData<Resource<HealthData>>

    fun getHealthDataByUserId(userId: Int): LiveData<Resource<HealthData>> //List

}