package com.example.onigiri_restaurant_recommendation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.onigiri_restaurant_recommendation.databinding.ItemSurveyBinding
import com.example.onigiri_restaurant_recommendation.model.Category

class SurveyAdapter(private val categories: ArrayList<Category>) :
    RecyclerView.Adapter<SurveyAdapter.MyViewHolder>() {


    lateinit var checkbox: CheckBox

    var check_string = arrayListOf<String>()
    var check_id = arrayListOf<Int>()

    inner class MyViewHolder(private val binding: ItemSurveyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
                with(category) {
                    idSurvey.text = name
                    idSurvey.setOnClickListener {
                        if (idSurvey.isChecked) {

                            if (!check_string.contains(name)) {
                                if (check_string.size <= 2) {
                                    Log.e("checkData: ", check_string.size.toString())
                                    check_string.add(name)

                                    Log.e("checkData: ", check_string.toString())
                                } else {
                                    Log.e("else: ", check_string[0])
                                    check_string.removeFirst()
                                    Log.e("else: ", check_string.toString())
                                }
                            }
                        } else {
                            check_string.remove(name)
                            Log.e("else: ", check_string.toString())
                        }
                    }
                    idSurvey.isChecked = check_string.contains(name)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyAdapter.MyViewHolder {
        val view = ItemSurveyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun checkData(id: Int, text: CharSequence, checked: Boolean) {
        if (checked) {
            if (!check_string.contains(text)) {
                if (check_string.size <= 1) {
                    Log.e("checkData: ", check_string.size.toString())
                    check_string.add(text.toString())
                    check_id.add(id)
                    Log.e("checkData: ", check_string.toString())
                } else {
//                    val indkes = check_id[0]
//                    checkbox = findViewById(indkes)
//                    checkbox.isChecked = false
                    Log.e("checkData: ", check_string[0])
                    check_string.removeFirst()
                    check_id.removeFirst()

                    check_string.add(text.toString())
                    check_id.add(id)
                    Log.e("else: ", check_string.toString())
                }
            }
        } else {
            check_string.remove(text)
            check_id.remove(id)
            Log.e("else: ", check_string.toString())
        }
    }
}