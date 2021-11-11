package cat.fib.sharecommunity.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.fib.sharecommunity.data.models.HealthData

/**
 * Data Access Object for the user table.
 */
@Dao
interface HealthDataDao {

    /**
     * Select a healthData by user_id.
     *
     * @param userId the user id.
     * @return the healthData with user_id.
     */
    @Query("SELECT * FROM healthdata WHERE user_id = :userId")
    fun getHealthDataByUserId(userId: Int): LiveData<HealthData>

    /**
     * Select a healthData by id.
     *
     * @param id the healthData id.
     * @return the healthData with id.
     */
    @Query("SELECT * FROM healthdata WHERE id = :healthDataId")
    fun getHealthDataById(healthDataId: Int): LiveData<HealthData>


    /**
     * Insert a healthData in the database. If the healthData already exists, replace it.
     *
     * @param healthData the healthData to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHealthData(healthData: HealthData)

}