package com.example.onigiri_restaurant_recommendation.remote.response

import com.google.gson.annotations.SerializedName

data class FavoritePostAndDelResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
