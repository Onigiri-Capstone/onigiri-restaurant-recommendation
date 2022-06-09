@file:Suppress("DEPRECATION")

package com.example.onigiri_restaurant_recommendation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityMainBinding
import com.example.onigiri_restaurant_recommendation.util.callNetworkConnection
import com.example.onigiri_restaurant_recommendation.util.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var gpsStatus: GpsStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callNetworkConnection(application, this, supportFragmentManager)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        subscribeToLocationPermissionListener()
        subscribeToGpsListener()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    private fun subscribeToGpsListener() = mainViewModel.gpsStatusLiveData
        .observe(this, gpsObserver)


    private val gpsObserver = Observer<GpsStatus> { status ->
        status?.let {
            gpsStatus = status
            updateGpsCheckUI(status)
        }
    }

    private fun updateGpsCheckUI(status: GpsStatus) {
        when (status) {
            is GpsStatus.Disabled -> {
                showGpsPermission(supportFragmentManager)
            }
            else -> {}
        }
    }

    private fun subscribeToLocationPermissionListener() =
        mainViewModel.locationPermissionStatusLiveData.observe(this, permissionObserver)

    private val permissionObserver = Observer<PermissionStatus> { status ->
        status?.let {
            when (status) {
                is PermissionStatus.Denied -> requestLocationPermission(this)
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}