package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Program
import retrofit2.http.GET
import retrofit2.http.Path

interface ProgramService {

    /**
     * @GET declares an HTTP GET request
     * @Path("id") annotation on the programId parameter marks it as a
     * replacement for the {id} placeholder in the @GET path
     */
    @GET("/programs/{id}")
    fun getProgram(@Path("id") programId: Int): LiveData<ApiResponse<Program>>


    /**
     * @GET declares an HTTP GET request
     */
    @GET ("/programs/")
    fun getPrograms(): LiveData<ApiResponse<List<Program>>>

}