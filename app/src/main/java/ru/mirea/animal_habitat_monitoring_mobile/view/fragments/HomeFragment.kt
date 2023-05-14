package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal
import java.lang.Math.*
import kotlin.math.pow


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
                    val arealList = createAreals(boundingPoints)
                    arealList.forEach { areal ->

                        val center = calcArealCentrePoint(areal)
                        val radius = calcArealRadius(center, areal)
                        drawCircleAreal(center, radius, areal)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    fun createAreals(arrayPoint: ArrayList<GeoPoint>): ArrayList<ArrayList<GeoPoint>>{
        // Создали пустой аккумулятор ответа
        val arealList = arrayListOf<ArrayList<GeoPoint>>()
        // Перебираем список входных точек
        for (point in arrayPoint){
            // Определяем ареал для входной точки или создаем новый, если такого нет
            val areal: ArrayList<GeoPoint> = getAreal(point, arealList)
            // Перебираем остальные точки из входящего списка
            for (otherPoint in arrayPoint){
                // Проверяем допустимость включения другой точки в наш ареал
                if(point.distanceToAsDouble(otherPoint) < 500){
                    // Проверяем что точка не включена в наш ареал, то мы ее добавляем
                    if(! hasPoint(otherPoint, areal)){
                        areal.add(otherPoint)
                    }
                }
            }
            arealList.add(areal)
        }
        return arealList
    }
    fun getAreal(point: GeoPoint, arealList: ArrayList<ArrayList<GeoPoint>>): ArrayList<GeoPoint>{
        var res: ArrayList<GeoPoint> = arrayListOf()
        for (areal in arealList){
            if (hasPoint(point, areal)){
                res =  areal
            }
        }
        return res
    }
    fun hasPoint(point: GeoPoint, areal: ArrayList<GeoPoint>): Boolean{
        return areal.contains(point)
    }
    fun calcArealCentrePoint(areal: ArrayList<GeoPoint>): GeoPoint{
        val latitude = areal.map { point -> point.latitude}.average()
        val longitude = areal.map { point -> point.longitude}.average()
        return GeoPoint(latitude, longitude)
    }
    fun calcArealRadius(center: GeoPoint, areal: ArrayList<GeoPoint>): Double{
        var maxDistance = 0.0
        for (point in areal){
            if (center.distanceToAsDouble(point) > maxDistance){
                maxDistance = center.distanceToAsDouble(point)
            }
        }
        return maxDistance + 50
    }
    fun drawCircleAreal(center: GeoPoint, radius: Double, areal: ArrayList<GeoPoint>){
        if(areal.size > 1){
            val numberOfPoints = 100
            val points = ArrayList<GeoPoint>()

            // Создание точек, составляющих круговую границу
            for (i in 0 until numberOfPoints) {
                val angle = Math.PI * 2 * (i.toDouble() / numberOfPoints.toDouble())
                val lat = center.latitude + radius / 111111.0 * Math.cos(angle)
                val lon = center.longitude + radius / (111111.0 * Math.cos(center.latitude * Math.PI / 180.0)) * Math.sin(angle)
                val point = GeoPoint(lat, lon)
                val finalPoint = correctPoint(point, areal)
                points.add(finalPoint)
            }

            val circle = Polygon()
            circle.points = points
            circle.fillColor = Color.parseColor("#150000ff") // Пример цвета заполнения (полупрозрачный красный)
            circle.strokeColor = Color.BLUE // Пример цвета обводки (красный)
            circle.strokeWidth = 1f // Толщина обводки

            mapView.overlays.add(circle) // Добавляем круговое наложение на карту
            mapView.invalidate() // Обновляем карту для отображения изменений
        }
    }
    fun correctPoint(point: GeoPoint, areal: ArrayList<GeoPoint>): GeoPoint{
        var minDistance = 100000.0
        var nearPoint = point
        for (otherPoint in areal){
            if (point.distanceToAsDouble(otherPoint) < minDistance){
                minDistance = point.distanceToAsDouble(otherPoint)
                nearPoint = otherPoint
            }
        }
        return reduceDistance(point, nearPoint, 50.0)
    }
    fun reduceDistance(startPoint: GeoPoint, endPoint: GeoPoint, distance: Double): GeoPoint {
        val lat1 = startPoint.latitude * Math.PI / 180
        val lon1 = startPoint.longitude * Math.PI / 180
        val lat2 = endPoint.latitude * Math.PI / 180
        val lon2 = endPoint.longitude * Math.PI / 180

        val d = 2 * asin(sqrt(sin((lat2 - lat1) / 2).pow(2) + cos(lat1) * cos(lat2) * sin((lon2 - lon1) / 2).pow(2)))
        val bearing = atan2(sin(lon2 - lon1) * cos(lat2), cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1))

        val reducedDistance = d - distance / 6371000.0 // 6371000.0 - средний радиус Земли в метрах

        val lat3 = asin(sin(lat1) * cos(reducedDistance) + cos(lat1) * sin(reducedDistance) * cos(bearing))
        val lon3 = lon1 + atan2(sin(bearing) * sin(reducedDistance) * cos(lat1), cos(reducedDistance) - sin(lat1) * sin(lat3))

        val newLatitude = lat3 * 180 / Math.PI
        val newLongitude = lon3 * 180 / Math.PI

        return GeoPoint(newLatitude, newLongitude)
    }
}