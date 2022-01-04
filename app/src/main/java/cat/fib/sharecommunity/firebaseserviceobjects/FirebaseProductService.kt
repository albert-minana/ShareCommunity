package cat.fib.sharecommunity.firebaseserviceobjects

import android.util.Log
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Product.Companion.toProduct
import cat.fib.sharecommunity.dataclasses.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseProductService {
    private const val TAG = "FirebaseProductService"
    suspend fun getProductData(id: String): Product? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("products")
                    .document(id).get().await().toProduct()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting product details", e)
            FirebaseCrashlytics.getInstance().log("Error getting product details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getProductes(): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.e(TAG, "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting products: ", exception)
                }
        return null
    }


    suspend fun createProduct(product: Product): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("products").document(product.id)
                    .set(hashMapOf("name" to product.name, "description" to product.description, "ubication" to product.ubication,
                            "state" to product.state, "type" to product.type, "photo" to product.photo))
            val product = db.collection("products").document(product.id).get().await().toProduct()
            return Resource.success(product)
        } catch (e: Exception) {
            Log.e(FirebaseProductService.TAG, "Error getting product details", e)
            FirebaseCrashlytics.getInstance().log("Error getting product details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", product.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }


}
