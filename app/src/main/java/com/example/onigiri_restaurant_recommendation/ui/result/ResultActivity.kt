@file:Suppress("DEPRECATION")

package com.example.onigiri_restaurant_recommendation.ui.result

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.RestaurantAdapter
import com.example.onigiri_restaurant_recommendation.databinding.ActivityResultBinding
import com.example.onigiri_restaurant_recommendation.ui.camera.CameraActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainViewModel
import com.example.onigiri_restaurant_recommendation.util.location.LocationUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val resultViewModel: ResultViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val restaurantAdapter = RestaurantAdapter()

    private var lon: Double = 0.0
    private var lat: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getMyLastLocation()
        setToolbar()
        setOnClickListener()
        showRecyclerView()
        setSearch()
        setFoodLabel()

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val locationUtil = LocationUtil(this, this, supportFragmentManager, mainViewModel)
        locationUtil.subscribeToLocationPermissionListener()
        locationUtil.subscribeToGpsListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            btnCamera.setOnClickListener {
                startActivity(Intent(this@ResultActivity, CameraActivity::class.java))
            }
            swiperefreshresult.setOnRefreshListener {
                binding.swiperefreshresult.isRefreshing = false
                showRecyclerView()
            }
        }
    }

    private fun setSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                    showEmpty(false)
                    showLoading(true)
                    resultViewModel.setSearchRestaurant(query, lat, lon)
                    Log.e("onQueryTextSubmit: ","textt" )

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.rvRestaurant.isVisible = false
                showEmpty(false)
                return false
            }
        })
    }

    private fun showRecyclerView() {
        with(binding) {
            rvRestaurant.layoutManager = LinearLayoutManager(this@ResultActivity)
            resultViewModel.getSearchRestaurant().observe(this@ResultActivity) {
                showEmpty(it.isEmpty())
                restaurantAdapter.setData(it)
                binding.rvRestaurant.adapter = restaurantAdapter
                Log.e("showRecyclerView: ",it.toString() )
                showLoading(false)
                binding.rvRestaurant.isVisible = true
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLastLocation()
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lon = location.longitude
                    lat = location.latitude
                    Log.e("getMyLastLocation: ", "$lon $lat")
                } else {
                    Toast.makeText(
                        this@ResultActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
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

    private fun setFoodLabel() {
        val foodname = intent?.getStringExtra(FOOD_NAME)
        if (foodname.isNullOrEmpty()) {
            binding.search.requestFocus()
        } else {
            binding.search.setQuery(foodname, false)
            resultViewModel.setSearchRestaurant(foodname, lat, lon)
            Log.e("onQueryTextSubmit: ", "")
            showRecyclerView()
        }
    }

    companion object {
        const val FOOD_NAME = ""
    }
}