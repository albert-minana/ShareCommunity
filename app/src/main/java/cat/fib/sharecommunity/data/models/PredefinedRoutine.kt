package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "predefinedRoutines")
data class PredefinedRoutine(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "name") @SerializedName("name")
    var name: String,
    @ColumnInfo(name = "description") @SerializedName("description")
    var description: String,
    @ColumnInfo(name = "categories") @SerializedName("categories")
    var categories: ArrayList<String>,
    @ColumnInfo(name = "time") @SerializedName("time")
    var time: String,
    @ColumnInfo(name = "age") @SerializedName("age")
    var age: String,
    @ColumnInfo(name = "level") @SerializedName("level")
    var level: String,
    @ColumnInfo(name = "equipment") @SerializedName("equipment")
    var equipment: String,
    @ColumnInfo(name = "objective") @SerializedName("objective")
    var objective: String,
    @ColumnInfo(name = "impact") @SerializedName("impact")
    var impact: String,
    @ColumnInfo(name = "image") @SerializedName("image")
    var image: String?,
    @ColumnInfo(name = "exercises") @SerializedName("exercises")
    var exercises: ArrayList<String>,
    @ColumnInfo(name = "classes") @SerializedName("classes")
    var classes: ArrayList<String>
)