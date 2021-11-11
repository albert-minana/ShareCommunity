package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@Entity(tableName = "user")
data class User(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id")
        var id: Int = 0,
        @ColumnInfo(name = "firstname") @SerializedName("firstname")
        var firstname: String,
        @ColumnInfo(name = "lastname") @SerializedName("lastname")
        var lastname: String,
        @ColumnInfo(name = "username") @SerializedName("username")
        var username: String,
        @ColumnInfo(name = "achievements") @SerializedName("achivements")
        var achievements: String? = "",
        @ColumnInfo(name = "password") @SerializedName("password")
        var password: String? = "",
        @ColumnInfo(name = "activitiesdone") @SerializedName("activitiesdone")
        var activitiesdone: Int?,
        @ColumnInfo(name = "points") @SerializedName("points")
        var points: Int?,
        @ColumnInfo(name = "level") @SerializedName("level")
        var level: String? = "",
        @ColumnInfo(name = "objectives") @SerializedName("objectives")
        var objectives: ArrayList<String>?,
        @ColumnInfo(name = "categories") @SerializedName("categories")
        var categories: ArrayList<String>?,
        @ColumnInfo(name = "weight") @SerializedName("weight")
        var weight: Float?,
        @ColumnInfo(name = "height") @SerializedName("height")
        var height: Float?,
        @ColumnInfo(name = "imc") @SerializedName("imc")
        var imc: Float?,
        @ColumnInfo(name = "igc") @SerializedName("igc")
        var igc: Float?,
        @ColumnInfo(name = "updated") @SerializedName("updated")
        var updated: String? = "",
        @ColumnInfo(name = "email") @SerializedName("email")
        var email: String,
        @ColumnInfo(name = "gender") @SerializedName("gender")
        var gender: String? = "",
        @ColumnInfo(name = "birthdate") @SerializedName("birthdate")
        var birthdate: String? = "",
        @ColumnInfo(name = "uid") @SerializedName("uid")
        var uid: String?,
        @ColumnInfo(name = "provider") @SerializedName("provider")
        var provider: String?
){
        constructor(firstname: String,
                    lastname: String,
                    username: String,
                    password: String,
                    email: String,
                    gender: String,
                    birthdate: String) : this(0, firstname, lastname, username, null, password, null, null, null, null, null, null, null, null, null, null, email, gender, birthdate, null, null)

        constructor(firstname: String,
                    lastname: String,
                    username: String,
                    email: String,
                    uid: String,
                    provider: String) : this(0, firstname, lastname, username, null, null, null, null, null, null, null, null, null, null, null, null, email, null, null, uid, provider)
}
