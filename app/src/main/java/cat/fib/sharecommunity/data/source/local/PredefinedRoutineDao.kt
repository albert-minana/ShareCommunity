package cat.fib.sharecommunity.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.fib.sharecommunity.data.models.PredefinedRoutine

/**
 * Data Access Object for the predefined routine table.
 */
@Dao
interface PredefinedRoutineDao {

    /**
     * Select a predefined routine by id.
     *
     * @param predefinedRoutineId the predefined routine id.
     * @return the predefined routine with id.
     */
    @Query("SELECT * FROM predefinedRoutines WHERE id = :predefinedRoutineId")
    fun getPredefinedRoutineById(predefinedRoutineId: Int): LiveData<PredefinedRoutine>

    /**
     * Insert a predefined routine in the database. If the predefined routine already exists, replace it.
     *
     * @param predefinedRoutine the predefined routine to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPredefinedRoutine(predefinedRoutine: PredefinedRoutine)


    /**
     * Select all predefined routines from the predefined routines table.
     *
     * @return all predefined routines.
     */
    @Query("SELECT * FROM predefinedRoutines")
    fun getPredefinedRoutines(): LiveData<List<PredefinedRoutine>>
}