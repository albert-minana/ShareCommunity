package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "healthdata")
data class HealthData(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id")
        var id: Int = 0,
        @ColumnInfo(name = "user_id") @SerializedName("user_id")
        var user_id: Int,
        @ColumnInfo(name = "weight") @SerializedName("weight")
        var weight: Float,
        @ColumnInfo(name = "height") @SerializedName("height")
        var height: Float,
        @ColumnInfo(name = "imc") @SerializedName("imc")
        var imc: Float?,
        @ColumnInfo(name = "igc") @SerializedName("igc")
        var igc: Float?,
        @ColumnInfo(name = "date") @SerializedName("date")
        var date: String?
        ) {

        constructor(user_id: Int,
                    weight: Float,
                    height: Float) : this(0, user_id, weight, height, null, null, null)

}
