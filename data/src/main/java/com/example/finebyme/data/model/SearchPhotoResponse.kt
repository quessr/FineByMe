package com.example.finebyme.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPhotoResponse(
    @Json(name = "results") val results: List<UnsplashPhoto>
)
