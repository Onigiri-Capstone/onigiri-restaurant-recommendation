package com.example.onigiri_restaurant_recommendation.ui.detailrestaurant.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetInputReviewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class InputReviewBottomSheet : BottomSheetDialogFragment(){

    private var _binding: BottomSheetInputReviewBinding? = null
    private val binding get() = _binding!!
    lateinit var dialog : BottomSheetDialog

    companion object {
        const val TAG = "InputReviewBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetInputReviewBinding.inflate(inflater, container, false)
        val mArgs = arguments
        val myValue = mArgs!!.getString("key")


        Log.e(TAG, "onCreateView: ${myValue.toString()}")
        binding.ratingBar.rating = myValue?.toFloat() ?: 0.0f
        setFullScreen()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = Bundle()
        args.putString("key"," rating.toString()")

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    fun setFullScreen(){
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}