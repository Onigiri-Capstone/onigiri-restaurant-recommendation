package com.example.onigiri_restaurant_recommendation.ui.home

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.FragmentHomeBinding
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoLocationBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.camera.CameraActivity
import com.example.onigiri_restaurant_recommendation.ui.category.CategoryActivity
import com.example.onigiri_restaurant_recommendation.ui.home.category.CategoryBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.home.location.LocationBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.profile.ProfileActivity
import com.example.onigiri_restaurant_recommendation.ui.result.ResultActivity
import com.example.onigiri_restaurant_recommendation.ui.survey.SurveyActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var address: String = "Location not found"

    private val homeViewModel: HomeViewModel by viewModels()

    private var lat: Double = 0.0
    private var long: Double = 0.0

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        if(!checkLocationPermission(requireContext())) {
            requestLocationPermissionLauncher.launch(
                REQUIRED_LOCATION_PERMISSIONS
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLocationRequest()
        createLocationCallback()

        homeViewModel.location.observe(viewLifecycleOwner) {
            showGPSControl(it == null)
            lat = it.lat
            long = it.long
        }

        setOnClickListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            btnGps.setOnClickListener(this@HomeFragment)
            tvLocation.setOnClickListener(this@HomeFragment)
            tvInput.setOnClickListener(this@HomeFragment)
            btnMore.setOnClickListener(this@HomeFragment)
            btnCamera.setOnClickListener(this@HomeFragment)
            btnSweets.setOnClickListener(this@HomeFragment)
            btnRice.setOnClickListener(this@HomeFragment)
            btnMeatball.setOnClickListener(this@HomeFragment)
            btnChicken.setOnClickListener(this@HomeFragment)
            btnDrinks.setOnClickListener(this@HomeFragment)
            btnCoffee.setOnClickListener(this@HomeFragment)
            btnSeafood.setOnClickListener(this@HomeFragment)
            ivProfile.setOnClickListener(this@HomeFragment)
            swiperefreshhome.setOnRefreshListener {
                swiperefreshhome.isRefreshing = false
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_gps -> {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            R.id.btn_camera -> {
                startActivity(Intent(activity, CameraActivity::class.java))
                activity?.onBackPressed()
            }
            R.id.tv_location -> {
                val locationBottomSheet = LocationBottomSheet().getInstance(address)
                locationBottomSheet.show(parentFragmentManager, LocationBottomSheet.TAG)
            }
            R.id.tv_input -> {
              startActivity(Intent(activity, ResultActivity::class.java))

            }
            R.id.btn_more -> {
                val categoryBottomSheet = CategoryBottomSheet()
                categoryBottomSheet.show(parentFragmentManager, CategoryBottomSheet.TAG)
            }
            R.id.btn_sweets -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.sweets))
                )
            }
            R.id.btn_rice -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.rice))
                )
            }
            R.id.btn_meatball -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.meatball))
                )
            }
            R.id.btn_chicken -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.chicken))
                )
            }
            R.id.btn_drinks -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.drinks))
                )
            }
            R.id.btn_coffee -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.coffee))
                )
            }
            R.id.btn_seafood -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.seafood))
                )
            }
            R.id.iv_profile -> {
                startActivity(Intent(activity, ProfileActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------------------------------------LOCATION ---------------------------------------------


    private val REQUIRED_LOCATION_PERMISSIONS
            = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private fun checkLocationPermission(context: Context): Boolean {
        return REQUIRED_LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLastLocation()
                }
                else -> {
                    val noLocationBottomSheet = NoLocationBottomSheet()
                    noLocationBottomSheet.show(parentFragmentManager, NoLocationBottomSheet.TAG)
                }
            }
        }

    private fun getLastLocation() {
        if (checkLocationPermission(requireContext())){
            if(isLocationEnabled()){
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        showGPSControl(false)
                        location.apply {
                            homeViewModel.setLocation(latitude, longitude)
                            address = geocoder.getFromLocation(latitude, longitude, 1)[0].getAddressLine(0)
                        }
                        Log.d(TAG, "Location is not null")
                    } else {
                        Log.d(TAG, "Location not found, create location callback")
                        startLocationUpdates()
                    }
                }
            } else {
                createLocationRequest()
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireContext())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getLastLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(requireContext(), sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                    getLastLocation()
                }
                RESULT_CANCELED ->
                    showGPSControl(true)
            }
        }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val latitude = locationResult.locations[0].latitude
                val longitude = locationResult.locations[0].longitude
                address = geocoder.getFromLocation(latitude, longitude, 1)[0].getAddressLine(0)
                homeViewModel.setLocation(latitude, longitude)
                Log.d(TAG, "onLocationResult: $latitude, $longitude")
                stopLocationUpdates()
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun showGPSControl(boolean: Boolean) {
        binding.location.isVisible = boolean
    }

}
