package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal


class HomeFragment : Fragment() {
    private lateinit var spinnerArrayFamily: Array<String>
    private var spinnerAreal: Spinner? = null
    private lateinit var mapView: MapView
    private val homeFragment = this
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)





        val context = requireContext() // получаем контекст фрагмента

        spinnerAreal = view.findViewById<Spinner>(R.id.spinnerAreal)
        spinnerArrayFamily = resources.getStringArray(R.array.spinnerAnimals)






        Configuration.getInstance().userAgentValue = context.packageName

        val geoPoint = GeoPoint(55.670091935852895, 37.48028786111761)

        // Найти MapView
        mapView = view.findViewById(R.id.mapView)

        // Настройте MapView, например, установите центр и масштаб карты
        mapView.setMultiTouchControls(true)
        mapView.isTilesScaledToDpi = true
        mapView.controller.animateTo(geoPoint)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(geoPoint)

        getSpinnerAreal()
        return view
    }

    private fun getSpinnerAreal(){
        val arrayAdapterAnimals =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, spinnerArrayFamily) }
        spinnerAreal!!.adapter = arrayAdapterAnimals
        spinnerAreal!!.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinnerArrayFamily[p2] != "None") {
                    Toast.makeText(context, spinnerArrayFamily[p2], Toast.LENGTH_SHORT)
                        .show()
                    val animal = spinnerArrayFamily[p2]
                    drawMarker(animal)
                }
                else{
                    clearMarker()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }
    private fun drawPoint(animal: Animal){
//        getUserData()
        // Создаем места для отображения на карте
//        val place = Animal(55.670091935852895, decimalLongitude)

        // Добавляем маркеры на карту для каждого места
        val latitude = animal.latitude
        val longitude = animal.longitude
        val species = animal.species
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.icon = resources.getDrawable(R.drawable.ic_dot)
        marker.title = species
        mapView.overlays.add(marker)


        // Центрируем карту на первом месте
        mapView.controller.setCenter(GeoPoint(latitude, longitude))
        mapView.controller.setZoom(15.0)
    }

    private fun clearMarker(){
        mapView.overlays.clear()
        mapView.invalidate()
    }
    private fun drawAreal(boundingPoints: ArrayList<GeoPoint>){
        val polygon = Polygon().apply {
            val color = Color.argb(150, 0, 0, 255)
            fillColor = color
            strokeColor = Color.RED
            strokeWidth = 5f

        }

        // Установка точек обводки
        polygon.setPoints(boundingPoints)

        // Добавление обводки на карту
        mapView.overlays.add(polygon)

        mapView.invalidate()

    }
    private fun drawMarker(animalSpinner: String) {
        val dbref = FirebaseDatabase.getInstance().getReference("animal")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("database", "get data: $snapshot")

                if (snapshot.exists() && homeFragment.isVisible) {

                    clearMarker()
                    var boundingPoints = ArrayList<GeoPoint>()

                    for (userSnapshot in snapshot.children) {


                        val latitude = userSnapshot.child("latitude").getValue(Double::class.java)
                        val longitude = userSnapshot.child("longitude").getValue(Double::class.java)
                        val species = userSnapshot.child("species").getValue(String::class.java)
                        val time = userSnapshot.child("time").getValue(String::class.java)
                        val animal = Animal(latitude!!, longitude!!, species!!, time!!)
                        if(species == animalSpinner){
                            drawPoint(animal)
                            val geoPoint = GeoPoint(latitude, longitude)
                            boundingPoints.add(geoPoint)
                        }
                        // TODO: Сделать обертку данных
                    }
                    drawAreal(boundingPoints)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
//    fun createAreals(arrayPoint: ArrayList<GeoPoint>): ArrayList<ArrayList<GeoPoint>>{
//
//    }
}