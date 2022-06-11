package com.example.onigiri_restaurant_recommendation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onigiri_restaurant_recommendation.data.remote.network.ApiConfig
import com.example.onigiri_restaurant_recommendation.data.remote.response.ListRestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.data.remote.response.RestaurantRecommendationResponse
import com.example.onigiri_restaurant_recommendation.model.Location
import com.example.onigiri_restaurant_recommendation.data.remote.response.RestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.ui.result.ResultViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    fun setLocation(lat: Double, long: Double) {
        _location.postValue(Location(lat, long))
    }

    private val _listRestaurant = MutableLiveData<List<RestaurantRecommendationResponse>>()
    val listRestaurant: LiveData<List<RestaurantRecommendationResponse>> = _listRestaurant

    fun getRecommendation(lat: Double, long: Double, first: String, second: String) {
        val client = ApiConfig.provideApiService().getRestaurantRecommendation(lat, long, first, second)
        client.enqueue(object : Callback<List<RestaurantRecommendationResponse>> {
            override fun onResponse(
                call: Call<List<RestaurantRecommendationResponse>>,
                response: Response<List<RestaurantRecommendationResponse>>
            ) {
                if (response.isSuccessful) {
                    _listRestaurant.postValue(response.body())
                }
                else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<RestaurantRecommendationResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "HomeViewModel"
    }
}