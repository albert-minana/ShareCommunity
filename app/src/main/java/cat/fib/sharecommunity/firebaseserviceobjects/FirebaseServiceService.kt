package cat.fib.sharecommunity.firebaseserviceobjects

import android.util.Log
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Service.Companion.toService
import cat.fib.sharecommunity.dataclasses.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseServiceService {
    private const val TAG = "FirebaseServiceService"
    suspend fun getServiceData(id: String): Service? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("services")
                    .document(id).get().await().toService()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting service details", e)
            FirebaseCrashlytics.getInstance().log("Error getting service details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getservices(): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        db.collection("service")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.e(TAG, "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting services: ", exception)
                }
        return null
    }


    suspend fun createservice(service: Service): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("services").document(service.id)
                    .set(hashMapOf("name" to service.name, "description" to service.description, "ubication" to service.ubication,
                            "state" to service.state, "type" to service.type, "data_ini" to service.data_ini, "data_fi" to service.data_fi))
            val service = db.collection("services").document(service.id).get().await().toService()
            return Resource.success(service)
        } catch (e: Exception) {
            Log.e(FirebaseServiceService.TAG, "Error getting service details", e)
            FirebaseCrashlytics.getInstance().log("Error getting service details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", service.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }


}
