package ru.mirea.animal_habitat_monitoring_mobile.model.repository

import android.content.Context
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.IOException

class MyRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    private var ipAddress = sharedPreferences.getString("ip_address", "localhost")


    // Создаем экземпляр Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://$ipAddress:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(ConnectivityInterceptor(context))
                .build()
        )
        .build()

    // Создаем экземпляр нашего сервиса
    val service: ApiService = retrofit.create(ApiService::class.java)


    interface ApiService {
        @Multipart
        @POST("/predict")
        suspend fun predict(
            @Part photo: MultipartBody.Part,
            @Part("signature") signature: RequestBody
        ): PredictionResponse
    }

    // Объявляем классы для запроса и ответа
    data class PredictionResponse(val result: String)

    suspend fun getResult(service: ApiService, photo: MultipartBody.Part, signature: RequestBody): PredictionResponse {
        return try {
            service.predict(photo, signature)
        } catch (e: IOException) {
            PredictionResponse("Ошибка подключения к серверу")
        }
    }

    fun RequestBody.toMultipartBody(name: RequestBody): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name.toString(), null, this)
    }
}

