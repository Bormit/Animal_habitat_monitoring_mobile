package ru.mirea.animal_habitat_monitoring_mobile.model.repository

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class MyRepository {

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

    // Создаем экземпляр Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.81:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    // Создаем экземпляр нашего сервиса
    val service: ApiService = retrofit.create(ApiService::class.java)

    suspend fun getResult(service: ApiService, photo: MultipartBody.Part, signature: RequestBody): PredictionResponse {
        val response = service.predict(photo, signature)
        return response
    }
    fun RequestBody.toMultipartBody(name: RequestBody): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name.toString(), null, this)
    }
}