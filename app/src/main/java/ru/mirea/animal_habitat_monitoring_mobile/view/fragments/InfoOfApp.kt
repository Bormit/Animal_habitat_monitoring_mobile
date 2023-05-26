package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.mirea.animal_habitat_monitoring_mobile.R

class InfoOfApp : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info_of_app, container, false)

        val infoAnimal1 = view.findViewById<TextView>(R.id.infoAnimal1)
        val infoAnimal2 = view.findViewById<TextView>(R.id.infoAnimal2)
        val infoAnimal3 = view.findViewById<TextView>(R.id.infoAnimal3)

        infoAnimal1.text = Html.fromHtml("<h1>Ласка</h1>\n" +
                "    <p>Ласка это небольшое млекопитающее, принадлежащее к семейству хорьковых.\n" +
                "    Они обладают пушистым мехом, плавным движением и характерными черными полосками на лице.</p>")
        infoAnimal2.text = Html.fromHtml( "<h2>Утка</h2>\n" +
                "    <p>Утки — это птицы средних и небольших размеров с относительно короткой шеей и цевкой, \n" +
                "    покрытой спереди поперечными щитками. Окраска оперения разнообразна, \n" +
                "    у многих видов на крыле имеется особое «зеркальце»</p>")
        infoAnimal3.text = Html.fromHtml("<h3>Большой пёстрый дятел</h3>\n" +
                "    <p>Большой пёстрый дятел - это птица размером с дрозда. Шапочка только у самца, \n" +
                "    и она мелкая - располагается на затылке. У этого вида нет пестрин на груди и брюхе. Спина черная. \n" +
                "    Образ жизни большинства дятлов оседлый.</p>")

        return view
    }
}