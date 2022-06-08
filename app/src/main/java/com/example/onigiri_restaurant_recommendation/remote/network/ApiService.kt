package com.example.onigiri_restaurant_recommendation.remote.network

import com.example.onigiri_restaurant_recommendation.remote.response.ListRestaurantDetailResponse
import com.example.onigiri_restaurant_recommendation.remote.response.ListRestaurantSearchResponse
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

    @POST("users/favorites")
    fun setFavoriteRestaurant(
        @Body user_id: String,
        @Body restaurant_id: String
    )

    @GET("users/favorites")
    suspend fun getFavoriteRestaurant(
        @Query("id") id: String
    )

    @DELETE("users/favorites")
    fun deleteFavoriteRestaurant(
        @Body id: String
    )

    //    @GET("main/nearby")
//    suspend fun searchRestaurant(
//        @Query("search") search: String,
//        @Query("lat") lat: Double,
//        @Query("long") long: Double
//    ): ListRestaurantSearchResponse

//    @GET("main/details/{id}")
//    fun getDetailRestaurant(
//        @Path("id") id: String
//    ): ListRestaurantDetailResponse

}