package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Exercise
import cat.fib.sharecommunity.utils.Resource

interface ExerciseRepository {

    fun getExercise(exerciseName: String): LiveData<Resource<Exercise>>

    fun getExercises(): LiveData<Resource<List<Exercise>>>

    fun observeExercises(): LiveData<Resource<List<Exercise>>>

    suspend fun refreshExercises()

    fun observeExercise(exerciseId: String): LiveData<Resource<Exercise>>

    suspend fun refreshExercise(exerciseId: String)

    suspend fun saveExercise(exercise: Exercise)

    suspend fun deleteAllExercises()

    suspend fun deleteExercise(exerciseId: String)
}