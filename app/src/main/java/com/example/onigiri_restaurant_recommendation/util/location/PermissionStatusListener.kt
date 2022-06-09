package com.example.onigiri_restaurant_recommendation.util.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.onigiri_restaurant_recommendation.R

class PermissionStatusListener(private val context: Context,
                               private val permissionToListen: String) : LiveData<PermissionStatus>() {

    override fun onActive() = handlePermissionCheck()

    private fun handlePermissionCheck() {
        val isPermissionGranted = ActivityCompat.checkSelfPermission(context,
            permissionToListen) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted)
            postValue(PermissionStatus.Granted())
        else
            postValue(PermissionStatus.Denied())
    }
}

sealed class PermissionStatus {
    data class Granted(val message: Int = R.string.permission_status_granted) : PermissionStatus()
    data class Denied(val message: Int = R.string.permission_status_denied) : PermissionStatus()
}

val REQUIRED_LOCATION_PERMISSIONS
        = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

fun requestLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        REQUIRED_LOCATION_PERMISSIONS,
        PERMISSION_REQUEST_ACCESS_LOCATION
    )
}