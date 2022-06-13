package com.example.onigiri_restaurant_recommendation.ui.category

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.RestaurantAdapter
import com.example.onigiri_restaurant_recommendation.databinding.ActivityCategoryBinding
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoLocationBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.home.HomeViewModel
import com.example.onigiri_restaurant_recommendation.ui.result.ResultActivity
import com.example.onigiri_restaurant_recommendation.util.distanceInKm
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    companion object {
        const val CATEGORY_NAME = "Category"
        const val TAG = "CategoryActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swiperefreshcategory.setOnRefreshListener {
            binding.swiperefreshcategory.isRefreshing = false
        }

        setToolbar(intent.getStringExtra(CATEGORY_NAME))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()
        createLocationCallback()

        homeViewModel.location.observe(this) {
            lat = it.lat
            lon = it.long

            if(distanceInKm(lat, lon, -6.420387913713653, 106.80529182196888) > 50) {
                lat = -6.175392
                lon = 106.827153
            }

            Log.d(TAG, "Location: $lat, $lon")
            foodCategory(intent.getStringExtra(CATEGORY_NAME))
        }
    }

    private fun setToolbar(categoryName: String?) {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            title = categoryName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showRecyclerView() {
        showLoading(true)
        with(binding) {
            rvRestaurant.layoutManager = LinearLayoutManager(this@CategoryActivity)
            categoryViewModel.listRestaurant.observe(this@CategoryActivity) {
                showEmpty(it.isEmpty())
                val restaurantAdapter = RestaurantAdapter()
                restaurantAdapter.setData(it)
                binding.rvRestaurant.adapter = restaurantAdapter
                showLoading(false)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun foodCategory(category: String?){
        var foodcategory = ""
        when(category){
            "Sweets" -> foodcategory = "permen"
            "Rice" -> foodcategory = "nasi"
            "Meatball" -> foodcategory = "baso"
            "Chicken" -> foodcategory = "ayam"
            "Drinks" -> foodcategory = "minuman"
            "Coffee" -> foodcategory = "kopi"
            "Seafood" -> foodcategory = "makanan_laut"
            "Western" -> foodcategory = "makanan_barat"
            "Noodle" -> foodcategory = "mie"
            "Chinese" -> foodcategory = "cina"
            "Indian" -> foodcategory = "india"
            "Japanese" -> foodcategory = "jepang"
            "Middle East" -> foodcategory = "makanan_timur"
            "Thai" -> foodcategory = "thailand"
        }
        categoryViewModel.setSearchRestaurant(foodcategory, lat, lon)
        Log.d(TAG, "masukin $lat $lon")
        showRecyclerView()
    }

    private fun showLoading(state: Boolean) {
        binding.loading.root.isVisible = state
    }

    private fun showEmpty(state: Boolean) {
        binding.notfoundresult.isVisible = state
    }

    //------------------------------------- LOCATION -------------------------------------------

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
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLastLocation() {
        if (checkLocationPermission(this)) {
            if(isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        location.apply {
                            homeViewModel.setLocation(latitude, longitude)
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
        val client = LocationServices.getSettingsClient(this)
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
//                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
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
                    Log.d(TAG, "onActivityResult: All location settings are satisfied.")
                    getLastLocation()
                }
                RESULT_CANCELED -> {
                    val noLocationBottomSheet = NoLocationBottomSheet()
                    noLocationBottomSheet.show(supportFragmentManager, NoLocationBottomSheet.TAG)
                }
            }
        }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val latitude = locationResult.locations[0].latitude
                val longitude = locationResult.locations[0].longitude
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

}