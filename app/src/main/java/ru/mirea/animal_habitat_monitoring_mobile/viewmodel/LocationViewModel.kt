package ru.mirea.animal_habitat_monitoring_mobile.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationViewModel : ViewModel() {
    private lateinit var context: Context
    private val locationPermissionCode = 2
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private val _hasLocation = MutableLiveData<Boolean>()
    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()

    val hasLocation: LiveData<Boolean> get() = _hasLocation
    val latitude: LiveData<Double> get() = _latitude
    val longitude: LiveData<Double> get() = _longitude

    fun setContext(context: Context) {
        this.context = context
    }

    fun onLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                _latitude.value = location.latitude
                _longitude.value = location.longitude
                _hasLocation.value = true
            } else {
                _hasLocation.value = false
            }
        }
    }
}


