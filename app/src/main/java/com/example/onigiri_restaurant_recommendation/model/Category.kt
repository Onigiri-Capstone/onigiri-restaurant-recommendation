package com.example.onigiri_restaurant_recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var name: String,
    var icon: Int
) : Parcelable