package com.example.onigiri_restaurant_recommendation.util.location

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.*
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.google.android.gms.location.*

class LocationService : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private var gpsIsEnabled = false

    private var permissionIsGranted = false

    private lateinit var gpsAndPermissionStatusLiveData: LiveData<Pair<PermissionStatus, GpsStatus>>

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("LocationService", "New Coordinates Received: ${locationResult.locations}")
        }
    }

    private val pairObserver = Observer<Pair<PermissionStatus, GpsStatus>> { pair ->
        pair?.let {
            Log.i("LocationService", "PairObserver received with ${pair.first} and ${pair.second}")
            handlePermissionStatus(pair.first)
            handleGpsStatus(pair.second)
            stopServiceIfNeeded()
        }
    }

    private fun handlePermissionStatus(status: PermissionStatus) {
        when (status) {
            is PermissionStatus.Granted -> {
                Log.i("LocationService", "Service - Permission: ${status.message}")
                permissionIsGranted = true
                registerForLocationTracking()
            }

            is PermissionStatus.Denied -> {
                Log.w("LocationService", "Service - Permission: ${status.message}")
                permissionIsGranted = false
            }
        }
    }

    private fun handleGpsStatus(status: GpsStatus) {
        when (status) {
            is GpsStatus.Enabled -> {
                Log.i("LocationService", "Service - Permission: ${status.message}")
                gpsIsEnabled = true
                registerForLocationTracking()
            }

            is GpsStatus.Disabled -> {
                Log.w("LocationService", "Service - Permission: ${status.message}")
                gpsIsEnabled = false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 5 * DateUtils.SECOND_IN_MILLIS
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        gpsAndPermissionStatusLiveData = with(application) {
            PermissionStatusListener(this, locationPermission)
                .combineLatestWith(GpsStatusListener(this))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i("LocationService", "Tracking service getting started")
        showOnGoingNotification()
        startObservingGpsAndPermissionStatus()
        return Service.START_STICKY
    }

    private fun startObservingGpsAndPermissionStatus() = gpsAndPermissionStatusLiveData
        .observe(this, pairObserver)

    private fun eitherPermissionOrGpsIsDisabled() = gpsIsEnabled.not() || permissionIsGranted.not()

    private fun registerForLocationTracking() {
        if (permissionIsGranted && gpsIsEnabled) {
            Log.i("LocationService", "Registering location update listener")
            isTrackingRunning = try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    Looper.myLooper()!!
                )
                true
            } catch (unlikely: SecurityException) {
                Log.e("LocationService", "Error when registerLocationUpdates()")
                error("Error when registerLocationUpdates()")
            }
        }
    }

    private fun unregisterFromLocationTracking() {
        Log.i("LocationService", "Unregistering location update listener")
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (unlikely: SecurityException) {
            Log.i("LocationService", "Error when unregisterLocationUpdated()")
            error("Error when unregisterLocationUpdated()")
        }
    }

    private fun showOnGoingNotification() {
        Log.i("LocationService", "showing showing ongoing notification")
        isServiceRunning = true
        Intent(this, MainActivity::class.java)
            .let { PendingIntent.getActivity(this, 0, it, 0) }
            .let { }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LocationService", "Service is destroyed now")
        isTrackingRunning = false
        isServiceRunning = false

        if (eitherPermissionOrGpsIsDisabled().not()) unregisterFromLocationTracking()
    }

    private fun stopServiceIfNeeded() {
        if (eitherPermissionOrGpsIsDisabled()) {
            stopSelf()
        }
    }

    companion object {
        //Refers to when this service is running and foreground notification is being displayed
        var isServiceRunning: Boolean = false
            private set

        //Refers to when app is listening to location updates
        var isTrackingRunning: Boolean = false
            private set
    }
}

fun <A, B> combineLatest(a: LiveData<A>, b: LiveData<B>): MutableLiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B> LiveData<A>.combineLatestWith(b: LiveData<B>): LiveData<Pair<A, B>> = combineLatest(this, b)