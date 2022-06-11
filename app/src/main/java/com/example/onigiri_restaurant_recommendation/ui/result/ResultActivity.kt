@file:Suppress("DEPRECATION")

package com.example.onigiri_restaurant_recommendation.ui.result

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.RestaurantAdapter
import com.example.onigiri_restaurant_recommendation.data.local.entity.FavoriteRestaurantLocal
import com.example.onigiri_restaurant_recommendation.databinding.ActivityResultBinding
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoLocationBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.camera.CameraActivity
import com.example.onigiri_restaurant_recommendation.ui.favorite.FavoriteRestaurantViewModelFactory
import com.example.onigiri_restaurant_recommendation.ui.favorite.FavoriteViewModel
import com.example.onigiri_restaurant_recommendation.ui.home.HomeViewModel
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val resultViewModel: ResultViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val restaurantAdapter = RestaurantAdapter()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    private var lon: Double = 0.0
    private var lat: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFoodLabel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        favoriteViewModel = ViewModelProvider(this, FavoriteRestaurantViewModelFactory(application))[FavoriteViewModel::class.java]
        favoriteViewModel.getAllFavoriteRestaurant()
            .observe(this@ResultActivity) {
                restaurantAdapter.setDataRestaurantFav(it)

            }

        createLocationRequest()
        createLocationCallback()

        homeViewModel.location.observe(this@ResultActivity) {
            lat = it.lat
            lon = it.long
            Log.d(TAG, "Location: $lat, $lon")
        }


        setToolbar()
        setOnClickListener()
        showRecyclerView()
        setSearch()


        //Delete
        restaurantAdapter.delFavButton().observe(this) {
            deleteFavoriteUser(it)
        }
        //Insert
        restaurantAdapter.addFavButton().observe(this) {
            insertFavoriteUser(it)
        }


        setFoodLabel()
    }

    private fun setOnClickListener() {
        with(binding) {
            btnCamera.setOnClickListener {
                startActivity(Intent(this@ResultActivity, CameraActivity::class.java))
            }
        }
    }

    private fun setSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showEmpty(false)
                showLoading(true)
                restaurantAdapter.searchRestaurant(query)
                resultViewModel.setSearchRestaurant(query, lat, lon)
                Log.e("onQueryTextSubmit: ", query)

                if(query.length > 1) {
                    showEmpty(false)
                    showLoading(true)
                    resultViewModel.setSearchRestaurant(query, lat, lon)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.rvRestaurant.isVisible = false
                showEmpty(false)
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerView() {
        with(binding) {
            rvRestaurant.layoutManager = LinearLayoutManager(this@ResultActivity)
            resultViewModel.getSearchRestaurant().observe(this@ResultActivity) {
                showEmpty(it.isEmpty())
                restaurantAdapter.setData(it)

                binding.rvRestaurant.adapter = restaurantAdapter

                Log.d(TAG, it.toString())
                showLoading(false)
                binding.rvRestaurant.isVisible = true
            }
        }

        restaurantAdapter.notifyDataSetChanged()
    }

    private fun setToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.loading.root.isVisible = state
    }

    private fun showEmpty(state: Boolean) {
        binding.notfoundresult.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this@ResultActivity, MainActivity::class.java))
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ResultActivity, MainActivity::class.java))
    }

    private fun setFoodLabel(): Boolean {
        val restaurantName = intent?.getStringExtra(FOOD_NAME)
        if (restaurantName.isNullOrEmpty()) {
            binding.search.requestFocus()
        } else {

            binding.search.setQuery(restaurantName, true)

            resultViewModel.setSearchRestaurant(restaurantName, lat, lon)
            Log.e("onQueryTextSubmit: ", restaurantName)

            showRecyclerView()
        }
        return true
    }

    private fun insertFavoriteUser(it: FavoriteRestaurantLocal) {

        favoriteViewModel.insert(it)

        Toast.makeText(
            this,
            "${it.name} has added to favorite users",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteFavoriteUser(it: FavoriteRestaurantLocal) {
        favoriteViewModel.delete(it)

        Toast.makeText(
            this,
            "${it.name} has removed from favorite users",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val TAG = "ResultActivity"
        const val FOOD_NAME = ""
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
        val locationManager = this@ResultActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLastLocation() {
        if (checkLocationPermission(this@ResultActivity)) {
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
        val client = LocationServices.getSettingsClient(this@ResultActivity)
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
                        Toast.makeText(this@ResultActivity, sendEx.message, Toast.LENGTH_SHORT).show()
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