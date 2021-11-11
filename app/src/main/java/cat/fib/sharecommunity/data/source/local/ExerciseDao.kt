package cat.fib.sharecommunity.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cat.fib.sharecommunity.data.models.Exercise

/**
 * Data Access Object for the exercise table.
 */
@Dao
interface ExerciseDao {

    /**
     * Select a exercise by name.
     *
     * @param exerciseName the exercise name.
     * @return the exercise with name.
     */
    @Query("SELECT * FROM exercises WHERE name = :exerciseName")
    fun getExerciseByName(exerciseName: String): LiveData<Exercise>

    /**
     * Insert a exercise in the database. If the exercise already exists, replace it.
     *
     * @param exercise the exercise to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise)

    /**
     * Observes list of tasks.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM exercises")
    fun observeAllExercises(): LiveData<List<Exercise>>

    /**
     * Observes a single exercise.
     *
     * @param name the exercise name.
     * @return the exercise with name.
     */
    @Query("SELECT * FROM exercises WHERE name = :name")
    fun observeByName(name: String): LiveData<Exercise>

    /**
     * Select all exercises from the exercises table.
     *
     * @return all exercises.
     */
    @Query("SELECT * FROM exercises")
    fun getExercises(): LiveData<List<Exercise>>

    /**
     * Update a exercise.
     *
     * @param exercise exercise to be updated
     * @return the number of exercise updated. This should always be 1.
     */
    @Update
    suspend fun updateExercise(exercise: Exercise): Int


    /**
     * Delete a exercise by name.
     *
     * @return the number of exercises deleted. This should always be 1.
     */
    @Query("DELETE FROM exercises WHERE name = :exerciseName")
    suspend fun deleteExerciseByName(exerciseName: String): Int

    /**
     * Delete all exercises.
     */
    @Query("DELETE FROM exercises")
    suspend fun deleteExercises()

}