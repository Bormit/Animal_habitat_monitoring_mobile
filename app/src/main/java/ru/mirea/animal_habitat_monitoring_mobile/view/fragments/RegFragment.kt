package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.User

class RegFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reg, container, false)
        val buttLog = view.findViewById<TextView>(R.id.regAuth)
        buttLog.setOnClickListener {
            findNavController().navigate(R.id.action_regFragment_to_loginFragment)
        }

        val buttReg = view.findViewById<Button>(R.id.regRegis)
        val regUser = view.findViewById<EditText>(R.id.regUser)
        val regFIO = view.findViewById<EditText>(R.id.regFIO)
        val regPass = view.findViewById<EditText>(R.id.regPass)

        buttReg.setOnClickListener {
            val database = Firebase.database.reference
            val user = User(
                regUser.text.toString(), regPass.text.toString(), regFIO.text.toString()
            )

            database.child("user").child(user.login).get().addOnSuccessListener {
                if(regUser.text.toString().isNotEmpty())
                {
                    if (it.value == null)
                    {
                        if (regUser.text.toString().length in 18 downTo 4)
                        {
                            if (regPass.text.toString().length in 20 downTo 8)
                            {
                                if (regFIO.text.toString().isNotEmpty())
                                {
                                    database.child("user").child(user.login)
                                    database.child("user").child(user.login)
                                        .child("password")
                                        .setValue(BCrypt.hashpw(user.pass, BCrypt.gensalt()))
                                    database.child("user").child(user.login)
                                        .child("FIO")
                                        .setValue(user.FIO)

                                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                                    if (sharedPref != null)
                                    {
                                        with (sharedPref.edit()) {
                                            putString("login", regUser.text.toString())
                                            apply()
                                        }
                                        Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT)
                                            .show()
                                        findNavController().navigate(R.id.action_regFragment_to_bottomNavigation)
                                    }
                                }
                                else
                                {
                                    Toast.makeText(
                                        context,
                                        "Введите фамилию, имя, отчество!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                            else
                            {
                                Toast.makeText(
                                    context,
                                    "Длинна пароля от 8 до 20 символов!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        else
                        {
                            Toast.makeText(context, "Длинна логина от 4 до 18 символов!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Данный логин занят!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                else
                {
                    Toast.makeText(context, "Логин пуст!", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener{
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }

}