package com.example.onigiri_restaurant_recommendation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ImageSliderItemBinding
import com.example.onigiri_restaurant_recommendation.databinding.ItemRestaurantBinding
import com.example.onigiri_restaurant_recommendation.model.ImageModel
import com.example.onigiri_restaurant_recommendation.remote.response.RestaurantSearchResponse
import java.util.*

class ViewPagerImageSlide:RecyclerView.Adapter<ViewPagerImageSlide.ImageViewHolder>(){
    private var image = ArrayList<ImageModel>()

    fun setData(newListData: List<ImageModel>?) {
        if (newListData == null) return
        image.clear()
        image.addAll(newListData)
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val binding: ImageSliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageModel) {
            Glide.with(itemView)
                .load(image.image)
                .transform(RoundedCorners(20))
                .into(binding.imageViewSlider)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageSlide.ImageViewHolder {
        val view = ImageSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(image[position])
    }

    override fun getItemCount(): Int {
        return image.size
    }

}