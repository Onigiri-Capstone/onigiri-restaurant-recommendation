package com.example.onigiri_restaurant_recommendation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.model.ImageModel
import java.util.*

class ViewPagerImageSlide(private val context: Context, private val myModeArrayList: ArrayList<ImageModel>) : PagerAdapter() {

    override fun getCount(): Int {
        Log.e("getCount: ",  myModeArrayList.size.toString())
        return myModeArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        // get data
        val model = myModeArrayList[position]
        val image = model.image
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        // set data
        Glide.with(itemView)
            .load(image)
            .transform(RoundedCorners(20))
            .into(imageView)

        itemView.setOnClickListener {
            Toast.makeText(context, "Item Clicked", Toast.LENGTH_LONG).show()
        }
        Objects.requireNonNull(container).addView(itemView)
        //add view to container

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as View)
    }
}