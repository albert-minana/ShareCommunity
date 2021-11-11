package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Collection
import retrofit2.http.GET
import retrofit2.http.Query

interface CollectionService {

    @GET("/colections")
    fun getCollection(@Query("name") collectionName: String): LiveData<ApiResponse<Collection>>

    /**
     * @GET declares an HTTP GET request
     */
    @GET ("/colections/")
    fun getCollections(): LiveData<ApiResponse<List<Collection>>>
}