package com.example.onigiri_restaurant_recommendation.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onigiri_restaurant_recommendation.adapter.FavoriteAdapter
import com.example.onigiri_restaurant_recommendation.data.remote.response.*
import com.example.onigiri_restaurant_recommendation.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        favoriteAdapter = FavoriteAdapter()
        val application = requireActivity().application
        favoriteViewModel = ViewModelProvider(this, FavoriteRestaurantViewModelFactory(application))[FavoriteViewModel::class.java]
        favoriteViewModel.getAllFavoriteRestaurant()
            .observe(viewLifecycleOwner) {
                binding.empty.isVisible = it.isEmpty()

                val list = arrayListOf<RestaurantSearchResponse>()
                for (i in it.indices) {
                    Log.d("TAG", "favoriteUserList loop: ${it[i]}")
                    val user = RestaurantSearchResponse(
                        place_id = it[i].place_id,
                        name = it[i].name!!,
                        photo_url = it[i].photo_url!!,
                        rating = it[i].rating!!,

                        photos = listOf(),
                        geometry = ListGeometryResponse(
                            ListLocationResponse(it[i].lat, it[i].lng),
                            ListViewportResponse(
                                ListNortheastResponse(0.0, 0.0),
                                ListSouthwestResponse(0.0, 0.0)
                            )
                        ),
                        vicinity = it[i].vicinity!!,
                        plus_code = ListPlusCodeResponse("", ""),
                        types = listOf(),
                        opening_hours = ListOpeningHoursReponse(true, listOf(), listOf())
                    )
                    list.add(user)
                }
                Log.d("TAG", " list.add :$list ")

                favoriteAdapter.setData(list)
                binding.apply {
                    rvRestaurant.layoutManager = LinearLayoutManager(requireContext())
                    rvRestaurant.adapter = favoriteAdapter
                    rvRestaurant.setHasFixedSize(true)
                }

                binding.loading.root.isVisible = false

            }
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