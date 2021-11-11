package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "program", indices = [Index(value = ["name", "level"], unique = true)])
data class Program(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "name") @SerializedName("name")
    var name: String = "",
    @ColumnInfo(name = "description") @SerializedName("description")
    var description: String = "",
    @ColumnInfo(name = "level") @SerializedName("level")
    var difficulty: String = "",
    @ColumnInfo(name = "weeks") @SerializedName("weeks")
    var weeks: Int = 0,
    @ColumnInfo(name = "predef_routines") @SerializedName("predef_routines")
    var predef_routines: ArrayList<Int>
){
    constructor(name: String,
                description: String,
                level: String,
                weeks: Int,
                predef_routines: ArrayList<Int>) : this(0, name, description, "B", 0, ArrayList())
}