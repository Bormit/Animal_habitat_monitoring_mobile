package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyViewModel : ViewModel() {
    val message: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val imageBitmapLiveData = MutableLiveData<Bitmap>()

    var imageBitmap: Bitmap? = null

    val imageUriLiveData = MutableLiveData<Uri>()

//    fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable("imageBitmap", imageBitmap)
//    }
//
//    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        imageBitmap = savedInstanceState?.getParcelable("imageBitmap")
//    }

}
