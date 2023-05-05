package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyViewModel : ViewModel() {
    val message: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val bitmapLiveData = MutableLiveData<Bitmap>()

    var imageBitmap: Bitmap? = null

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("imageBitmap", imageBitmap)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        imageBitmap = savedInstanceState?.getParcelable("imageBitmap")
    }

}
