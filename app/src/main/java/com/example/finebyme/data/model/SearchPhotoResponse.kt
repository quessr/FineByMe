package com.example.finebyme.data.model

import com.example.finebyme.data.model.Photo
import com.squareup.moshi.Json

data class SearchPhotoResponse(
    @Json(name = "result") val searchPhotoList: List<Photo>
)
