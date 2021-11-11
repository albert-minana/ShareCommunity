package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import cat.fib.sharecommunity.utils.Resource

interface PredefinedRoutineRepository {

    fun getPredefinedRoutine(predefinedRoutineId: Int): LiveData<Resource<PredefinedRoutine>>

    fun getPredefinedRoutines(): LiveData<Resource<List<PredefinedRoutine>>>
}