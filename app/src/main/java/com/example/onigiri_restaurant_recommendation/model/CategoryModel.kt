package com.example.onigiri_restaurant_recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val lat: Double,
    val lng: Double,
    val category: String
):Parcelable