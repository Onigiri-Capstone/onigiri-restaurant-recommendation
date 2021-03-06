package com.example.onigiri_restaurant_recommendation.data.remote.response

data class ErrorListRestaurantDetailResponse(
    val results: ErrorRestaurantDetailResponse,
    val app_reviews: List<String>
)

data class ErrorRestaurantDetailResponse(
    val address_components: List<ListAddressComponentsResponse>,
    val adr_address: String,
    val business_status: String,
    val formatted_address: String,
    val formatted_phone_number: String,
    val geometry: ListGeometryResponse,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val international_phone_number: String,
    val name: String,
    val opening_hours: ListOpeningHoursReponse,
    val url: String,
    val photo_url: String,
    val photos: List<ListPhotosResponse>,
    val place_id: String,
    val plus_code: ListPlusCodeResponse,
    val price_level: Int,
    val rating: Float,
    val reference: String,
    val range: Double = 0.0
)