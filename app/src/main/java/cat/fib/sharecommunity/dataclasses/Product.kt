package cat.fib.sharecommunity.dataclasses

import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize


@Parcelize
class Product(
        var id: String,
        var name: String,
        var description: String,
        var ubication: String,
        var state: String,
        var type: String,
        var photo: String?
) : Parcelable {

    constructor(id: String,
                name: String,
                description: String,
                ubication: String,
                type: String,
    ) : this(id, name, description, ubication, "disponible", type, null)

    companion object {
        fun DocumentSnapshot.toProduct(): Product? {
            try {
                val name = getString("name")!!
                val description = getString("description")!!
                val ubication = getString("ubication")!!
                val state = getString("state")!!
                val type = getString("type")!!
                val photo = getString("photo")

                return Product(id, name, description, ubication, state, type, photo)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting product", e)
                FirebaseCrashlytics.getInstance().log("Error converting product")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "Product"
    }
}