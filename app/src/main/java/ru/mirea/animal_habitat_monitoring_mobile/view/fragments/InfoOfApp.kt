package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
        val infoAnimal4 = view.findViewById<TextView>(R.id.infoAnimal4)
        val infoAnimal5 = view.findViewById<TextView>(R.id.infoAnimal5)

        val animal1 = view.findViewById<ImageView>(R.id.imageView)
        val animal2 = view.findViewById<ImageView>(R.id.imageView2)
        val animal3 = view.findViewById<ImageView>(R.id.imageView3)
        val animal4 = view.findViewById<ImageView>(R.id.imageView4)
        val animal5 = view.findViewById<ImageView>(R.id.imageView5)


        animal1.setImageResource(R.drawable.weasel)
        animal2.setImageResource(R.drawable.duck)
        animal3.setImageResource(R.drawable.woodpecker)
        animal4.setImageResource(R.drawable.owl)
        animal5.setImageResource(R.drawable.hedgehog)

        val animal1Link = "<a href=\"https://bigenc.ru/c/laska-4f703e\">Подробнее..</a>"
        val animal2Link = "<a href=\"https://moscowzoo.ru/animals/guseobraznye/kryakva/?sphrase_id=693663\">Подробнее..</a>"
        val animal3Link = "<a href=\"https://birdsrussia.org/about/articles/e-d-krasnova-bolshoy-pestryy-dyatel/\">Подробнее..</a>"
        val animal4Link = "<a href=\"https://bigenc.ru/c/sovinye-f14c5c\">Подробнее..</a>"
        val animal5Link = "<a href=\"https://moscowzoo.ru/animals/nasekomoyadnye/yezh-obyknovennyy/\">Подробнее..</a>"

        infoAnimal1.text = HtmlCompat.fromHtml("<h1>Ласка</h1>\n" +
                "    <p>Ласка это небольшое млекопитающее, принадлежащее к семейству хорьковых.\n" +
                "    Они обладают пушистым мехом, плавным движением и характерными черными полосками на лице.\n" +
                "    Ласка не боится человека, при встрече с ним она отбегает на небольшое расстояние и встаёт на задние лапы.</p>" +
                "    <h1>$animal1Link</h1>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        infoAnimal2.text = HtmlCompat.fromHtml( "<h2>Утка</h2>\n" +
                "    <p>Утки — это птицы средних и небольших размеров с относительно короткой шеей и цевкой, \n" +
                "    покрытой спереди поперечными щитками. Окраска оперения разнообразна, \n" +
                "    у многих видов на крыле имеется особое «зеркальце»</p>" +
                "    <h2>$animal2Link</h2>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        infoAnimal3.text = HtmlCompat.fromHtml("<h3>Большой пёстрый дятел</h3>\n" +
                "    <p>Большой пёстрый дятел - это птица размером с дрозда. Шапочка только у самца, \n" +
                "    и она мелкая - располагается на затылке. У этого вида нет пестрин на груди и брюхе. Спина черная. \n" +
                "    Образ жизни большинства дятлов оседлый.</p>" +
                "    <h3>$animal3Link</h3>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        infoAnimal4.text = HtmlCompat.fromHtml("<h4>Большая рогатая сова</h4>\n" +
                "    <p>Большая рогатая сова - вид хищных птиц из семейства совиных или настоящих сов, \n" +
                "    широко распространён на территориях обеих Америк. Из-за перьевых ушек получил прозвище «Большой рогатый филин» \n" +
                "    Эти совы охотятся почти на любую живность, кроме крупных млекопитающих. \n" +
                "    Встречается как в лесах, так и в степях.</p>" +
                "    <h4>$animal4Link</h4>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        infoAnimal5.text = HtmlCompat.fromHtml("<h5>Ёж</h5>\n" +
                "    <p>Ёж - вид млекопитающих из рода евразийских ежей семейства ежовых. \n" +
                "    Длина его тела составляет 20—30 см, хвоста — около 3 см масса тела — 700—800 г,\n" +
                "    уши относительно небольшие (обычно меньше 3,5 см). \n" +
                "    Между иглами располагаются тонкие, длинные, очень редкие волосы.</p>" +
                "    <h5>$animal5Link</h5>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Применяем кликабельные ссылки
        infoAnimal1.movementMethod = LinkMovementMethod.getInstance()
        infoAnimal2.movementMethod = LinkMovementMethod.getInstance()
        infoAnimal3.movementMethod = LinkMovementMethod.getInstance()
        infoAnimal4.movementMethod = LinkMovementMethod.getInstance()
        infoAnimal5.movementMethod = LinkMovementMethod.getInstance()

        return view
    }
}