package cat.fib.sharecommunity.dataclasses

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfile(
    var email: String, //Document ID is actually the user id
    var provider: String,
    var uid: String?,
    var password: String?,
    var firstname: String,
    var lastname: String,
    var phone: String?,
    var birthday: String?,
    var gender: String?,
    /*var productsobtained: ArrayList<String>,
    var servicesobtained: ArrayList<String>,
    var productsgiven: ArrayList<String>,
    var servicesgiven: ArrayList<String>, */
    var aptitudes: String?) : Parcelable {

    constructor(email: String,
            password: String,
            firstname: String,
            lastname: String,
            phone: String,
            birthday: String,
            gender: String
    ) : this(email, "Local", null, password, firstname, lastname, phone, birthday, gender, null)

    constructor(email: String,
            provider: String,
            uid: String,
            firstname: String,
            lastname: String
    ) : this(email, provider, uid, null, firstname, lastname, null, null, null, null)

    companion object {
        fun DocumentSnapshot.toUserProfile(): UserProfile? {
            try {
                val provider = getString("provider")!!
                val uid = getString("uid")
                val password = getString("password")
                val firstname = getString("firstname")!!
                val lastname = getString("lastname")!!
                val phone = getString("phone")
                val birthday = getString("birthday")
                val gender = getString("gender")
                /* val ArrayList<String> productsobtained = get("productsobtained")!!
                val servicesobtained = get("servicesobtained")!!
                val productsgiven = get("productsgiven")!!
                val servicesgiven = get("servicesgiven")!! */
                val aptitudes = getString("aptitudes")
                return UserProfile(id, provider, uid, password, firstname, lastname, phone, birthday, gender, aptitudes)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("email", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "UserProfile"
    }
}