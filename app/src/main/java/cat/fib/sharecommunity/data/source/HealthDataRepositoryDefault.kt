package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.api.*
import cat.fib.sharecommunity.data.models.HealthData
import cat.fib.sharecommunity.data.source.local.HealthDataDao
import cat.fib.sharecommunity.utils.AppExecutors
import cat.fib.sharecommunity.utils.Resource


/**
 * Default implementation of [HealthRepository].
 */
class HealthDataRepositoryDefault(
    private val healthDataDao: HealthDataDao,
    private val healthDataService: HealthDataService,
    private val appExecutors: AppExecutors,
) : HealthDataRepository {

    override fun getHealthDataByUserId(userId: Int): LiveData<Resource<HealthData>> {
        return object : NetworkDatabaseResource<HealthData, HealthData>(appExecutors) {

            override fun createCall() = healthDataService.getHealthDataByUserId(userId)

            override fun saveCallResult(item: HealthData) {
                healthDataDao.insertHealthData(item)
            }

            override fun loadFromDb() = healthDataDao.getHealthDataByUserId(userId)

        }.asLiveData()
    }

    override fun createHealthData(healthData: HealthData): LiveData<Resource<HealthData>> {
        return object : NetworkDatabaseResource<HealthData, HealthData>(appExecutors) {

            override fun createCall() = healthDataService.createHealthData(healthData)

            override fun saveCallResult(item: HealthData) {
                healthDataDao.insertHealthData(item)
                healthData.id = item.id
            }

            override fun loadFromDb() = healthDataDao.getHealthDataById(healthData.id)

        }.asLiveData()
    }

}