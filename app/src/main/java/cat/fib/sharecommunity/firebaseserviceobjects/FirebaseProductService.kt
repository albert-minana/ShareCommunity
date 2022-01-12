package cat.fib.sharecommunity.firebaseserviceobjects

import android.content.Context
import android.util.Log
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Product.Companion.toProduct
import cat.fib.sharecommunity.dataclasses.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

object FirebaseProductService {
    private const val TAG = "FirebaseProductService"
    suspend fun createProduct(product: Product): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val id = db.collection("users").document(product.userEmail).collection("products")
                    .add(hashMapOf("name" to product.name, "description" to product.description, "ubication" to product.ubication,
                                   "state" to product.state, "type" to product.type, "photo" to product.photo)).await().id
            product.id = id
            val createdProduct = db.collection("users").document(product.userEmail).collection("products").document(product.id).get().await().toProduct()
            return Resource.success(createdProduct)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating product: ", e)
            FirebaseCrashlytics.getInstance().log("Error creating product")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getUserProducts(userEmail: String): Resource<ArrayList<Product>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val result = db.collection("users").document(userEmail).collection("products").get().await()
            var productList = ArrayList<Product>()
            for (document in result) {
                val product = document.toProduct()
                productList.add(product!!)
            }
            return Resource.success(productList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user products: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting user products")
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getAvailableProducts(): Resource<ArrayList<Product>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val result = db.collectionGroup("products").whereEqualTo("state", "Disponible").get().await()
            var productList = ArrayList<Product>()
            for (document in result) {
                val product = document.toProduct()
                productList.add(product!!)
            }
            return Resource.success(productList)
        } catch (e: Exception) {
                Log.e(TAG, "Error getting available products: ", e)
                FirebaseCrashlytics.getInstance().log("Error getting available products")
                FirebaseCrashlytics.getInstance().recordException(e)
                return Resource.error(e)
        }
    }

    suspend fun getProduct(id: String, userEmail: String): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val product = db.collection("users").document(userEmail).collection("products").document(id).get().await().toProduct()
            return Resource.success(product)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting product: ", e)
            FirebaseCrashlytics.getInstance().log("Error getting product")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun updateProduct(product: Product): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("users").document(product.userEmail).collection("products").document(product.id)
                    .update("name", product.name, "description", product.description, "ubication", product.ubication, "state", product.state, "type", product.type, "photo", product.photo).await()
            val updatedProduct = db.collection("users").document(product.userEmail).collection("products").document(product.id).get().await().toProduct()
            return Resource.success(updatedProduct)
        }
        catch (e: Exception) {
            Log.e(TAG, "Error updating product: ", e)
            FirebaseCrashlytics.getInstance().log("Error updating product")
            FirebaseCrashlytics.getInstance().setCustomKey("id", product.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun deleteProduct(id: String, userEmail: String): Resource<String>? {
        lateinit var resourceResult : Resource<String>
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("users").document(userEmail).collection("products").document(id).delete().await()
            return Resource.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting product: ", e)
            FirebaseCrashlytics.getInstance().log("Error deleting product")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

}
