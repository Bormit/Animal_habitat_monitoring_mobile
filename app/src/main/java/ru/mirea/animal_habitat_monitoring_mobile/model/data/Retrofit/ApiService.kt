//package ru.mirea.animal_habitat_monitoring_mobile.model.data.Retrofit
//
//import kotlinx.coroutines.runBlocking
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.RequestBody.Companion.asRequestBody
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.POST
//import java.io.File
//import kotlinx.coroutines.*
//import okhttp3.*
//import okhttp3.RequestBody.Companion.toRequestBody
//import retrofit2.http.Multipart
//import retrofit2.http.Part
//import java.io.IOException
//
//class ApiService {
//    // Объявляем интерфейс для Retrofit-сервиса
//    interface ApiService {
//        @Multipart
//        @POST("/predict")
//        suspend fun predict(
//            @Part photo: MultipartBody.Part,
//            @Part("signature") signature: RequestBody
//        ): PredictionResponse
//    }
//
//    // Объявляем классы для запроса и ответа
//    data class PredictionResponse(val result: String)
//
//    fun main() = runBlocking {
//
////        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
//        // Создаем экземпляр Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.1.81:8000/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(OkHttpClient())
//            .build()
//
//        // Создаем экземпляр нашего сервиса
//        val service = retrofit.create(ApiService::class.java)
//
//        val file = File("/storage/emulated/0/Android/data/ru.mirea.animal_habitat_monitoring_mobile/files/Pictures/JPEG_20230509_1754317495736493668161882.jpg")
////        val file =
//        val signature = "signature".toRequestBody("text/plain".toMediaTypeOrNull())
//        val photoRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val photo = MultipartBody.Part.createFormData("photo", "photo.jpg", photoRequestBody)
//
//        val response = getResult(service, photo, signature)
//
//        println(response)
//    }
//
//    suspend fun getResult(service: ApiService, photo: MultipartBody.Part, signature: RequestBody): PredictionResponse {
//        val response = service.predict(photo, signature)
//        return response
//    }
//
//    fun RequestBody.toMultipartBody(name: RequestBody): MultipartBody.Part {
//        return MultipartBody.Part.createFormData(name.toString(), null, this)
//    }
//}
//
//
