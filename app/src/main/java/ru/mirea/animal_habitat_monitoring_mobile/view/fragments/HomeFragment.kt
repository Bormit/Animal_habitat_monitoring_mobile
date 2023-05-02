package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import ru.mirea.animal_habitat_monitoring_mobile.R


class HomeFragment : Fragment() {
    private lateinit var mapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val context = requireContext() // получаем контекст фрагмента

        val searchView: SearchView = view.findViewById(R.id.searchAreal)

        searchView.clearFocus()

        Configuration.getInstance().userAgentValue = context.packageName

        val geoPoint = GeoPoint(55.75249280754598, 37.6329600194683)

        // Найти MapView
        mapView = view.findViewById(R.id.mapView)

        // Настройте MapView, например, установите центр и масштаб карты
        mapView.setMultiTouchControls(true)
        mapView.controller.animateTo(geoPoint)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(geoPoint)

        return view
    }
}