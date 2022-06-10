package com.example.onigiri_restaurant_recommendation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_restaurant")
data class FavoriteRestaurantLocal(
    @PrimaryKey
    @ColumnInfo(name = "place_id")
    val place_id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "photo_url")
    val photo_url: String = "",

    @ColumnInfo(name = "rating")
    val rating: Float = 0.0f,

    @ColumnInfo(name = "lat")
    val lat: Double = 0.0,

    @ColumnInfo(name = "lng")
    val lng: Double = 0.0,

    @ColumnInfo(name = "vicinity")
    val vicinity: String = "",
)