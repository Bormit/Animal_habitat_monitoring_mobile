package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.util.GeoPoint
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel

class UserProfile : Fragment() {
    private lateinit var viewModel: MyViewModel
    private val userProfile = this
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val context = requireContext()

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        viewModel.setContext(requireContext())

        val userProfile = view.findViewById<TextView>(R.id.userProfileName)
        val exitProfile = view.findViewById<ConstraintLayout>(R.id.exitProfile)
        val photoCount = view.findViewById<TextView>(R.id.photoCount)

        countPhoto()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val login = sharedPref?.getString("login", "Пользователь")
        if (login != "Пользователь"){
            getFIO()
        }

        viewModel.photoCount.observe(viewLifecycleOwner){ count ->
            photoCount.text = count.toString()
        }
        viewModel.userFIO.observe(viewLifecycleOwner) { fio ->
            userProfile.text = fio
        }

        exitProfile.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.remove("login")?.apply()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.containerFragment,Profile())
                ?.commit()
            Toast.makeText(context, "Вы вышли из аккаунта!", Toast.LENGTH_SHORT)
                .show()
        }

        return view
    }

    private fun getFIO() {
        val dbref = FirebaseDatabase.getInstance().getReference("user")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val login = sharedPref?.getString("login", "Пользователь")
        val fioRef = dbref.child(login!!).child("FIO")

        fioRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fio = snapshot.getValue(String::class.java)
                viewModel.userFIO.value = fio
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun countPhoto() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val login = sharedPref?.getString("login", "Пользователь")
        val dbref = FirebaseDatabase.getInstance().getReference("animal")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var photoCount = 0 // Переменная для подсчета количества фотографий

                if (snapshot.exists() && userProfile.isVisible) {


                    for (userSnapshot in snapshot.children) {
                        val userID = userSnapshot.child("userID").getValue(String::class.java)
                        if(userID == login){
                            photoCount++ // Увеличение счетчика фотографий
                        }
                    }
                    viewModel.photoCount.value = photoCount
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}