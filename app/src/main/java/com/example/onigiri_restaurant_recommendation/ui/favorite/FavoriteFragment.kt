package com.example.onigiri_restaurant_recommendation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.onigiri_restaurant_recommendation.databinding.FragmentFavoriteBinding
import com.example.onigiri_restaurant_recommendation.util.callNetworkConnection

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        callNetworkConnection(requireActivity().application, this, parentFragmentManager)

        binding.swiperefreshfavorite.setOnRefreshListener {
            binding.swiperefreshfavorite.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}