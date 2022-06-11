package com.example.onigiri_restaurant_recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Survey(
    var id: Int,
    var text: CharSequence,
    var checked: Boolean
) : Parcelable
