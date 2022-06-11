package com.example.onigiri_restaurant_recommendation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.data.remote.response.RestaurantRecommendationResponse
import com.example.onigiri_restaurant_recommendation.databinding.ItemRestaurantBinding
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.DetailRestaurantActivity

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.MyViewHolder>() {

    private var restaurants = ArrayList<RestaurantRecommendationResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<RestaurantRecommendationResponse>?) {
        if (newListData == null) return
        restaurants.clear()
        restaurants.addAll(newListData)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: RestaurantRecommendationResponse) {
            binding.apply {
                with(restaurant) {
                    RestaurantName.text = name
                    AddressRestaurant.text = address
                    rateRestaurant.text = rating.toString()
                    distance.text = "${"%.1f".format(range)} KM"

//                    Glide.with(itemView)
//                        .load(photo_url)
//                        .transform(RoundedCorners(20))
//                        .into(ivRestaurant)

                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailRestaurantActivity::class.java)
                        intent.putExtra(DetailRestaurantActivity.PLACE_ID, place_id)
                        itemView.context.startActivity(intent)
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
}