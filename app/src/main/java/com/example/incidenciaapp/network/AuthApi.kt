import com.example.incidenciaapp.model.IncidenciaDTO
import com.example.incidenciaapp.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface AuthApi {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @GET("coordinador/incidencias")
    fun getIncidenciasDeFacultad(): Call<List<IncidenciaDTO>>


    @POST("ruta/registrarIncidencia")
    fun registrarIncidencia(@Body dto: IncidenciaDTO): Call<Void>

}


