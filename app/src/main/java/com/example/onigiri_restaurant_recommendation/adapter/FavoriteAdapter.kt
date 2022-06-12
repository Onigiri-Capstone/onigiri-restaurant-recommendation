package com.example.onigiri_restaurant_recommendation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.data.remote.response.RestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.databinding.ItemRestaurantFavoriteBinding
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.DetailRestaurantActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
    private var fav: Boolean = false
    private var searchnameRestaurant :String?=""

    private var restaurants = ArrayList<RestaurantSearchResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<RestaurantSearchResponse>?) {
        if (newListData == null) return
        restaurants.clear()
        restaurants.addAll(newListData)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemRestaurantFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: RestaurantSearchResponse) {
            binding.apply {
                with(restaurant) {
                    Log.e("place_id: ", place_id)

                    Log.e( "bind: ",fav.toString() )

                    RestaurantName.text = name
                    AddressRestaurant.text = vicinity
                    rateRestaurant.text = rating.toString()

                    if(photo_url != "") {
                        Glide.with(itemView)
                            .load(photo_url)
                            .into(ivRestaurant)
                    }

                    itemView.setOnClickListener {

                        val intent = Intent(itemView.context, DetailRestaurantActivity::class.java)
                        if(searchnameRestaurant!=null){
                            intent.putExtra(DetailRestaurantActivity.SEARCH_NAME, searchnameRestaurant)
                        }
                        intent.putExtra(DetailRestaurantActivity.PLACE_ID, place_id)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemRestaurantFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size
}