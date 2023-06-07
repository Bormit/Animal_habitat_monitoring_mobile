package ru.mirea.animal_habitat_monitoring_mobile.view.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.LocationViewModel
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel
import java.io.File
import java.io.InputStream
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private lateinit var viewModel: MyViewModel
    private lateinit var location: LocationViewModel
    private val locationPermissionCode = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        viewModel.setContext(this)

        location = ViewModelProvider(this)[LocationViewModel::class.java]
        location.setContext(this)

        supportActionBar?.hide()
    }

    fun takePhoto(){
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
            // The user has already granted permission for these activities.  Toggle the camera!
            invokeCamera()
        } else {
            // The user has not granted permissions, so we must request.
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        } else {
            Toast.makeText(this, getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG).show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "ru.mirea.animal_habitat_monitoring_mobile.fileprovider", file)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
//            Toast.makeText(applicationContext, currentImagePath, Toast.LENGTH_LONG)
//            .show()
            viewModel.imagePath.value = currentImagePath
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success) {
            Log.i(TAG, "Image Location: $uri")
            val strUri = uri
            viewModel.imageUriLiveData.value = uri
            val inputStream: InputStream? = strUri?.let { contentResolver.openInputStream(it) }
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
            viewModel.imageBitmapLiveData.value = bitmap
        } else {
            Log.e(TAG, "Image not saved. $uri")
        }

    }

    private fun convertToDecimalDegrees(coordinate: Double, reference: String): Double {
        val degrees = Location.convert(coordinate, Location.FORMAT_SECONDS)
        val parts = degrees.split(":")

        val degreesValue = parts[0].toDouble()
        val minutesValue = parts[1].toDouble()
        val secondsValue = parts[2].toDouble()

        val decimalDegrees = degreesValue + (minutesValue / 60) + (secondsValue / 3600)

        return if (reference == "S" || reference == "W") {
            -decimalDegrees
        } else {
            decimalDegrees
        }
    }

    fun getPhotoMetadata(photoPath: String) {
        try {
            val exifInterface = ExifInterface(photoPath)

            val dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)

            val hasLocation = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null &&
                    exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null

            if (hasLocation) {
                val latitude = exifInterface.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE, 0.0)
                val longitude = exifInterface.getAttributeDouble(ExifInterface.TAG_GPS_LONGITUDE, 0.0)

                val latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                val longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

                // Преобразование географических координат в формат десятичных чисел
                val decimalLatitude = latitudeRef?.let { convertToDecimalDegrees(latitude, it) }
                val decimalLongitude = longitudeRef?.let { convertToDecimalDegrees(longitude, it) }

                viewModel.dateTime.value = dateTime
                if (decimalLatitude != 0.0 && decimalLongitude != 0.0) {
                    viewModel.latitude.value = decimalLatitude
                    viewModel.longitude.value = decimalLongitude
                    viewModel.hasLocation.value = true
                }
                else{
                    takeGPS()
                }
            } else {
                viewModel.dateTime.value = dateTime
                takeGPS()
            }
            // Используйте полученные метаданные по своему усмотрению
            viewModel.dateTime.value = dateTime

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun takeGPS() {
        if (hasFinePermission() == PERMISSION_GRANTED && hasCoarsePermission() == PERMISSION_GRANTED) {
            // The user has already granted permission for these activities.
            takeLocation()
        } else {
            // The user has not granted permissions, so we must request.
            requestGPSPermissionsLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private val requestGPSPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            takeLocation()
        } else {
            Toast.makeText(this, getString(R.string.GPSPermissionDenied), Toast.LENGTH_LONG).show()
        }
    }

    private fun takeLocation() {
        location.onLocationPermissionGranted()
        location.hasLocation.observe(this) {
            viewModel.latitude.value = location.latitude.value
            viewModel.longitude.value = location.longitude.value
            viewModel.hasLocation.value = true
        }
    }


    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    private fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun hasFinePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    private fun hasCoarsePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
}