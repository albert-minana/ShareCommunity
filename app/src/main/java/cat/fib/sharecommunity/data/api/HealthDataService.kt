package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.HealthData
import retrofit2.http.*


interface HealthDataService {

    /**
     * @POST declares an HTTP POST request
     */
    @POST("/healthdata/")
    fun createHealthData(@Body healthData: HealthData): LiveData<ApiResponse<HealthData>>


    @GET("/healthdata")
    fun getHealthDataByUserId(@Query("user_id") user_id: Int
    ): LiveData<ApiResponse<HealthData>>

}

