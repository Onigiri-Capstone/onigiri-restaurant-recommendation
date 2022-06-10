package com.example.onigiri_restaurant_recommendation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ItemRestaurantBinding
import com.example.onigiri_restaurant_recommendation.model.FavoriteRestaurantLocal
import com.example.onigiri_restaurant_recommendation.remote.response.RestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.DetailRestaurantActivity
import java.util.ArrayList


class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {
    private var fav: Boolean = false
    private var searchnameRestaurant :String?=""

    private var restaurants = ArrayList<RestaurantSearchResponse>()
    private var restaurantsFavString = ArrayList<String>()
    private var addrestaurant_id = MutableLiveData<FavoriteRestaurantLocal>()
    private var delrestaurant_id = MutableLiveData<FavoriteRestaurantLocal>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<RestaurantSearchResponse>?) {
        if (newListData == null) return
        restaurants.clear()
        restaurants.addAll(newListData)
        notifyDataSetChanged()
    }
    fun searchRestaurant(text:String){
        searchnameRestaurant = text
    }
    fun setDataRestaurantFav(it:List<FavoriteRestaurantLocal>) {
        for (element in it) {
            restaurantsFavString.add(element.place_id)

        }
        Log.e( "setDataRestaurantFav: ", restaurantsFavString.toString())
    }

    fun addFavButton(): MutableLiveData<FavoriteRestaurantLocal> {
        return addrestaurant_id
    }

    fun delFavButton(): MutableLiveData<FavoriteRestaurantLocal> {
        return delrestaurant_id
    }

    inner class MyViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: RestaurantSearchResponse) {
            binding.apply {
                with(restaurant) {
                    Log.e("place_id: ", place_id)
                    if (restaurantsFavString.contains(place_id)) {
                        Log.e("bind: ", place_id )
                        fav = true
                        buttonFavorite.setBackgroundResource(R.drawable.ic_favorite_active)
                    }
                    else{
                        buttonFavorite.setBackgroundResource(R.drawable.ic_favorite)
                    }
                    Log.e( "bind: ",fav.toString() )

                    RestaurantName.text = name
                    AddressRestaurant.text = vicinity
                    rateRestaurant.text = rating.toString()

                    Glide.with(itemView)
                        .load(photo_url)
                        .transform(RoundedCorners(20))
                        .into(ivRestaurant)

                    itemView.setOnClickListener {

                        val intent = Intent(itemView.context, DetailRestaurantActivity::class.java)
                        if(searchnameRestaurant!=null){
                            intent.putExtra(DetailRestaurantActivity.SEARCH_NAME, searchnameRestaurant)
                        }
                        intent.putExtra(DetailRestaurantActivity.PLACE_ID, place_id)
                        itemView.context.startActivity(intent)
                    }
                    buttonFavorite.setOnClickListener { it ->
                        fav =!fav
                        if (fav) {
                            addrestaurant_id.postValue(
                                FavoriteRestaurantLocal(
                                    place_id = place_id,
                                    name = name,
                                    photo_url = photo_url,
                                    rating = rating.toFloat(),
                                    lat = geometry.location.lat,
                                    lng = geometry.location.lng,
                                    vicinity = vicinity
                                )
                            )
                            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite_active)
                        } else {
                            delrestaurant_id.postValue(
                                FavoriteRestaurantLocal(
                                    place_id = place_id,
                                    name = name,
                                    photo_url = photo_url,
                                    rating = rating.toFloat(),
                                    lat = geometry.location.lat,
                                    lng = geometry.location.lng,
                                    vicinity = vicinity
                                )
                            )
                            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite)
                        }
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size
//    private fun favorite(buttonFavorite: ImageButton) {
//        if (fav) {
//            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite_active)
//        } else {
//            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite)
//        }
//    }


}