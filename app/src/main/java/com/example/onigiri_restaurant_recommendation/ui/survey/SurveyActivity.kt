package com.example.onigiri_restaurant_recommendation.ui.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.databinding.ActivitySurveyBinding

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var checkbox: CheckBox
    private var checkString = arrayListOf<String>()
    private var checkId = arrayListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCheckBox()
    }

    private fun setCheckBox() {
        binding.apply {
            idSweets.setOnClickListener {
                checkData(R.id.id_sweets, idSweets.text, idSweets.isChecked)
            }
            idRice.setOnClickListener {
                checkData(R.id.id_rice, idRice.text, idRice.isChecked)
            }
            idMeatball.setOnClickListener {
                checkData(R.id.id_meatball, idMeatball.text, idMeatball.isChecked)
            }
            idChicken.setOnClickListener {
                checkData(R.id.id_chicken, idChicken.text, idChicken.isChecked)
            }
            idDrinks.setOnClickListener {
                checkData(R.id.id_drinks, idDrinks.text, idDrinks.isChecked)
            }
            idCoffee.setOnClickListener {
                checkData(R.id.id_coffee, idCoffee.text, idCoffee.isChecked)
            }
            idSeafood.setOnClickListener {
                checkData(R.id.id_seafood, idSeafood.text, idSeafood.isChecked)
            }
            idWestern.setOnClickListener {
                checkData(R.id.id_western, idWestern.text, idWestern.isChecked)
            }
            idNoodle.setOnClickListener {
                checkData(R.id.id_noodle, idNoodle.text, idNoodle.isChecked)
            }
            idChinese.setOnClickListener {
                checkData(R.id.id_chinese, idChinese.text, idChinese.isChecked)
            }
            idIndian.setOnClickListener {
                checkData(R.id.id_indian, idIndian.text, idIndian.isChecked)
            }
            idJapanese.setOnClickListener {
                checkData(R.id.id_japanese, idJapanese.text, idJapanese.isChecked)
            }
            idMiddleEast.setOnClickListener {
                checkData(R.id.id_middle_east, idMiddleEast.text, idMiddleEast.isChecked)
            }
            idThai.setOnClickListener {
                checkData(R.id.id_thai, idThai.text, idThai.isChecked)
            }
        }
    }

    private fun checkData(id: Int, text: CharSequence, checked: Boolean) {
        if (checked) {
            if (!checkId.contains(id)) {
                if (checkString.size <= 2) {
                    Log.e("checkData: ", checkString.size.toString())
                    checkString.add(text.toString())
                    checkId.add(id)
                    Log.e("checkData: ", checkString.toString())
                } else {
                    val indeks = checkId[0]
                    checkbox = findViewById(indeks)
                    checkbox.isChecked = false
                    Log.e("checkData: ", checkString[0])
                    checkString.removeFirst()
                    checkId.removeFirst()

                    checkString.add(text.toString())
                    checkId.add(id)
                    Log.e("else: ", checkString.toString())
                }
            }
        } else {
            checkString.remove(text)
            checkId.remove(id)
            Log.e("else: ", checkString.toString())
        }
    }
}
