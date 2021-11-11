package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey @ColumnInfo(name = "name") @SerializedName("name")
    var name: String = "",
    @ColumnInfo(name = "description") @SerializedName("description")
    var description: String,
    @ColumnInfo(name = "predef_routines") @SerializedName("predef_routines")
    var predef_routines: ArrayList<Int>
)