package com.example.onigiri_restaurant_recommendation.ui.result

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.RestaurantAdapter
import com.example.onigiri_restaurant_recommendation.databinding.ActivityResultBinding
import com.example.onigiri_restaurant_recommendation.ui.camera.CameraActivity
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private val resultViewModelSimple: ResultViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setToolbar()
        setOnClickListener()
        setFoodLabel()
        getMyLastLocation()
        setSearch()
    }

    private fun setSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                resultViewModelSimple.SetSearchRestaurant(query, lat, lon)
                Log.e("onQueryTextSubmit: ", "")

                showRecyclerView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

    private fun showRecyclerView() {
        binding.rvRestaurant.visibility = View.INVISIBLE
        binding.progresbarresult.visibility = View.VISIBLE
        val restaurntAdapter = RestaurantAdapter()
        resultViewModelSimple.GetSearchRestaurant().observe(this@ResultActivity) { restaurant ->
            if (restaurant.isNotEmpty()) {
                Log.e("data: ", restaurant.size.toString())
                restaurntAdapter.setData(restaurant)
                binding.rvRestaurant.visibility = View.VISIBLE
                binding.notfoundresult.visibility = View.INVISIBLE
                binding.progresbarresult.visibility = View.INVISIBLE
            } else {
                Log.e("showRecyclerView: ", "kosong")
                binding.notfoundresult.visibility = View.VISIBLE
                binding.progresbarresult.visibility = View.INVISIBLE
            }
        }
        with(binding.rvRestaurant) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = restaurntAdapter
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
            resultViewModelSimple.SetSearchRestaurant(foodname, lat, lon)
            Log.e("onQueryTextSubmit: ", "")
            showRecyclerView()

        }
    }

    companion object {
        const val FOOD_NAME = ""
    }
}