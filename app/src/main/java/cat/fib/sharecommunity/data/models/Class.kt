package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "classes")
data class Class(
        @PrimaryKey @ColumnInfo(name = "name") @SerializedName("name")
        var name: String,
        @ColumnInfo(name = "description") @SerializedName("description")
        var description: String,
        @ColumnInfo(name = "age") @SerializedName("age")
        var age: String,
        @ColumnInfo(name = "difficulty") @SerializedName("difficulty")
        var difficulty: String,
        @ColumnInfo(name = "length") @SerializedName("length")
        var length: String,
        @ColumnInfo(name = "categories") @SerializedName("categories")
        var categories: ArrayList<String>,
        @ColumnInfo(name = "pre") @SerializedName("pre")
        var pre: String,
        @ColumnInfo(name = "workarea") @SerializedName("workarea")
        var workarea: String,
        @ColumnInfo(name = "trainer") @SerializedName("trainer")
        var trainer: String,
        @ColumnInfo(name = "videoclass") @SerializedName("videoclass")
        var videoclass: String,
)