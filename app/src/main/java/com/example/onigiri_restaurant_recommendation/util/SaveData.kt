package com.example.onigiri_restaurant_recommendation.util

import android.content.Context
import android.content.SharedPreferences

class SaveData(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)

    fun setTokenFcm(token: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("Dark", token)
        editor.apply()
    }

    fun getTokenFcm(): String? {
        val token: String? = sharedPref.getString("Token", "")
        return token
    }
}