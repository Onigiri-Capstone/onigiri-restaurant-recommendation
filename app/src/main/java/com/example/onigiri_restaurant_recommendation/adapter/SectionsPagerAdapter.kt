package com.example.onigiri_restaurant_recommendation.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.descriptiondetailrestaurant.DescriptionRestaurantFragment
import com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.reviewdetailrestaurant.ReviewDetailRestaurantFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DescriptionRestaurantFragment()
            1 -> fragment = ReviewDetailRestaurantFragment()
        }
        return fragment as Fragment
    }

}