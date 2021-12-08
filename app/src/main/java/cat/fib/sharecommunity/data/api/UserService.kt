package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.User
import retrofit2.http.*


interface UserService {
    /**
     * @GET declares an HTTP GET request
     * @Path("id") annotation on the userId parameter marks it as a
     * replacement for the {id} placeholder in the @GET path
     */
    @GET("/users/{id}")
    fun getUser(@Path("id") userId: Int): LiveData<ApiResponse<User>>

    /**
     * @POST declares an HTTP POST request
     */
    @POST("/users/")
    fun createUser(@Body user: User): LiveData<ApiResponse<User>>

    @POST("/users/login")
    fun login(@Body loginInformation: LoginInformation): LiveData<ApiResponse<User>>

    @POST("/users/login")
    fun login(@Body googleFacebookInformation: GoogleFacebookInformation): LiveData<ApiResponse<User>>

    @PATCH("/users/{email}/")
    fun updateUser(@Path("email") userEmail: String, @Body updatedUser: User): LiveData<ApiResponse<User>>

    @DELETE ("/users/{id}/")
    fun deleteUser(@Path ("id") userId: Int): LiveData<ApiResponse<User>>

    @GET("/users")
    fun getUserByEmail(@Query("email") email: String
    ): LiveData<ApiResponse<User>>

}

