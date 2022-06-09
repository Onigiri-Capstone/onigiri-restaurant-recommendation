package com.example.onigiri_restaurant_recommendation.ui.detailrestaurant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onigiri_restaurant_recommendation.remote.network.ApiConfig
import com.example.onigiri_restaurant_recommendation.remote.response.ListRestaurantDetailResponse
import com.example.onigiri_restaurant_recommendation.remote.response.RestaurantDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRestaurantViewModel : ViewModel() {

    private val _restaurant = MutableLiveData<RestaurantDetailResponse>()
    private val restaurant: LiveData<RestaurantDetailResponse> = _restaurant

    fun setDetailRestaurant(placeId: String, lat: Double, lon: Double) {
        val client = ApiConfig.provideApiService().getDetailRestaurant(placeId,lat,lon)
        client.enqueue(object : Callback<ListRestaurantDetailResponse> {
            override fun onResponse(
                call: Call<ListRestaurantDetailResponse>,
                response: Response<ListRestaurantDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _restaurant.postValue(response.body()?.results)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListRestaurantDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailRestaurant(): LiveData<RestaurantDetailResponse> {
        return restaurant
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}