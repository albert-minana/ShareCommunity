package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import retrofit2.http.GET
import retrofit2.http.Path

interface PredefinedRoutineService {

    /**
     * @GET declares an HTTP GET request
     * @Path("id") annotation on the predefinedRoutineId parameter marks it as a
     * replacement for the {id} placeholder in the @GET path
     */
    @GET("/predefinedroutines/{id}")
    fun getPredefinedRoutine(@Path("id") predefinedRoutineId: Int): LiveData<ApiResponse<PredefinedRoutine>>

    /**
     * @GET declares an HTTP GET request
     */
    @GET("/predefinedroutines/")
    fun getPredefinedRoutines(): LiveData<ApiResponse<List<PredefinedRoutine>>>

}