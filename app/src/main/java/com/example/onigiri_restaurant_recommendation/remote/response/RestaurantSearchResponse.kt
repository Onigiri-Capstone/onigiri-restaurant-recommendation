package com.example.onigiri_restaurant_recommendation.remote.response

data class ListRestaurantSearchResponse(
    val results: List<RestaurantSearchResponse>
)

data class RestaurantSearchResponse (
    val business_status: String,
    val geometry: ListGeometryResponse,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val opening_hours: ListOpeningHoursReponse,
    val photos: List<ListPhotosResponse>,
    val place_id: String,
    val plus_code: ListPlusCodeResponse,
    val price_level: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val user_ratings_total: Int,
    val vicinity: String
)

data class ListGeometryResponse(
    val location: ListLocationResponse,
    val viewport: ListViewportResponse,
)

data class ListLocationResponse(
    val lat: Double,
    val lng: Double
)

data class ListViewportResponse(
    val northeast: ListNortheastResponse,
    val southwest: ListSouthwestResponse
)

data class ListNortheastResponse(
    val lat: Double,
    val lng: Double
)

data class ListSouthwestResponse(
    val lat: Double,
    val lng: Double
)

data class ListOpeningHoursReponse(
    val open_now: Boolean,
    val periods: List<ListPeriodsResponse>?,
    val weekday_text: List<String>?
)

data class ListPhotosResponse(
    val height: Int,
    val html_attributions: List<String>,
    val photo_reference: String,
    val width: Int
)

data class ListPlusCodeResponse(
    val compound_code: String,
    val global_code: String
)

data class ListPeriodsResponse(
    val close: ListCloseResponse,
    val open: ListOpenResponse
)

data class ListCloseResponse(
    val day: Int,
    val time: String
)

data class ListOpenResponse(
    val day: Int,
    val time: String
)