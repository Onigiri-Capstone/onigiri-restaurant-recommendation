package com.example.onigiri_restaurant_recommendation.util.location

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.onigiri_restaurant_recommendation.ui.main.MainViewModel

class LocationUtil(
    private val activity: Activity,
    private val lifecycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager,
    private val mainViewModel: MainViewModel) {

    private lateinit var gpsStatus: GpsStatus

    fun subscribeToGpsListener() = mainViewModel.gpsStatusLiveData
        .observe(lifecycleOwner, gpsObserver)


    val gpsObserver = Observer<GpsStatus> { status ->
        status?.let {
            gpsStatus = status
            updateGpsCheckUI(status)
        }
    }

    fun updateGpsCheckUI(status: GpsStatus) {
        when (status) {
            is GpsStatus.Disabled -> {
                showGpsPermission(fragmentManager)
            }
            else -> {}
        }
    }

    fun subscribeToLocationPermissionListener() =
        mainViewModel.locationPermissionStatusLiveData.observe(lifecycleOwner, permissionObserver)

    private val permissionObserver = Observer<PermissionStatus> { status ->
        status?.let {
            when (status) {
                is PermissionStatus.Denied -> requestLocationPermission(activity)
            }
        }
    }
}