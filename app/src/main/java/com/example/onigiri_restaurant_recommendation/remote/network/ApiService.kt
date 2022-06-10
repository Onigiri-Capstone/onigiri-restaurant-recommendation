package com.example.onigiri_restaurant_recommendation.remote.network

import com.example.onigiri_restaurant_recommendation.model.FavoriteModel
import com.example.onigiri_restaurant_recommendation.remote.response.FavoritePostAndDelResponse
import com.example.onigiri_restaurant_recommendation.remote.response.ListFavoriteUser
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

    @Headers("Content-Type: application/json")
    @POST("users/favorites")
    fun setFavoriteRestaurant(
        @Body userData: FavoriteModel
    ): Call<FavoritePostAndDelResponse>

    @GET("users/favorites")
    fun getFavoriteRestaurant(
        @Query("id") id: String
    ): Call<ListFavoriteUser>

    @Headers("Content-Type: application/json")
    @DELETE("users/favorites")
    fun deleteFavoriteRestaurant(
        @Body userData: FavoriteModel
    ): Call<FavoritePostAndDelResponse>

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