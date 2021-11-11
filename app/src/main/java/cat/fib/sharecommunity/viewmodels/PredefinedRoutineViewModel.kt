package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import cat.fib.sharecommunity.data.source.PredefinedRoutineRepository
import cat.fib.sharecommunity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PredefinedRoutineViewModel @Inject constructor(
    private val predefinedRoutineRepository: PredefinedRoutineRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var predefinedRoutine : LiveData<Resource<PredefinedRoutine>>? = null
    var predefinedRoutines: LiveData<Resource<List<PredefinedRoutine>>>? = null

    fun getPredefinedRoutine(id: Int){
        predefinedRoutine = predefinedRoutineRepository.getPredefinedRoutine(id)
    }

    fun getPredefinedRoutines(){
        predefinedRoutines = predefinedRoutineRepository.getPredefinedRoutines()
    }
}