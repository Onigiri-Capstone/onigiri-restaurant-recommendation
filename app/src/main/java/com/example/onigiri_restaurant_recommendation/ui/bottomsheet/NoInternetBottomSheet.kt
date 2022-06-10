package com.example.onigiri_restaurant_recommendation.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetNoInternetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoInternetBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BottomSheetNoInternetBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "NoInternetBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNoInternetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTry.setOnClickListener {
            dismiss()
        }
    }

}