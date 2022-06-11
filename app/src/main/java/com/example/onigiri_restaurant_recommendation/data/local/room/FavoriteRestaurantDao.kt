package com.example.onigiri_restaurant_recommendation.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.onigiri_restaurant_recommendation.data.local.entity.FavoriteRestaurantLocal

@Dao
interface FavoriteRestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteRestaurantLocal: FavoriteRestaurantLocal)

    @Delete
    suspend fun delete(favoriteRestaurantLocal: FavoriteRestaurantLocal)

    @Query("SELECT EXISTS (SELECT * FROM favorite_restaurant WHERE place_id = :place_id)")
    suspend fun isFavoriteRestaurantExists(place_id: String): Boolean

    @Query("SELECT * FROM favorite_restaurant WHERE place_id = :place_id LIMIT 1")
    suspend fun getFavoriteRestaurant(place_id: String): FavoriteRestaurantLocal

    @Query("SELECT * FROM favorite_restaurant")
    fun getAllFavoriteRestaurant(): LiveData<List<FavoriteRestaurantLocal>>
}