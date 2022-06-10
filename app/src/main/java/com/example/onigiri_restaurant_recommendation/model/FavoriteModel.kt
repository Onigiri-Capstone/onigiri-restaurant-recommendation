package com.example.onigiri_restaurant_recommendation.model

import com.google.gson.annotations.SerializedName


data class FavoriteModel (
    @SerializedName("user_id") val userId: String?,
    @SerializedName("restaurant_id") val userName: String?,

)