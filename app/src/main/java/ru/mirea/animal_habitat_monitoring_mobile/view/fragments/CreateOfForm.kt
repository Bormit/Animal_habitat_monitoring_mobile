package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
    private val dataModel : MyViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var imageAnimals: ImageView
    private lateinit var animalLayout: ConstraintLayout
    private var animal: String = "None"
    private var imageEmpty: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_of_form, container, false)



        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        viewModel.setContext(requireContext())

        imageAnimals = view.findViewById<ImageView>(R.id.imageAnimals)
        animalLayout = view.findViewById<ConstraintLayout>(R.id.animalLayout)


        val cardAnimals = view.findViewById<CardView>(R.id.cardAnimals)
        val spinnerArrayFamily = resources.getStringArray(R.array.spinnerAnimals)
        val spinnerAnimals = view.findViewById<Spinner>(R.id.spinnerAnimals)
        val createForm = view.findViewById<Button>(R.id.createForm)

        setImage()


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
            setImage()
        }

        createForm.setOnClickListener {
            if(animal == "None"){
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
                            when(answer){
                                "hen" -> answer = "Курица"
                                "horse" -> answer = "Лошадь"
                                "squirrel" -> answer = "Белка"
                                "" ->
                                    Toast.makeText(context, "answer пуст", Toast.LENGTH_LONG)
                                        .show()
                                // TODO: Перенести перевод результата на сервер 
                            }
                            if (answer == animal){
                                Toast.makeText(context, "Успешно создана форма $answer", Toast.LENGTH_LONG)
                                    .show()

                                (activity as MainActivity?)?.getPhotoMetadata(viewModel.imagePath.value.toString())
                                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                                val login = sharedPref?.getString("login", "Пользователь")
                                val form = Animal(
                                    latitude = viewModel.latitude.value!!,
                                    longitude = viewModel.longitude.value!!,
                                    species = answer!!,
                                    time = viewModel.dateTime.value!!,
                                    userID = login!!
                                )
                                DatabaseConnection().saveDataToFirebase(form)

                            }
                            else{
                                Toast.makeText(context, "Животное не совпадает с $answer", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }

        val arrayAdapterAnimals =
            context?.let { ArrayAdapter(it, R.layout.spinner_item, spinnerArrayFamily) }
        spinnerAnimals.adapter = arrayAdapterAnimals
        spinnerAnimals.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinnerArrayFamily[p2] != "None") {
                    Toast.makeText(context, spinnerArrayFamily[p2], Toast.LENGTH_SHORT)
                        .show()
                    animal = spinnerArrayFamily[p2]
                    viewModel.message.value = animal
                }
                else{
                    animal = "None"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return view
    }
    private fun setImage(){
        viewModel.imageBitmapLiveData.observe(viewLifecycleOwner) { bitmap ->
            val layoutParams = imageAnimals.layoutParams
            layoutParams.width = bitmap.width
            layoutParams.height = bitmap.height
            imageAnimals.layoutParams = layoutParams
            imageAnimals.setImageBitmap(bitmap)
            imageAnimals.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageAnimals.requestLayout()
            imageEmpty = false
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