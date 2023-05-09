package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mirea.animal_habitat_monitoring_mobile.model.repository.MyRepository
import java.io.File


class MyViewModel : ViewModel() {
//    private val data: MutableLiveData<MyRepository.PredictionResponse> = MutableLiveData()

    private val viewModelRepo = ViewModelRepo(MyRepository())

    val message: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val imageBitmapLiveData = MutableLiveData<Bitmap>()

    var imagePath = MutableLiveData<String>()

    val imageUriLiveData = MutableLiveData<Uri>()

//    fun fetchData(photo: MultipartBody.Part, signature: RequestBody) {
//        viewModelScope.launch {
//            val response = repository.getResult(repository.service, photo, signature)
//            data.value = response
//        }
//    }

    fun adaptData() {
        val pathName = imagePath.value.toString()
        val message = message.value!!
        val file = File(pathName)
        val signature = message.toRequestBody("text/plain".toMediaTypeOrNull())
        val photoRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photo = MultipartBody.Part.createFormData("photo", "photo.jpg", photoRequestBody)
        viewModelRepo.fetchData(photo, signature)
    }

//    fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable("imageBitmap", imageBitmap)
//    }
//
//    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        imageBitmap = savedInstanceState?.getParcelable("imageBitmap")
//    }

}
