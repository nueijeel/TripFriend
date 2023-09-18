package com.test.tripfriend.ui.home

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
//        val mapFragment = fm.findFragmentById(R.id.mapViewHomeMap) as MapFragment?
//            ?: MapFragment.newInstance().also {
////                fm.beginTransaction().add(R.id.mapViewHomeMap, it).commit()
//            }
//        mapFragment.getMapAsync(this)

        val mapFragment = fm.findFragmentById(R.id.mapViewHomeMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return fragmentHomeMapBinding.root

    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0


    }

    // 마커 추가
//    private fun openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
//            val markerLatLng = likelyPlaceLatLngs[which]
//            var markerSnippet = likelyPlaceAddresses[which]
//            if (likelyPlaceAttributions[which] != null) {
//                markerSnippet = """
//                $markerSnippet
//                ${likelyPlaceAttributions[which]}
//                """.trimIndent()
//            }
//
//            if (markerLatLng == null) {
//                return@OnClickListener
//            }
//
//            // Add a marker for the selected place, with an info window
//            // showing information about that place.
//            googleMap?.addMarker(MarkerOptions()
//                .title(likelyPlaceNames[which])
//                .position(markerLatLng)
//                .snippet(markerSnippet))
//
//            // Position the map's camera at the location of the marker.
//            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                DEFAULT_ZOOM.toFloat()))
//        }
//
//        // Display the dialog.
//        AlertDialog.Builder(mainActivity)
//            .setTitle(R.string.pick_place)
//            .setItems(likelyPlaceNames, listener)
//            .show()
//    }

}