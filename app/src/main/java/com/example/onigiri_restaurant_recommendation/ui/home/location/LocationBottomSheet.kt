package com.example.onigiri_restaurant_recommendation.ui.home.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetLocationBinding
import com.example.onigiri_restaurant_recommendation.model.Location
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class LocationBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BottomSheetLocationBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<Location>()

    companion object {
        const val TAG = "LocationBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(listLocations)
        getLocation()
    }

    private val listLocations: ArrayList<Location>
        get() {
            val name = resources.getStringArray(R.array.location)
            val listLocation = ArrayList<Location>()
            for (i in name.indices) {
                val location = Location(name[i])
                listLocation.add(location)
            }
            return listLocation
        }

    private fun getLocation() {
        for(i in 0 until list.size) {
            val chip = Chip(context)
            chip.text = list[i].name

            chip.setOnClickListener {
                Toast.makeText(context, "${list[i].name} is chosen", Toast.LENGTH_SHORT).show()
                this.dismiss()
            }

            binding.chipGroup.addView(chip)
        }
    }
}