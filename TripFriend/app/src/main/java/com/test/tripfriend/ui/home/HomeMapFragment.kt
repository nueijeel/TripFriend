package com.test.tripfriend.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.type.LatLng
import com.test.tripfriend.BuildConfig
import com.test.tripfriend.R
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.databinding.FragmentHomeMapBinding

class HomeMapFragment : Fragment(), OnMapReadyCallback {
    lateinit var fragmentHomeMapBinding: FragmentHomeMapBinding
    lateinit var mainActivity: MainActivity
    var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeMapBinding = FragmentHomeMapBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        val MAPS_API_KEY = BuildConfig.MAPS_API_KEY

        // Initialize the SDK
        Places.initialize(context, MAPS_API_KEY)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(context)

        val fm = mainActivity.supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapViewHomeMap) as MapFragment?
            ?: MapFragment.newInstance().also {
//                fm.beginTransaction().add(R.id.mapViewHomeMap, it).commit()
            }
        mapFragment.getMapAsync(this)

//        val mapFragment = mainActivity.supportFragmentManager.findFragmentById(R.id.mapViewHomeMap) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        return fragmentHomeMapBinding.root

    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        val SEOUL = com.google.android.gms.maps.model.LatLng(37.556, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국 수도")

        googleMap?.addMarker(markerOptions)

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f))
    }
}