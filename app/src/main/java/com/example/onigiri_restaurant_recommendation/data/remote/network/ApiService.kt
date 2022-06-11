package com.example.onigiri_restaurant_recommendation.data.remote.network

import com.example.onigiri_restaurant_recommendation.data.remote.response.ListRestaurantDetailResponse
import com.example.onigiri_restaurant_recommendation.data.remote.response.ListRestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.data.remote.response.RestaurantRecommendationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("main/nearby")
    fun searchRestaurant(
        @Query("search") search: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): Call<ListRestaurantSearchResponse>

    @GET("main/details")
    fun getDetailRestaurant(
        @Query("id") id: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): Call<ListRestaurantDetailResponse>

    @GET("users/recommendation")
    fun getRestaurantRecommendation(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("first") first: String,
        @Query("second") second: String
    ): Call<List<RestaurantRecommendationResponse>>
}