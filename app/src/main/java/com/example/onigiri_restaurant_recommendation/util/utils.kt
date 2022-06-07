package com.example.onigiri_restaurant_recommendation.util

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoLocationBottomSheet
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun checkPermission(permission: Array<String>, context: Context): Boolean {
    return permission.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

val REQUIRED_LOCATION_PERMISSIONS
        = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

fun checkLocationPermission(activity: Activity): Boolean {
    return REQUIRED_LOCATION_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            activity,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun requestLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        REQUIRED_LOCATION_PERMISSIONS,
        PERMISSION_REQUEST_ACCESS_LOCATION
    )
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun requestGPS(supportFragmentManager: FragmentManager) {
    val noLocationBottomSheet = NoLocationBottomSheet()
    noLocationBottomSheet.show(supportFragmentManager, NoLocationBottomSheet.TAG)
}

fun getUserLocation(activity: Activity, supportFragmentManager: FragmentManager) {
    if(!checkLocationPermission(activity)) {
        requestLocationPermission(activity)
    } else {
        if(!isLocationEnabled(activity)) {
            requestGPS(supportFragmentManager)
        }
    }
}