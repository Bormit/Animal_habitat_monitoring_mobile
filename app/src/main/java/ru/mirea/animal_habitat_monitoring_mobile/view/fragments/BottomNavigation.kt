package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mirea.animal_habitat_monitoring_mobile.R


class BottomNavigation : Fragment() {
    private var fragment: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false)
        val menu = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (savedInstanceState == null){
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.containerFragment,HomeFragment())
                ?.commit()
        }


        when(fragment){
            1 ->{
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.containerFragment,HomeFragment())
                    ?.commit()
                menu.selectedItemId = R.id.homeFragment
            }
            2 ->{
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.containerFragment,CreateOfForm())
                    ?.commit()
                menu.selectedItemId = R.id.listOfForm
            }
            3 ->{
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.containerFragment,InfoOfApp())
                    ?.commit()
                menu.selectedItemId = R.id.infoOfApp
            }
            4 ->{
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                val login = sharedPref?.getString("login", "Пользователь")
                if (login != "Пользователь"){
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,UserProfile())
                        ?.commit()
                }else{
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,Profile())
                        ?.commit()
                }
                menu.selectedItemId = R.id.profileUser
            }
            5 ->{
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.containerFragment,SettingsFragment())
                    ?.commit()
                menu.selectedItemId = R.id.settingsFragment
            }
        }

        menu.selectedItemId = R.id.homeFragment

        menu.setOnNavigationItemSelectedListener {

            when(it.itemId){
                R.id.homeFragment -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,HomeFragment())
                        ?.commit()
                    fragment = 1
                }
                R.id.listOfForm -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,CreateOfForm())
                        ?.commit()
                    fragment = 2
                }
                R.id.infoOfApp -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,InfoOfApp())
                        ?.commit()
                    fragment = 3
                }
                R.id.profileUser -> {
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    val login = sharedPref?.getString("login", "Пользователь")
                    if (login != "Пользователь"){
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.containerFragment,UserProfile())
                            ?.commit()
                    }else{
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.containerFragment,Profile())
                            ?.commit()
                    }
                    fragment = 4
                }
                R.id.settingsFragment -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.containerFragment,SettingsFragment())
                        ?.commit()
                    fragment = 5
                }
            }
            true
        }

        return view
    }
}
