package cat.fib.sharecommunity.firebaseserviceobjects

import android.util.Log
import cat.fib.sharecommunity.dataclasses.UserProfile
import cat.fib.sharecommunity.dataclasses.UserProfile.Companion.toUserProfile
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseUserProfileService {
    private const val TAG = "FirebaseUserProfileService"
    suspend fun getUserProfileData(email: String): UserProfile? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users")
                .document(email).get().await().toUserProfile()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("email", email)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun login(email: String, password: String): UserProfile? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users").whereEqualTo("password", password).whereEqualTo(FieldPath.documentId(), email)
                .get().await().documents.first().toUserProfile()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("email", email)
            FirebaseCrashlytics.getInstance().recordException(e)
            return null
        }
    }


}