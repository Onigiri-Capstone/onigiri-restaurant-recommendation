package com.example.onigiri_restaurant_recommendation.ui.home

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.FragmentHomeBinding
import com.example.onigiri_restaurant_recommendation.ui.camera.CameraActivity
import com.example.onigiri_restaurant_recommendation.ui.category.CategoryActivity
import com.example.onigiri_restaurant_recommendation.ui.home.category.CategoryBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.home.location.LocationBottomSheet
import com.example.onigiri_restaurant_recommendation.ui.profile.ProfileActivity
import com.example.onigiri_restaurant_recommendation.ui.result.ResultActivity
import com.example.onigiri_restaurant_recommendation.util.location.checkLocationPermission
import com.example.onigiri_restaurant_recommendation.util.location.requestLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var address: String

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        getLastLocation()

//        callNetworkConnection(requireActivity().application, this, parentFragmentManager)

        setOnClickListener()
    }

    private fun getLastLocation() {
        if(!checkLocationPermission(requireContext())) {
            requestLocationPermission(requireActivity())
        }

        val lastLocation = fusedLocationProviderClient.lastLocation

        lastLocation.apply {
            addOnSuccessListener {
                val geocodeAddress = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                address = geocodeAddress[0].getAddressLine(0)
            }
            addOnFailureListener {
                Log.d(TAG, "getLastLocation: Failed to load location")
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            tvLocation.setOnClickListener(this@HomeFragment)
            tvInput.setOnClickListener(this@HomeFragment)
            btnMore.setOnClickListener(this@HomeFragment)
            btnCamera.setOnClickListener(this@HomeFragment)
            btnSweets.setOnClickListener(this@HomeFragment)
            btnRice.setOnClickListener(this@HomeFragment)
            btnMeatball.setOnClickListener(this@HomeFragment)
            btnChicken.setOnClickListener(this@HomeFragment)
            btnDrinks.setOnClickListener(this@HomeFragment)
            btnCoffee.setOnClickListener(this@HomeFragment)
            btnSeafood.setOnClickListener(this@HomeFragment)
            ivProfile.setOnClickListener(this@HomeFragment)
            swiperefreshhome.setOnRefreshListener {
                swiperefreshhome.isRefreshing = false
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_camera -> {
                startActivity(Intent(activity, CameraActivity::class.java))
                activity?.onBackPressed()
            }
            R.id.tv_location -> {
                val locationBottomSheet = LocationBottomSheet().getInstance(address)
                locationBottomSheet.show(parentFragmentManager, LocationBottomSheet.TAG)
            }
            R.id.tv_input -> {
                startActivity(Intent(activity, ResultActivity::class.java))
            }
            R.id.btn_more -> {
                val categoryBottomSheet = CategoryBottomSheet()
                categoryBottomSheet.show(parentFragmentManager, CategoryBottomSheet.TAG)
            }
            R.id.btn_sweets -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.sweets))
                )
            }
            R.id.btn_rice -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.rice))
                )
            }
            R.id.btn_meatball -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.meatball))
                )
            }
            R.id.btn_chicken -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.chicken))
                )
            }
            R.id.btn_drinks -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.drinks))
                )
            }
            R.id.btn_coffee -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.coffee))
                )
            }
            R.id.btn_seafood -> {
                startActivity(
                    Intent(activity, CategoryActivity::class.java)
                        .putExtra(CategoryActivity.CATEGORY_NAME, resources.getString(R.string.seafood))
                )
            }
            R.id.iv_profile -> {
                startActivity(Intent(activity, ProfileActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}