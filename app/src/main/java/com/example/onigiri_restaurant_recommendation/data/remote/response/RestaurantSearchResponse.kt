package com.example.onigiri_restaurant_recommendation.data.remote.response

data class ListRestaurantSearchResponse(
    val results: List<RestaurantSearchResponse>,
    val token: String?
)

data class RestaurantSearchResponse (
    val business_status: String ="",
    val geometry: ListGeometryResponse,
    val icon: String ="",
    val icon_background_color: String ="",
    val icon_mask_base_uri: String="",
    val name: String="",
    val opening_hours: ListOpeningHoursReponse,
    val photos: List<ListPhotosResponse>,
    val photo_url: String="",
    val place_id: String="",
    val plus_code: ListPlusCodeResponse,
    val price_level: Int=0,
    val rating: Double=0.0,
    val reference: String="",
    val scope: String="",
    val types: List<String>,
    val user_ratings_total: Int=0,
    val vicinity: String="",
    val range: Double=0.0
)

data class ListGeometryResponse(
    val location: ListLocationResponse,
    val viewport: ListViewportResponse,
)

data class ListLocationResponse(
    val lat: Double=0.0,
    val lng: Double=0.0
)

data class ListViewportResponse(
    val northeast: ListNortheastResponse,
    val southwest: ListSouthwestResponse
)

data class ListNortheastResponse(
    val lat: Double=0.0,
    val lng: Double=0.0
)

data class ListSouthwestResponse(
    val lat: Double=0.0,
    val lng: Double=0.0
)

data class ListOpeningHoursReponse(
    val open_now: Boolean,
    val periods: List<ListPeriodsResponse>?,
    val weekday_text: List<String>?
)

data class ListPhotosResponse(
    val height: Int=0,
    val html_attributions: List<String>,
    val photo_reference: String="",
    val width: Int=0
)

data class ListPlusCodeResponse(
    val compound_code: String="",
    val global_code: String=""
)

data class ListPeriodsResponse(
    val close: ListCloseResponse,
    val open: ListOpenResponse
)

data class ListCloseResponse(
    val day: Int=0,
    val time: String=""
)

data class ListOpenResponse(
    val day: Int=0,
    val time: String=""
)