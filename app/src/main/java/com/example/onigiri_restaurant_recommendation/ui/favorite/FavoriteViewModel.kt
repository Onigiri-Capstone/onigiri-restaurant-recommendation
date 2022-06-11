package com.example.onigiri_restaurant_recommendation.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.onigiri_restaurant_recommendation.data.local.FavoriteRestaurantRepository
import com.example.onigiri_restaurant_recommendation.data.local.entity.FavoriteRestaurantLocal

class FavoriteViewModel(application: Application) : ViewModel() {

    private val favoriteUserRepository = FavoriteRestaurantRepository(application)

    fun getAllFavoriteRestaurant(): LiveData<List<FavoriteRestaurantLocal>> {
        return favoriteUserRepository.getAllFavoriteRestaurant()
    }
    fun insert(favoriteRestaurantLocal: FavoriteRestaurantLocal) {

        favoriteUserRepository.insert(favoriteRestaurantLocal)
    }

    fun delete(favoriteRestaurantLocal: FavoriteRestaurantLocal) {
        favoriteUserRepository.delete(favoriteRestaurantLocal)
    }
    fun isFavoriteUserExists(place_id: String): Boolean {
        return favoriteUserRepository.isFavoriteRestaurantExists(place_id)
    }


    companion object {
        const val TAG = "FavoriteViewmodel"
    }
}