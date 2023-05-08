package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.view.activities.MainActivity
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel

class CreateOfForm : Fragment() {
    private lateinit var viewModel: MyViewModel
    private val dataModel : MyViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var imageAnimals: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_of_form, container, false)

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        imageAnimals = view.findViewById<ImageView>(R.id.imageAnimals)
        val animalLayout = view.findViewById<ConstraintLayout>(R.id.animalLayout)

//        if (imageAnimals.drawable.toBitmap() != null) {
//            setParamsImage()
//        }


        animalLayout.setOnClickListener {
//            MainActivity().takePhoto()
            (activity as MainActivity?)?.takePhoto()
//            viewModel.bitmapLiveData.observe(viewLifecycleOwner) { bitmap ->
//                imageAnimals.setImageBitmap(bitmap)
//            }
//            dataModel.bitmapLiveData.observe(activity as LifecycleOwner) {
//                viewModel.imageBitmap = it
//                imageAnimals.setImageBitmap(it)
//            }
            setParamsImage()
        }







        return view
    }
    private fun setParamsImage(){
        viewModel.imageBitmapLiveData.observe(viewLifecycleOwner) { bitmap ->
            val layoutParams = imageAnimals.layoutParams
            layoutParams.width = bitmap.width
            layoutParams.height = bitmap.height
            imageAnimals.layoutParams = layoutParams
            imageAnimals.setImageBitmap(bitmap)
            imageAnimals.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageAnimals.requestLayout()

        }
    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.onSaveInstanceState(outState)
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        viewModel.onRestoreInstanceState(savedInstanceState)
//    }
}