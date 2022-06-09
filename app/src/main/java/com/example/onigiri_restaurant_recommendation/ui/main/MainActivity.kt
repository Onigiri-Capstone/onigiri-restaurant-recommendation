@file:Suppress("DEPRECATION")

package com.example.onigiri_restaurant_recommendation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityMainBinding
import com.example.onigiri_restaurant_recommendation.util.callNetworkConnection
import com.example.onigiri_restaurant_recommendation.util.location.LocationUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callNetworkConnection(application, this, supportFragmentManager)

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val locationUtil = LocationUtil(this, this, supportFragmentManager, mainViewModel)
        locationUtil.subscribeToLocationPermissionListener()
        locationUtil.subscribeToGpsListener()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }


    override fun onBackPressed() {
        finishAffinity()
    }
}