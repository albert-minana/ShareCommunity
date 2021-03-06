package cat.fib.sharecommunity.firebaseserviceobjects

import android.util.Log
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Product.Companion.toProduct
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.dataclasses.UserProfile
import cat.fib.sharecommunity.dataclasses.UserProfile.Companion.toUserProfile
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object FirebaseUserProfileService {
    private const val TAG = "FirebaseUserProfileService"

    suspend fun getUserProfile(email: String): Resource<UserProfile>? {
        val db = FirebaseFirestore.getInstance()
        return try {
            val user = db.collection("users")
                .document(email).get().await().toUserProfile()
            return Resource.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("email", email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun login(email: String, password: String): Resource<UserProfile>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val userProfile = db.collection("users").whereEqualTo("password", password).whereEqualTo(FieldPath.documentId(), email)
                .get().await().documents.first().toUserProfile()
            return Resource.success(userProfile)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("email", email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun login(uid: String): Resource<UserProfile>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val userProfile = db.collection("users").whereEqualTo("uid", uid)
                    .get().await().documents.first().toUserProfile()
            return Resource.success(userProfile)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("uid", uid)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun createUser(user: UserProfile): Resource<UserProfile>? {
        val db = FirebaseFirestore.getInstance()
        try {
             db.collection("users").document(user.email)
                    .set(hashMapOf("provider" to user.provider, "uid" to user.uid, "password" to user.password,
                            "firstname" to user.firstname, "lastname" to user.lastname, "phone" to user.phone,
                            "birthday" to user.birthday, "gender" to user.gender, "aptitudes" to user.aptitudes))
            val userProfile = db.collection("users").document(user.email).get().await().toUserProfile()
            return Resource.success(userProfile)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("email", user.email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun deleteUser(email: String): Resource<String>? {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        try {
            val deleteProducts = db.collection("users").document(email).collection("products").get().await()
            deleteProducts.documents.forEach {
                it.reference.delete().await()
            }
            val deleteServices = db.collection("users").document(email).collection("services").get().await()
            deleteServices.documents.forEach {
                it.reference.delete().await()
            }
            val deletePhotos = storage.getReference().child("/" + email).listAll().await()
            deletePhotos.items.map {
                it.delete().await()
            }

            db.collection("users").document(email).delete().await()
            return Resource.success(email)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting user: ", e)
            FirebaseCrashlytics.getInstance().log("Error deleting user")
            FirebaseCrashlytics.getInstance().setCustomKey("email", email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun updateUserProfile(user: UserProfile): Resource<UserProfile>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("users").document(user.email)
                .update("firstname", user.firstname, "lastname", user.lastname, "password", user.password, "phone", user.phone, "birthday", user.birthday, "gender", user.gender).await()
            val updatedUser = db.collection("users").document(user.email).get().await().toUserProfile()
            return Resource.success(updatedUser)
        }
        catch (e: Exception) {
            Log.e(TAG, "Error updating user: ", e)
            FirebaseCrashlytics.getInstance().log("Error updating user")
            FirebaseCrashlytics.getInstance().setCustomKey("email", user.email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }


}