package com.test.tripfriend.ui.trip

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentAccompanyRegister1Binding
import com.test.tripfriend.databinding.FragmentModifyPostBinding

class ModifyPostFragment : Fragment(), OnMapReadyCallback {
    lateinit var fragmentModifyPostBinding: FragmentModifyPostBinding
    lateinit var mainActivity: MainActivity

    var country: String? = ""
    var locality: String? = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private var mapFragment: SupportMapFragment? = null
    private lateinit var coordinates: LatLng
    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var startAutocompleteIntentListener = View.OnClickListener { view: View ->
        view.setOnClickListener(null)
        startAutocompleteIntent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyPostBinding = FragmentModifyPostBinding.inflate(layoutInflater)

        // search icon
        fragmentModifyPostBinding.iconButtonModifyPost.setOnClickListener(
            startAutocompleteIntentListener
        )

        // 바텀 네비 GONE
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        // 이전 화면 정보 가져오기
        val tripPostDocumentId = arguments?.getString("postId")!!
        val tripPostWriterEmail = arguments?.getString("tripPostWriterEmail")
        val tripPostLocationName = arguments?.getString("locationName")
        val tripPostLatitude = arguments?.getDouble("latitude")
        val tripPostLongitude = arguments?.getDouble("longitude")

        fragmentModifyPostBinding.run {

            val mapFragment = childFragmentManager.findFragmentById(R.id.mapViewModifyPost) as SupportMapFragment?

            if (tripPostLatitude != null && tripPostLongitude != null) {
                coordinates = LatLng(tripPostLatitude, tripPostLongitude)
                mapFragment?.getMapAsync(this@ModifyPostFragment)
            }

            if(country == "") {
                toolbarTextModifyPost.text = tripPostLocationName
            } else {
                toolbarTextModifyPost.text = "$country $locality"
                coordinates = LatLng(latitude, longitude)
                mapFragment?.getMapAsync(this@ModifyPostFragment)
            }

            materialToolbarModifyPost.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODFY_POST_FRAGMENT)
                }
            }



            buttonModifyPostToNextView.setOnClickListener {
                // 다음 화면으로 정보 전달
                val newBundle = Bundle()
                newBundle.putString("tripPostDocumentId", tripPostDocumentId)
                newBundle.putString("tripPostWriterEmail", tripPostWriterEmail)

                if(country == "") {
                    newBundle.putString("country", tripPostLocationName)
                    if(tripPostLatitude != null && tripPostLongitude != null) {
                        newBundle.putDouble("latitude", tripPostLatitude.toDouble())
                        newBundle.putDouble("longitude", tripPostLongitude.toDouble())
                    }
                } else {
                    newBundle.putString("country", country + " " + locality)
                    newBundle.putDouble("latitude", latitude)
                    newBundle.putDouble("longitude", longitude)
                }

                mainActivity.replaceFragment(MainActivity.MODFY_POST2_FRAGMENT,true,true, newBundle)
            }
        }

        return fragmentModifyPostBinding.root
    }

    override fun onPause() {
        super.onPause()
//        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }

    // autocomplete 정의
    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        fragmentModifyPostBinding.iconButtonModifyPost.setOnClickListener(
            startAutocompleteIntentListener
        )

        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                val place = Autocomplete.getPlaceFromIntent(intent)

                // Place의 주소 구성요소
                fillInAddress(place)
            }
        } else if (result.resultCode == AppCompatActivity.RESULT_CANCELED) {
            // 작업 취소
        }
    }

    // autocomplete 인텐트
    private fun startAutocompleteIntent() {
        // 사용자가 선택한 후 반환할 장소 데이터 유형 지정
        val fields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG
        )

        // 필드, 국가 및 유형 필터가 적용된 자동 완성 인텐트 구축
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            fields
        )
            .setTypesFilter(listOf(PlaceTypes.ADDRESS))
            .setTypesFilter(listOf(PlaceTypes.CITIES))
            .build(mainActivity)
        startAutocomplete.launch(intent)
    }

    private fun fillInAddress(place: Place) {

        val components = place.addressComponents


        if (components != null) {
            for (component in components.asList()) {
                when (component.types[0]) {
                    "country" -> {
                        country = component.name
                    }

                    "locality" -> {
                        locality = component.name
                    }
                }
            }
        }
        fragmentModifyPostBinding.toolbarTextModifyPost.setText(country + " " + locality)

        // 지도 추가
        showMap(place)
    }

    // autocomplete 맵 추가
    private fun showMap(place: Place) {
        coordinates = place.latLng as LatLng
        latitude = coordinates.latitude
        longitude = coordinates.longitude

        // 태그 설정하여 검색
        mapFragment =
            mainActivity.supportFragmentManager.findFragmentByTag("MAP") as SupportMapFragment?

        // 아직 존재하지 않는 경우에만 프래그먼트 생성
        if (mapFragment == null) {
            val mapOptions = GoogleMapOptions()
            mapOptions.mapToolbarEnabled(false)

            mapFragment = SupportMapFragment.newInstance(mapOptions)

            // FragmentTransaction 사용하여 추가
            mainActivity.supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.mapViewModifyPost,
                    mapFragment!!,
                    "MAP"
                )
                .commit()
            mapFragment?.getMapAsync(this)
        } else {
            updateMap(coordinates)
        }
    }

    private fun updateMap(latLng: LatLng) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
        marker = map?.addMarker(MarkerOptions().position(coordinates))
    }

    // autocomplete 맵 준비
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        try {
            // 정의된 JSON 객체를 사용하여 기본 지도의 스타일을 맞춤설정
            val success = map!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(mainActivity, R.raw.style_json)
            )
            if (!success) {
            }
        } catch (e: Resources.NotFoundException) {
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
        marker = map?.addMarker(MarkerOptions().position(coordinates))
    }

    override fun onStop() {
        super.onStop()
    }

}
