package ru.mirea.animal_habitat_monitoring_mobile.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.view.fragments.CreateOfForm
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private lateinit var viewModel: MyViewModel
    private val CAMERA_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        supportActionBar?.hide()
    }
////    private fun dispatchTakePictureIntent() {
////        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////        startActivityForResult(intent, CAMERA_REQUEST_CODE)
////        val test = 1
////    }
//
//    private val takePictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
//        if (result) {
////            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
////            viewModel.bitmapLiveData.value = imageBitmap
//        } else {
////            Toast.makeText(this, "Error taking photo", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private var currentPhotoPath: String? = null
//
//    private fun dispatchTakePictureIntent() {
//        val photoFile: File? = try {
//            createImageFile()
//        } catch (ex: IOException) {
//            // Error occurred while creating the File
//            null
//        }
//        // Continue only if the File was successfully created
//        photoFile?.also {
//            val photoURI: Uri = FileProvider.getUriForFile(
//                this,
//                "ru.mirea.animal_habitat_monitoring_mobile.fileprovider",
//                it
//            )
//            takePictureContract.launch(photoURI)
//        }
////        val data = "Hello, World!".toByteArray()
////
////        val file = File.createTempFile("temp_file", ".txt", this.cacheDir)
////        file.writeBytes(data)
////
////        val fileUri = FileProvider.getUriForFile(this, "ru.mirea.animal_habitat_monitoring_mobile.fileprovider", file)
////        val ful = fileUri
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            viewModel.bitmapLiveData.value = imageBitmap
//        }
//    }
//    fun cameraCheckPermission() {
//
//        Dexter.withContext(this)
//            .withPermissions(
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.CAMERA).withListener(
//
//                object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                        report?.let {
//
//                            if (report.areAllPermissionsGranted()) {
//                                dispatchTakePictureIntent()
//                            }
//
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        p0: MutableList<PermissionRequest>?,
//                        p1: PermissionToken?) {
//                        showRotationalDialogForPermission()
//                    }
//
//                }
//            ).onSameThread().check()
//    }
//    private fun showRotationalDialogForPermission() {
//        AlertDialog.Builder(this)
//            .setMessage("It looks like you have turned off permissions"
//                    + "required for this feature. It can be enable under App settings!!!")
//
//            .setPositiveButton("Go TO SETTINGS") { _, _ ->
//
//                try {
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri = Uri.fromParts("package", "ru.mirea.animal_habitat_monitoring_mobile", null)
//                    intent.data = uri
//                    startActivity(intent)
//
//                } catch (e: ActivityNotFoundException) {
//                    e.printStackTrace()
//                }
//            }
//
//            .setNegativeButton("CANCEL") { dialog, _ ->
//                dialog.dismiss()
//            }.show()
//    }


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
            var foo = e.message
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
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success) {
            Log.i(TAG, "Image Location: $uri")
            val strUri = uri
            val inputStream: InputStream? = strUri?.let { contentResolver.openInputStream(it) }
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
            viewModel.imageBitmapLiveData.value = bitmap
        } else {
            Log.e(TAG, "Image not saved. $uri")
        }

    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}