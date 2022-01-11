package cat.fib.sharecommunity.dataclasses

import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
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
    //var photo: String?
) : Parcelable {

    constructor(id: String,
                name: String,
                description: String,
                ubication: String,
                type: String,
                var data_ini: String,
                var data_fi: String,
    ) : this(id, name, description, ubication, "disponible", data_ini, data_fi)

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

                return Service(id, name, description, ubication, state, type, dat_ini, data_fi)
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
}Service