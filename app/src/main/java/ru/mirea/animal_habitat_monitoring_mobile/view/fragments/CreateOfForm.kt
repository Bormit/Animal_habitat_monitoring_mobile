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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_of_form, container, false)

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val imageAnimals = view.findViewById<ImageView>(R.id.imageAnimals)

        if (viewModel.imageBitmap != null) {
            imageAnimals.setImageBitmap(viewModel.imageBitmap)
        }


        imageAnimals.setOnClickListener {
//            MainActivity().takePhoto()
            (activity as MainActivity?)?.takePhoto()
//            viewModel.bitmapLiveData.observe(viewLifecycleOwner) { bitmap ->
//                imageAnimals.setImageBitmap(bitmap)
//            }

        }

        dataModel.bitmapLiveData.observe(activity as LifecycleOwner) {
            viewModel.imageBitmap = it
            imageAnimals.setImageBitmap(it)
        }

        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }
}