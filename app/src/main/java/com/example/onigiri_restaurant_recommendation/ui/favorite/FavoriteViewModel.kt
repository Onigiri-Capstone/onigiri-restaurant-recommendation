package com.example.onigiri_restaurant_recommendation.ui.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onigiri_restaurant_recommendation.model.FavoriteModel
import com.example.onigiri_restaurant_recommendation.model.FavoriteRestaurantLocal
import com.example.onigiri_restaurant_recommendation.remote.local.FavoriteRestaurantRepository
import com.example.onigiri_restaurant_recommendation.remote.network.ApiConfig
import com.example.onigiri_restaurant_recommendation.remote.response.*
import com.example.onigiri_restaurant_recommendation.ui.result.ResultViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(application: Application) : ViewModel() {
    private val _ResponseFavPost = MutableLiveData<FavoritePostAndDelResponse>()
    val ResponseFavPost: LiveData<FavoritePostAndDelResponse> = _ResponseFavPost
    private val _ResponseFavDel = MutableLiveData<FavoritePostAndDelResponse>()
    val ResponseFavDel: LiveData<FavoritePostAndDelResponse> = _ResponseFavDel
    private val _listRestaurant = MutableLiveData<List<ResultsItem?>?>()
    val listRestaurant: LiveData<List<ResultsItem?>?> = _listRestaurant

    private val favoriteUserRepository = FavoriteRestaurantRepository(application)


    fun setFavoritePst(restaurant: FavoriteModel) {
        val client = ApiConfig.provideApiService().setFavoriteRestaurant(restaurant)
        client.enqueue(object : Callback<FavoritePostAndDelResponse> {
            override fun onResponse(
                call: Call<FavoritePostAndDelResponse>,
                response: Response<FavoritePostAndDelResponse>
            ) {
                if (response.isSuccessful) {
                    _ResponseFavPost.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FavoritePostAndDelResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFavoritePst(): LiveData<FavoritePostAndDelResponse> {
        return ResponseFavPost
    }

    fun setFavoriteDel(restaurant: FavoriteModel) {
        val client = ApiConfig.provideApiService().deleteFavoriteRestaurant(restaurant)
        client.enqueue(object : Callback<FavoritePostAndDelResponse> {
            override fun onResponse(
                call: Call<FavoritePostAndDelResponse>,
                response: Response<FavoritePostAndDelResponse>
            ) {
                if (response.isSuccessful) {
                    _ResponseFavPost.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FavoritePostAndDelResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFavoriteDel(): LiveData<FavoritePostAndDelResponse> {
        return ResponseFavPost
    }


    fun setRestaurantListFav(id: String) {
        val client = ApiConfig.provideApiService().getFavoriteRestaurant(id)
        client.enqueue(object : Callback<ListFavoriteUser> {
            override fun onResponse(
                call: Call<ListFavoriteUser>,
                response: Response<ListFavoriteUser>
            ) {
                if (response.isSuccessful) {
                    _listRestaurant.value = response.body()?.results
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListFavoriteUser>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSearchRestaurant(): LiveData<List<ResultsItem?>?> {
        return listRestaurant
    }

    //local
    fun getAllFavoriteRestaurant(): LiveData<List<FavoriteRestaurantLocal>> {
        return favoriteUserRepository.getAllFavoriteRestaurant()
    }
    fun insert(favoriteRestaurantLocal: FavoriteRestaurantLocal) {

        favoriteUserRepository.insert(favoriteRestaurantLocal)
    }

    fun delete(favoriteRestaurantLocal: FavoriteRestaurantLocal) {
        favoriteUserRepository.delete(favoriteRestaurantLocal)
    }

    fun getFavoriteUser(place_id: String): FavoriteRestaurantLocal {
        return favoriteUserRepository.getFavoriteRestaurant(place_id)
    }

    fun isFavoriteUserExists(place_id: String): Boolean {
        return favoriteUserRepository.isFavoriteRestaurantExists(place_id)
    }


    companion object {
        const val TAG = "FavoriteViewmodel"
    }
}