package cat.fib.sharecommunity.data.api

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Exercise
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseService {
     /**
         * @GET declares an HTTP GET request
         * @Path("name") annotation on the exerciseName parameter marks it as a
         * replacement for the {name} placeholder in the @GET path
         */
        @GET("/exercises/{name}")
        fun getExercise(@Path("name") exerciseName: String): LiveData<ApiResponse<Exercise>>

        /**
         * @GET declares an HTTP GET request
         */
        @GET("/exercises/")
        fun getExercises(): LiveData<ApiResponse<List<Exercise>>>

}

