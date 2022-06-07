package com.example.onigiri_restaurant_recommendation.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.adapter.CategoryAdapter
import com.example.onigiri_restaurant_recommendation.databinding.BottomSheetCategoryBinding
import com.example.onigiri_restaurant_recommendation.model.Category
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheet: BottomSheetDialogFragment() {

    private var _binding: BottomSheetCategoryBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<Category>()

    companion object {
        const val TAG = "CategoryBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(listCategories)
        showRecyclerView()
    }

    private val listCategories: ArrayList<Category>
        get() {
            val name = resources.getStringArray(R.array.categeory_name)
            val icon = resources.obtainTypedArray(R.array.categeory_icon)
            val listCategory = ArrayList<Category>()
            for (i in name.indices) {
                val category = Category(name[i], icon.getResourceId(i, -1))
                listCategory.add(category)
            }
            return listCategory
        }

    private fun showRecyclerView() {
        with(binding.rvCategory) {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = CategoryAdapter(list)
        }

    }

}