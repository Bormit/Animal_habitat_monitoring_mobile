package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.viewmodel.MyViewModel


class SettingsFragment : Fragment() {
    private lateinit var viewModel: MyViewModel
    private lateinit var address: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        viewModel.setContext(requireContext())

        address = view.findViewById<EditText>(R.id.editIP)

        setAddressFromSharedPreferences()

        address.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                try {
                    val check = address.text.toString()
                    val len = check.length
                    if (len > 11){
                        val ipAddressPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$".toRegex()

                        val isValidIpAddress = check.matches(ipAddressPattern.toRegex())

                        if (isValidIpAddress) {
                            saveIpAddressToSharedPreferences(check)
//                            setAddressFromSharedPreferences()
                            Toast.makeText(context, "Адрес установлен $check", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (e: Exception){
                    Toast.makeText(context, "Неправильный ввод!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

//        val ipAddress = editIP.text.toString()
//
//        val ipAddressPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$".toRegex()
//
//        val isValidIpAddress = ipAddress.matches(ipAddressPattern)




        return view
    }
    fun saveIpAddressToSharedPreferences(ipAddress: String) {
        val context = requireContext()
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ip_address", ipAddress)
        editor.apply()
    }
    fun setAddressFromSharedPreferences(){
        val context = requireContext()
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val ipAdd = sharedPreferences.getString("ip_address", null)
        address.setText(ipAdd)
    }
}