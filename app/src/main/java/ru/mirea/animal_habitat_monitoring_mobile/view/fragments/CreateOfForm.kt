package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.db.DatabaseConnection
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal
import ru.mirea.animal_habitat_monitoring_mobile.view.activities.MainActivity
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel

class CreateOfForm : Fragment() {
    private var answer: String? = null
    private lateinit var viewModel: MyViewModel
    private lateinit var imageAnimals: ImageView
    private lateinit var animalLayout: ConstraintLayout
    private var animal: String = "Выбрать значение"
    private var imageEmpty: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_of_form, container, false)

        val context = requireContext()

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        viewModel.setContext(requireContext())

        imageAnimals = view.findViewById<ImageView>(R.id.imageAnimals)
        animalLayout = view.findViewById<ConstraintLayout>(R.id.animalLayout)




        val cardAnimals = view.findViewById<CardView>(R.id.cardAnimals)
        val spinnerArrayFamily = resources.getStringArray(R.array.spinnerAnimals)
        val spinnerAnimals = view.findViewById<Spinner>(R.id.spinnerAnimals)
        val createForm = view.findViewById<Button>(R.id.createForm)
        val clearForm = view.findViewById<ImageView>(R.id.createClose)

        setImage()


        animalLayout.setOnClickListener {
            (activity as MainActivity?)?.takePhoto()
            setImage()
        }

        createForm.setOnClickListener {
            if(animal == "Выбрать значение"){
                Toast.makeText(context, "Не выбрано животное из списка!", Toast.LENGTH_LONG)
                    .show()
            }
            else{
                if(imageEmpty){
                    Toast.makeText(context, "Форма не имеет фотографии!", Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    lifecycleScope.launch {
                        answer = viewModel.adaptData().result
                        answer?.let { // Проверка, что answer не null
                            if (answer == animal){
                                Toast.makeText(context, "Успешно создана форма $answer", Toast.LENGTH_LONG)
                                    .show()

                                (activity as MainActivity?)?.getPhotoMetadata(viewModel.imagePath.value.toString())
                                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                                val login = sharedPref?.getString("login", "Пользователь")
                                viewModel.hasLocation.observe(viewLifecycleOwner) { _ ->
                                    val form = Animal(
                                        latitude = viewModel.latitude.value!!,
                                        longitude = viewModel.longitude.value!!,
                                        species = answer!!,
                                        time = viewModel.dateTime.value!!,
                                        userID = login!!
                                    )
                                    DatabaseConnection().saveDataToFirebase(form)
                                }
                            }
                            else{
                                Toast.makeText(context, answer, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        }

        clearForm.setOnClickListener {
            animal = "Выбрать значение"
            spinnerAnimals.setSelection(0)
            viewModel.imageBitmapLiveData.value = null
            imageAnimals.setImageDrawable(null)
            val layoutParams = imageAnimals.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            imageEmpty = true
        }

        val arrayAdapterAnimals =
//            context?.let { ArrayAdapter(it, R.layout.spinner_item, spinnerArrayFamily) }
            context.let { SpinnerAdapter(context, spinnerArrayFamily) }
        arrayAdapterAnimals.setDropDownViewResource(R.layout.spinner_item)
        spinnerAnimals.adapter = arrayAdapterAnimals
        spinnerAnimals.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as? String
                if (position != 0) {
                    Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT)
                        .show()
                    if (selectedItem != null) {
                        animal = selectedItem
                        viewModel.message.value = animal
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return view
    }
    private fun setImage(){
        viewModel.imageBitmapLiveData.observe(viewLifecycleOwner) { bitmap ->
            if (bitmap != null){
                val exif = ExifInterface(viewModel.imagePath.value.toString())
                val rotationInDegrees = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }

                val matrix = Matrix()
                matrix.postRotate(rotationInDegrees.toFloat())

                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                // Применяем поворот и отображаем фотографию

                val layoutParams = imageAnimals.layoutParams
                layoutParams.width = bitmap.width
                layoutParams.height = bitmap.height
                imageAnimals.layoutParams = layoutParams
                imageAnimals.scaleType = ImageView.ScaleType.FIT_CENTER
//            imageAnimals.setImageBitmap(bitmap)
                imageAnimals.setImageBitmap(rotatedBitmap)
                imageAnimals.requestLayout()
                imageEmpty = false
            }
        }
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        val spinnerAnimals = requireView().findViewById<Spinner>(R.id.spinnerAnimals)
//        // Восстановление выбранной позиции после создания фрагмента
//        viewModel.formSpinnerPosition.value?.let { position ->
//            spinnerAnimals?.setSelection(position)
//            val selectedItem = spinnerAnimals?.selectedItem as? String
//            if (selectedItem != null) {
//                animal = selectedItem
//            }
//        }
////        setImage()
//    }
}