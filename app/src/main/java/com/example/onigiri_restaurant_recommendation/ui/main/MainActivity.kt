package com.example.onigiri_restaurant_recommendation.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivityMainBinding
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoInternetBottomSheet
import com.example.onigiri_restaurant_recommendation.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var checkGPS: CheckGPS

    companion object {
        const val LOCATION_RESULT = 200

        private val REQUIRED_PERMISSIONS
                = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private const val REQUEST_CODE_PERMISSIONS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkGPS = CheckGPS(this,this, supportFragmentManager)
        checkGPS.getUserLocation()

        callNetworkConnection(application, this, supportFragmentManager)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermission(REQUIRED_PERMISSIONS, this@MainActivity)) {
                if(!isLocationEnabled(this@MainActivity)) {
                    requestGPS(supportFragmentManager)
                }
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}