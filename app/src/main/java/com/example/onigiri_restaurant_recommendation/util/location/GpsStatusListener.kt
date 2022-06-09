package com.example.onigiri_restaurant_recommendation.util.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoLocationBottomSheet

@Suppress("DEPRECATION")
class GpsStatusListener(private val context: Context) : LiveData<GpsStatus>() {

    private val gpsSwitchStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = checkGpsAndReact()
    }

    override fun onInactive() = unregisterReceiver()

    override fun onActive() {
        registerReceiver()
        checkGpsAndReact()
    }

    private fun checkGpsAndReact() = if (isLocationEnabled()) {
        postValue(GpsStatus.Enabled())
    } else {
        postValue(GpsStatus.Disabled())
    }

    private fun isLocationEnabled() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(LocationManager::class.java)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    } else {
        try {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE
            ) != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Settings.SettingNotFoundException) {
            Log.e("GpsStatusListener", e.toString())
            false
        }
    }

    private fun registerReceiver() = context.registerReceiver(gpsSwitchStateReceiver,
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
    )

    private fun unregisterReceiver() = context.unregisterReceiver(gpsSwitchStateReceiver)
}

sealed class GpsStatus {
    data class Enabled(val message: Int = R.string.gps_status_enabled) : GpsStatus()
    data class Disabled(val message: Int = R.string.gps_status_disabled) : GpsStatus()
}

val noLocationBottomSheet = NoLocationBottomSheet()

fun showGpsPermission(fragmentManager: FragmentManager) {
    noLocationBottomSheet.show(fragmentManager, NoLocationBottomSheet.TAG)
}

