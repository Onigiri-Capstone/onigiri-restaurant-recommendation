package com.example.onigiri_restaurant_recommendation.ui.main

import android.Manifest
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.onigiri_restaurant_recommendation.util.location.GpsStatusListener
import com.example.onigiri_restaurant_recommendation.util.location.PermissionStatusListener

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val gpsStatusLiveData = GpsStatusListener(application)

    val locationPermissionStatusLiveData = PermissionStatusListener(application,
        Manifest.permission.ACCESS_FINE_LOCATION)
}