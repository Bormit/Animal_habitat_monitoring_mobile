package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mirea.animal_habitat_monitoring_mobile.model.db.DatabaseConnection
import ru.mirea.animal_habitat_monitoring_mobile.model.repository.MyRepository
import java.io.File


class MyViewModel : ViewModel() {

    private lateinit var context: Context

    private lateinit var viewModelRepo: ViewModelRepo

//    private lateinit var dbConnect

    val message: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val imageBitmapLiveData = MutableLiveData<Bitmap>()

    var imagePath = MutableLiveData<String>()

    val imageUriLiveData = MutableLiveData<Uri>()

    var result: MutableLiveData<MyRepository.PredictionResponse> = MutableLiveData()

    val latitude = MutableLiveData<Double>()

    val longitude = MutableLiveData<Double>()

    val dateTime = MutableLiveData<String>()

    private val getFirebase: MutableLiveData<String> = MutableLiveData()
    val resultFirebase: LiveData<String> = getFirebase

    fun setContext(context: Context){
        this.context = context
        viewModelRepo = ViewModelRepo(MyRepository(context))
    }
//    fun setContextForFirebase(context: Context){
//        this.context = context
//        dbConnect = DatabaseConnection(context)
//    }

    suspend fun adaptData(): MyRepository.PredictionResponse{
        val pathName = imagePath.value.toString()
        val message = message.value!!
        val file = File(pathName)
        val signature = message.toRequestBody("text/plain".toMediaTypeOrNull())
        val photoRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photo = MultipartBody.Part.createFormData("photo", "photo.jpg", photoRequestBody)
        return MyRepository(context).getResult(MyRepository(context).service, photo, signature)
    }
    // TODO: Создать отдельную функцию для запроса на сервер во ViewModel 

//    fun getFromFirebase() {
//        DatabaseConnection().fetchDataFromFirebase { result ->
//            getFirebase.postValue(result)
//        }
//    }
    // TODO: Создать функции для записи и чтения из FireBase через ViewModel 

//    fun setOnFirebase(){
//        viewModelScope.launch {
//
//        }
//    }


//    fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable("imageBitmap", imageBitmap)
//    }
//
//    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        imageBitmap = savedInstanceState?.getParcelable("imageBitmap")
//    }

}
