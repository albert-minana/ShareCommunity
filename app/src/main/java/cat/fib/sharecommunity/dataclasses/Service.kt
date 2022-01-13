package cat.fib.sharecommunity.dataclasses

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize


@Parcelize
class Service(
        var id: String,
        var name: String,
        var description: String,
        var ubication: String,
        var state: String,
        var type: String,
        var data_ini: String,
        var data_fi: String,
        var publishDate: String,
        var userEmail: String
) : Parcelable {

    constructor(name: String,
                description: String,
                ubication: String,
                type: String,
                data_ini: String,
                data_fi: String,
                publishDate: String,
                userEmail: String
    ) : this("0", name, description, ubication, "Disponible",type ,data_ini, data_fi, publishDate, userEmail)


    companion object {
        fun DocumentSnapshot.toService(): Service? {
            try {
                val name = getString("name")!!
                val description = getString("description")!!
                val ubication = getString("ubication")!!
                val state = getString("state")!!
                val type = getString("type")!!
                val data_ini = getString("data_ini")!!
                val data_fi = getString("data_fi")!!
                val publishDate = getString("publishDate")!!
                val userEmail = getString("userEmail")!!
                return Service(id, name, description, ubication, state, type, data_ini, data_fi, publishDate, userEmail)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting service", e)
                FirebaseCrashlytics.getInstance().log("Error converting service")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "Service"
    }
}