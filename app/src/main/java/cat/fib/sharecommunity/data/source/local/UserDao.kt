package cat.fib.sharecommunity.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.fib.sharecommunity.data.models.User

/**
 * Data Access Object for the user table.
 */
@Dao
interface UserDao {

    /**
     * Select a user by id.
     *
     * @param userId the user id.
     * @return the user with id.
     */
    /*
    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserById(userId: Int): LiveData<User>

     */

    /**
     * Select a user by userEmail.
     *
     * @param userEmail the email of the user.
     * @return the user with email.
     */
    @Query("SELECT * FROM user WHERE email = :userEmail")
    fun getUserByEmail(userEmail: String): LiveData<User>

    /**
     * Select a user by userUid.
     *
     * @param userUid the uid of the user.
     * @return the user with uid.
     */
    @Query("SELECT * FROM user WHERE uid = :userUid")
    fun getUserByUid(userUid: String): LiveData<User>


    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
/*
    /**
     * Delete a user in the database.
     *
     * @param userId the user id.
     */
    @Query("DELETE FROM user WHERE id = :userId")
    fun deleteUser(userId: Int)

 */
}