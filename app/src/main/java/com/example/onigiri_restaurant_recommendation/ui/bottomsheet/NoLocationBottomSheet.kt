package com.example.onigiri_restaurant_recommendation.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetNoLocationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoLocationBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BottomSheetNoLocationBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "NoLocationBottomSheet"
    }

    init {
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNoLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        binding.btnTry.setOnClickListener {
            dismiss()
        }
    }
}