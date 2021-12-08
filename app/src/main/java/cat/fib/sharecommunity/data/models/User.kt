package cat.fib.sharecommunity.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@Entity(tableName = "user")
data class User(
        @PrimaryKey @ColumnInfo(name = "email") @SerializedName("email")
        var email: String,
        @ColumnInfo(name = "provider") @SerializedName("provider")
        var provider: String,
        @ColumnInfo(name = "uid") @SerializedName("uid")
        var uid: String? = "",
        @ColumnInfo(name = "password") @SerializedName("password")
        var password: String? = "",
        @ColumnInfo(name = "firstname") @SerializedName("firstname")
        var firstname: String,
        @ColumnInfo(name = "lastname") @SerializedName("lastname")
        var lastname: String,
        @ColumnInfo(name = "phone") @SerializedName("phone")
        var phone: String? = "",
        @ColumnInfo(name = "birthday") @SerializedName("birthday")
        var birthday: String? = "",
        @ColumnInfo(name = "gender") @SerializedName("gender")
        var gender: String? = "",
        @ColumnInfo(name = "productsobtained") @SerializedName("productsobtained")
        var productsobtained: ArrayList<String>?,
        @ColumnInfo(name = "servicesobtained") @SerializedName("servicesobtained")
        var servicesobtained: ArrayList<String>?,
        @ColumnInfo(name = "productsgiven") @SerializedName("productsgiven")
        var productsgiven: ArrayList<String>?,
        @ColumnInfo(name = "servicesgiven") @SerializedName("servicesgiven")
        var servicesgiven: ArrayList<String>?,
        @ColumnInfo(name = "aptitudes") @SerializedName("aptitudes")
        var aptitudes: String? = "",
        @ColumnInfo(name = "itemsoffered") @SerializedName("itemsoffered")
        var itemsoffered: ArrayList<Int>?
){
        constructor(email: String,
                    password: String,
                    firstname: String,
                    lastname: String,
                    phone: String,
                    birthday: String,
                    gender: String
                    ) : this(email, "Local", null, password, firstname, lastname, phone, birthday, gender, null, null, null, null, null, null)

        constructor(email: String,
                    provider: String,
                    uid: String,
                    firstname: String,
                    lastname: String
                    ) : this(email, provider, uid, null, firstname, lastname, null, null, null, null, null, null, null, null, null)
}
