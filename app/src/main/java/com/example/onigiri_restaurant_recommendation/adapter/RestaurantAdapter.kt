package com.example.onigiri_restaurant_recommendation.adapter
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ItemRestaurantBinding
import com.example.onigiri_restaurant_recommendation.remote.response.RestaurantSearchResponse
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.DetailRestaurantActivity
import java.util.ArrayList

class RestaurantAdapter: RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {
    private var fav = false

    private var restaurants = ArrayList<RestaurantSearchResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<RestaurantSearchResponse>?) {
        if (newListData == null) return
        restaurants.clear()
        restaurants.addAll(newListData)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: RestaurantSearchResponse) {
            binding.apply {
                with(restaurant) {
                    RestaurantName.text = name
                    AddressRestaurant.text = vicinity
                    rateRestaurant.text = rating.toString()

                    Glide.with(itemView)
                        .load(photo_url)
                        .transform(RoundedCorners(20))
                        .into(ivRestaurant)

                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailRestaurantActivity::class.java)
                        intent.putExtra(DetailRestaurantActivity.PLACE_ID, place_id)
                        itemView.context.startActivity(intent)
                    }
                }
                buttonFavorite.setOnClickListener {
                    fav = !fav
                    favorite(buttonFavorite)
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
    private fun favorite(buttonFavorite: ImageButton) {
        if (fav) {
            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite_active)
        } else {
            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite)
        }
    }
}