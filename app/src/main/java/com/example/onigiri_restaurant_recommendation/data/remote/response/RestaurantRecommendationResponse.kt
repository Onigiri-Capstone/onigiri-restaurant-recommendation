package com.example.onigiri_restaurant_recommendation.data.remote.response

import com.google.gson.annotations.SerializedName

data class RestaurantRecommendationResponse(
    val place_id: String,
    val name: String,
    val type: String,
    val price_level: Int,
    val rating: Double,
    val address: String,
    val phone: String,
    val photos: String,
    val latitude: String,
    val longitude: String,
    @SerializedName("distance")
    val range: Double
)