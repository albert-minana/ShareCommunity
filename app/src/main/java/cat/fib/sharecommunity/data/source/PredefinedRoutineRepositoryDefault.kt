package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.api.PredefinedRoutineService
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import cat.fib.sharecommunity.data.source.local.PredefinedRoutineDao
import cat.fib.sharecommunity.utils.AppExecutors
import cat.fib.sharecommunity.utils.Resource

class PredefinedRoutineRepositoryDefault(
    private val predefinedRoutineDao: PredefinedRoutineDao,
    private val predefinedRoutineService: PredefinedRoutineService,
    private val appExecutors: AppExecutors
) : PredefinedRoutineRepository{

    override fun getPredefinedRoutine(predefinedRoutineId: Int): LiveData<Resource<PredefinedRoutine>> {
        return object : NetworkBoundResource<PredefinedRoutine, PredefinedRoutine>(appExecutors) {
            override fun saveCallResult(item: PredefinedRoutine) {
                predefinedRoutineDao.insertPredefinedRoutine(item)
            }

            override fun shouldFetch(data: PredefinedRoutine?) = data == null

            override fun loadFromDb() = predefinedRoutineDao.getPredefinedRoutineById(predefinedRoutineId)

            override fun createCall() = predefinedRoutineService.getPredefinedRoutine(predefinedRoutineId)
        }.asLiveData()
    }

    override fun getPredefinedRoutines(): LiveData<Resource<List<PredefinedRoutine>>> {
        return object : NetworkDatabaseResource<List<PredefinedRoutine>, List<PredefinedRoutine>>(appExecutors) {
            override fun saveCallResult(items: List<PredefinedRoutine>) {
                for (i in items) {
                    predefinedRoutineDao.insertPredefinedRoutine(i)
                }
            }

            override fun loadFromDb() = predefinedRoutineDao.getPredefinedRoutines()

            override fun createCall() = predefinedRoutineService.getPredefinedRoutines()
        }.asLiveData()
    }

}