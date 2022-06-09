package com.example.onigiri_restaurant_recommendation.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

class CheckGPS(private val activity: Activity, private val context: Context, private val supportFragmentManager: FragmentManager) {
    companion object {
        val REQUIRED_LOCATION_PERMISSIONS
                = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    fun checkLocationPermission(): Boolean {
        return REQUIRED_LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            REQUIRED_LOCATION_PERMISSIONS,
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getUserLocation() {
        if(!checkLocationPermission(activity)) {
            requestLocationPermission(activity)
        } else {
            if(!isLocationEnabled(activity)) {
                requestGPS(supportFragmentManager)
            }
        }
    }
}