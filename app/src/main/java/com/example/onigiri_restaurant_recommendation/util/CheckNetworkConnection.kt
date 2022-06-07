package com.example.onigiri_restaurant_recommendation.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.onigiri_restaurant_recommendation.ui.bottomsheet.NoInternetBottomSheet

class CheckNetworkConnection(private val connectivityManager: ConnectivityManager): LiveData<Boolean>() {

    constructor(application: Application): this(application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    private val networkCallback =

    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}


private lateinit var checkNetworkConnection: CheckNetworkConnection

fun callNetworkConnection(application: Application, lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager) {
    checkNetworkConnection = CheckNetworkConnection(application)
    checkNetworkConnection.observe(lifecycleOwner) {
        if(!it) {
            val noInternetBottomSheet = NoInternetBottomSheet()
            noInternetBottomSheet.show(fragmentManager, NoInternetBottomSheet.TAG)
        }
    }
}