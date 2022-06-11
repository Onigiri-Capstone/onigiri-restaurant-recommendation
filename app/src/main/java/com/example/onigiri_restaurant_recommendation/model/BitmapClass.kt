package com.example.onigiri_restaurant_recommendation.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BitmapClass(
    val bitmap: Bitmap
):Parcelable