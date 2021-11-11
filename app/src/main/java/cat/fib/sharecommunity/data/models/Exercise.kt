package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a Exercise. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param id id of the exercise
 * @param name name of the exercise
 * @param image image of the exercise
 * @param video video of the exercise
 */

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey @ColumnInfo(name = "name") @SerializedName("name")
    var name: String = "",
    @ColumnInfo(name = "pre") @SerializedName("pre")
    var pre: String,
    @ColumnInfo(name = "videotutorial") @SerializedName("videotutorial")
    var videotutorial: String,
    @ColumnInfo(name = "videoexercise") @SerializedName("videoexercise")
    var videoexercise: String,
    @ColumnInfo(name = "description") @SerializedName("description")
    var description: String,
    @ColumnInfo(name = "muscle") @SerializedName("muscle")
    var muscle: String,
    @ColumnInfo(name = "age") @SerializedName("age")
    var age: String,
    @ColumnInfo(name = "difficulty") @SerializedName("difficulty")
    var difficulty: String,
    @ColumnInfo(name = "categories") @SerializedName("categories")
    var categories: ArrayList<String>,
    @ColumnInfo(name = "length") @SerializedName("length")
    var length: String,
    @ColumnInfo(name = "muscleimage") @SerializedName("muscleimage")
    var muscleimage: String
)