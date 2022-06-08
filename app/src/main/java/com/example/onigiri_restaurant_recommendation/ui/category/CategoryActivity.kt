package com.example.onigiri_restaurant_recommendation.ui.category

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.RestaurantAdapter
import com.example.onigiri_restaurant_recommendation.databinding.ActivityCategoryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    private val categoryViewModel: CategoryViewModel by viewModels()

    companion object {
        const val CATEGORY_NAME = "Category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val categoryName = foodCategory(intent.getStringExtra(CATEGORY_NAME))

        binding.swiperefreshcategory.setOnRefreshListener {
            binding.swiperefreshcategory.isRefreshing = false
        }

        setToolbar(intent.getStringExtra(CATEGORY_NAME))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()

//        categoryName?.let { categoryViewModel.setSearchRestaurant(it, lat, lon) }
        categoryViewModel.setSearchRestaurant(categoryName, lat, lon)

        showRecyclerView()
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
        with(binding) {
            showLoading(true)
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
                        this@CategoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun foodCategory(category: String?): String {
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
        return foodcategory
    }

    private fun showLoading(state: Boolean) {
        binding.loading.root.isVisible = state
    }

    private fun showEmpty(state: Boolean) {
        binding.notfoundresult.isVisible = state
    }
}