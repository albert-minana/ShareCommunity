package cat.fib.sharecommunity.firebaseserviceobjects

import android.util.Log
import android.content.Context
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Service.Companion.toService
import cat.fib.sharecommunity.dataclasses.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

object FirebaseServiceService {
    private const val TAG = "FirebaseServiceService"
    suspend fun createService(service: Service): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val nservices = db.collection("users").document(service.userEmail).collection("services").orderBy(
                FieldPath.documentId()).limit(1).get().await().documents.first().toService()?.id
            var id = 0
            if (nservices != null) {
                id = nservices.toInt() + 1
            }
            service.id = id.toString()
            db.collection("users").document(service.userEmail).collection("products").document(service.id)
                .set(hashMapOf("name" to service.name, "description" to service.description, "ubication" to service.ubication,
                    "state" to service.state, "type" to service.type, "data_ini" to service.data_ini, "data_fi" to service.data_fi))
            val product = db.collection("users").document(service.userEmail).collection("products").document(service.id).get().await().toService()
            return Resource.success(service)
        } catch (e: Exception) {
            Log.e(FirebaseServiceService.TAG, "Error getting service details", e)
            FirebaseCrashlytics.getInstance().log("Error getting service details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", service.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    /*suspend fun getservices(): Resource<Service>? {
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
    }*/


    suspend fun getService(userEmail: String, id: String): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val service = db.collection("users").document(userEmail).collection("services").document(id).get().await().toService()
            return Resource.success(service)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting service details", e)
            FirebaseCrashlytics.getInstance().log("Error getting service details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getServicesByUser(userEmail: String): Resource<ArrayList<Service>>? {
        lateinit var resourceResult : Resource<ArrayList<Service>>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userEmail).collection("services").get()
            .addOnSuccessListener { result ->
                var servicesList = ArrayList<Service>()
                for (document in result) {
                    val service = document.toService()
                    servicesList.add(service!!)
                }
                resourceResult = Resource.success(servicesList)
            }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error getting services: ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting services items")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

    /*
    suspend fun getAvailableServices(): Resource<ArrayList<Service>>? {
        lateinit var resourceResult : Resource<ArrayList<Service>>
        val db = FirebaseFirestore.getInstance()
        db.collectionGroup("services").whereEqualTo("state", "disponible").get()
            .addOnSuccessListener { result ->
                var servicesList = ArrayList<Service>()
                for (document in result) {
                    val product = document.toService()
                    servicesList.add(service!!)
                }
                resourceResult = Resource.success(servicesList)
            }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error getting services: ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting services items")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }
*/


    suspend fun getAvailableServices(): Resource<ArrayList<Service>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            //val result = db.collection("users").document("prova@prova.com").collection("products").get().await()
            val result = db.collectionGroup("services").whereEqualTo("state", "Disponible").get().await()
            var serviceList = ArrayList<Service>()
            for (document in result) {
                val product = document.toService()
                serviceList.add(product!!)
            }
            return Resource.success(serviceList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting available service", e)
            FirebaseCrashlytics.getInstance().log("Error getting available service")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun updateService(service: Service): Resource<Service>? {
        lateinit var resourceResult : Resource<Service>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(service.userEmail).collection("services").document(service.id)
            .update("name",service.name,"description", service.description,"ubication", service.ubication, "state", service.state, "type", service.type, "data_ini", service.data_ini, "data_fi", service.data_fi)
            .addOnSuccessListener { result ->
                resourceResult = Resource.success(service) }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error updating service: ", exception)
                FirebaseCrashlytics.getInstance().log("Error updating service")
                FirebaseCrashlytics.getInstance().setCustomKey("id", service.id)
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

    suspend fun deleteAvailableService(userEmail: String, id: String): Resource<String>? {
        lateinit var resourceResult : Resource<String>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userEmail).collection("services").document(id)
            .delete()
            .addOnSuccessListener { result ->
                resourceResult = Resource.success(id) }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error deleting service: ", exception)
                FirebaseCrashlytics.getInstance().log("Error deleting service")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

}
