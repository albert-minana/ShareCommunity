package cat.fib.sharecommunity.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.fib.sharecommunity.data.models.Class

/**
 * Data Access Object for the class table.
 */
@Dao
interface ClassDao {

    /**
     * Select a class by name.
     *
     * @param className the class name.
     * @return the class with name.
     */
    @Query("SELECT * FROM classes WHERE name = :className")
    fun getClassByName(className: String): LiveData<Class>

    /**
     * Insert a class in the database. If the class already exists, replace it.
     *
     * @param classe the class to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClass(classe: Class)

    /**
     * Select all classes from the classes table.
     *
     * @return all classes.
     */
    @Query("SELECT * FROM classes")
    fun getClasses(): LiveData<List<Class>>

}