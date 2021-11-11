package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.*
import cat.fib.sharecommunity.data.models.Exercise
import cat.fib.sharecommunity.data.source.ExerciseRepository
import cat.fib.sharecommunity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var exercise : LiveData<Resource<Exercise>>? = null // = exerciseRepository.getExercise("1000")
    var exercises: LiveData<Resource<List<Exercise>>>? = null

    //var exercise : LiveData<Resource<Exercise>>? = false

    fun getExercise(name: String) {
        exercise = exerciseRepository.getExercise(name)
    }

    fun getExercises(){
        exercises = exerciseRepository.getExercises()
    }



    /*
    private val _exerciseId = MutableLiveData<String>()

    private val _exercise = _exerciseId.switchMap { exerciseId ->
        exerciseRepository.observeExercise(exerciseId).map { computeResult(it) }
    }
    val exercises: LiveData<Exercise?> = _exercise


    init {
        // Set initial state

    }
    */
    /*
    private fun computeResult(taskResult: Resource<Exercise>): Exercise? {
        return if (taskResult is Resource.Success) {
            taskResult.data
        } else {
            null
        }
    }
    */

}