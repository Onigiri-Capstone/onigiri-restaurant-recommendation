package com.example.onigiri_restaurant_recommendation.remote.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.onigiri_restaurant_recommendation.model.FavoriteRestaurantLocal
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class FavoriteRestaurantRepository(application: Application) {
    private val favoriteRestaurantDao: FavoriteRestaurantDao =
        FavoriteRestaurantDatabase.getDatabase(application).getFavoriteRestaurantDao()

    fun getAllFavoriteRestaurant(): LiveData<List<FavoriteRestaurantLocal>> {
        return favoriteRestaurantDao.getAllFavoriteRestaurant()
    }

    fun getFavoriteRestaurant(place_id: String): FavoriteRestaurantLocal {
        lateinit var favRestaurant: FavoriteRestaurantLocal
        runBlocking {
            val temp = async {
                favoriteRestaurantDao.getFavoriteRestaurant(place_id)
            }
            favRestaurant = temp.await()
        }
        return favRestaurant
    }

    fun insert(favoriteRestaurantLocal: FavoriteRestaurantLocal) {
        runBlocking {
            favoriteRestaurantDao.insert(favoriteRestaurantLocal)
        }
    }

    fun delete(favoriteRestaurantLocal: FavoriteRestaurantLocal) {
        runBlocking {
            favoriteRestaurantDao.delete(favoriteRestaurantLocal)
        }
    }


    fun isFavoriteRestaurantExists(place_id: String): Boolean {
        var isExists: Boolean
        runBlocking {
            val temp = async {
                favoriteRestaurantDao.isFavoriteRestaurantExists(place_id)
            }
            isExists = temp.await()
        }
        return isExists
    }
}