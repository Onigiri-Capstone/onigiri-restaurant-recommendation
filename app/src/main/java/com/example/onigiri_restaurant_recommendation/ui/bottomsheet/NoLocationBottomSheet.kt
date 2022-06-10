package com.example.onigiri_restaurant_recommendation.ui.bottomsheet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetNoLocationBinding
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoLocationBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BottomSheetNoLocationBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "NoLocationBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNoLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }
}