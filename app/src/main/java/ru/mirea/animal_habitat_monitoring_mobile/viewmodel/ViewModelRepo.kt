package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.mirea.animal_habitat_monitoring_mobile.model.repository.MyRepository

class ViewModelRepo(private val repository: MyRepository): ViewModel() {
    val data: MutableLiveData<MyRepository.PredictionResponse> = MutableLiveData()

    suspend fun fetchData(photo: MultipartBody.Part, signature: RequestBody) {
        val response = repository.getResult(repository.service, photo, signature)
        data.value = response
    }
    // TODO: Удалить ViewModelRepo за ненадобностью 
}