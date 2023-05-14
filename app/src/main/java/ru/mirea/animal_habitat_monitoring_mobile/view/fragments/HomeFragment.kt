package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import ru.mirea.animal_habitat_monitoring_mobile.R
import ru.mirea.animal_habitat_monitoring_mobile.model.db.DatabaseConnection
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal


class HomeFragment : Fragment() {
    private lateinit var mapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val settings = view.findViewById<ImageView>(R.id.settings)

        settings.setOnClickListener {
            findNavController().navigate(R.id.action_bottomNavigation_to_settingsFragment)
        }

        val context = requireContext() // получаем контекст фрагмента

        val searchView: SearchView = view.findViewById(R.id.searchAreal)

        searchView.clearFocus()

        Configuration.getInstance().userAgentValue = context.packageName

        val geoPoint = GeoPoint(55.75249280754598, 37.6329600194683)

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

        setPoint()

        return view
    }
    private fun setPoint(){
//        getUserData()
        // Создаем места для отображения на карте
//        val place = Animal(55.670091935852895, decimalLongitude)

        // Добавляем маркеры на карту для каждого места
        val marker = Marker(mapView)
        marker.position = GeoPoint(55.670091935852895, 37.48028786111761)
        marker.title = "test"
        mapView.overlays.add(marker)


        // Центрируем карту на первом месте
        mapView.controller.setCenter(GeoPoint(55.670091935852895, 37.48028786111761))
        mapView.controller.setZoom(15.0)
    }
    private fun getUserData() {
        val dbref = FirebaseDatabase.getInstance().getReference("animal")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("database", "get data: $snapshot")

                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {


                        val animal = userSnapshot.getValue(Animal::class.java)

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}