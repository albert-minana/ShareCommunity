package cat.fib.sharecommunity.firebaseserviceobjects

import android.content.Context
import android.util.Log
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Product.Companion.toProduct
import cat.fib.sharecommunity.dataclasses.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

object FirebaseProductService {
    private const val TAG = "FirebaseProductService"
    suspend fun createProduct(product: Product): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val nproducts = db.collection("users").document(product.userEmail).collection("products").orderBy(
                FieldPath.documentId()).limit(1).get().await().documents.first().toProduct()?.id
            var id = 0
            if (nproducts != null) {
                id = nproducts.toInt() + 1
            }
            product.id = id.toString()
            db.collection("users").document(product.userEmail).collection("products").document(product.id)
                .set(hashMapOf("name" to product.name, "description" to product.description, "ubication" to product.ubication,
                    "state" to product.state, "type" to product.type, "photo" to product.photo))
            val product = db.collection("users").document(product.userEmail).collection("products").document(product.id).get().await().toProduct()
            return Resource.success(product)
        } catch (e: Exception) {
            Log.e(FirebaseProductService.TAG, "Error getting product details", e)
            FirebaseCrashlytics.getInstance().log("Error getting product details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", product.id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getProduct(userEmail: String, id: String): Resource<Product>? {
        val db = FirebaseFirestore.getInstance()
        try {
            val product = db.collection("users").document(userEmail).collection("products").document(id).get().await().toProduct()
            return Resource.success(product)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting product details", e)
            FirebaseCrashlytics.getInstance().log("Error getting product details")
            FirebaseCrashlytics.getInstance().setCustomKey("id", id)
            FirebaseCrashlytics.getInstance().recordException(e)
            return Resource.error(e)
        }
    }

    suspend fun getProductsByUser(userEmail: String): Resource<ArrayList<Product>>? {
        lateinit var resourceResult : Resource<ArrayList<Product>>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userEmail).collection("products").get()
            .addOnSuccessListener { result ->
                var productList = ArrayList<Product>()
                for (document in result) {
                    val product = document.toProduct()
                    productList.add(product!!)
                }
                resourceResult = Resource.success(productList)
            }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error getting products: ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting products items")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

    /*
    suspend fun getAvailableProducts(): Resource<ArrayList<Product>>? {
        lateinit var resourceResult : Resource<ArrayList<Product>>
        val db = FirebaseFirestore.getInstance()
        db.collectionGroup("products").whereEqualTo("state", "disponible").get()
            .addOnSuccessListener { result ->
                var productList = ArrayList<Product>()
                for (document in result) {
                    val product = document.toProduct()
                    productList.add(product!!)
                }
                resourceResult = Resource.success(productList)
            }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error getting products: ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting products items")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }
    */

    suspend fun getAvailableProducts(): Resource<ArrayList<Product>>? {
        val db = FirebaseFirestore.getInstance()
        try {
            //val result = db.collection("users").document("prova@prova.com").collection("products").get().await()
            val result = db.collectionGroup("products").whereEqualTo("state", "Disponible").get().await()
            var productList = ArrayList<Product>()
            for (document in result) {
                val product = document.toProduct()
                productList.add(product!!)
            }
            return Resource.success(productList)
        } catch (e: Exception) {
                Log.e(TAG, "Error getting available products", e)
                FirebaseCrashlytics.getInstance().log("Error getting available products")
                FirebaseCrashlytics.getInstance().recordException(e)
                return Resource.error(e)
        }
    }

    suspend fun updateProduct(product: Product): Resource<Product>? {
        lateinit var resourceResult : Resource<Product>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(product.userEmail).collection("products").document(product.id)
            .update("name",product.name,"description", product.description,"ubication", product.ubication, "state", product.state, "type", product.type, "photo", product.photo)
            .addOnSuccessListener { result ->
                resourceResult = Resource.success(product) }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error updating product: ", exception)
                FirebaseCrashlytics.getInstance().log("Error updating product")
                FirebaseCrashlytics.getInstance().setCustomKey("id", product.id)
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

    suspend fun deleteAvailableProduct(userEmail: String, id: String): Resource<String>? {
        lateinit var resourceResult : Resource<String>
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userEmail).collection("products").document(id)
            .delete()
            .addOnSuccessListener { result ->
                resourceResult = Resource.success(id) }
            .addOnFailureListener { exception ->
                resourceResult = Resource.error(exception)
                Log.e(TAG, "Error deleting product: ", exception)
                FirebaseCrashlytics.getInstance().log("Error deleting product")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        return resourceResult
    }

}
