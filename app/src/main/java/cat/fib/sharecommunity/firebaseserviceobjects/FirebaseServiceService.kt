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
            val id = db.collection("users").document(service.userEmail).collection("services")
                .add(hashMapOf("name" to service.name, "description" to service.description, "ubication" to service.ubication,
                    "state" to service.state, "type" to service.type, "data_ini" to service.data_ini, "data_fi" to service.data_fi, "publishDate" to service.publishDate, "userEmail" to service.userEmail)).await().id
            service.id = id
            val createdService = db.collection("users").document(service.userEmail).collection("services").document(service.id).get().await().toService()
            return Resource.success(createdService)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating service: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting service details")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getUserServices(userEmail: String): Resource<ArrayList<Service>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val result = db.collection("users").document(userEmail).collection("services").get().await()
            var serviceList = ArrayList<Service>()
            for (document in result) {
                val service = document.toService()
                serviceList.add(service!!)
            }
            return Resource.success(serviceList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user services: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting user services")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getAvailableServices(): Resource<ArrayList<Service>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val result = db.collectionGroup("services").whereEqualTo("state", "Disponible").get().await()
            var serviceList = ArrayList<Service>()
            for (document in result) {
                val service = document.toService()
                serviceList.add(service!!)
            }
            return Resource.success(serviceList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting available services: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting available services")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getService(id: String, userEmail: String): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val service = db.collection("users").document(userEmail).collection("services").document(id).get().await().toService()
            return Resource.success(service)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting service: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting service")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun updateService(service: Service): Resource<Service>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("users").document(service.userEmail).collection("services").document(service.id)
                .update("name", service.name, "description", service.description, "ubication", service.ubication, "state", service.state, "data_ini", service.data_ini, "data_fi", service.data_fi, "type", service.type).await()
            val updatedService = db.collection("users").document(service.userEmail).collection("services").document(service.id).get().await().toService()
            return Resource.success(updatedService)
        }
        catch (e: Exception) {
            Log.e(TAG, "Error updating service: ", e)
            FirebaseCrashlytics.getInstance().log("Error updating service")
            FirebaseCrashlytics.getInstance().setCustomKey("id", service.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun deleteService(id: String, userEmail: String): Resource<String>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("users").document(userEmail).collection("services").document(id).delete().await()
            return Resource.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting service: ", e)
            FirebaseCrashlytics.getInstance().log("Error deleting service")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

}
