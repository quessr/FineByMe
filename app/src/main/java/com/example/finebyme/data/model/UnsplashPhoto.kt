package com.example.finebyme.data.model

import com.squareup.moshi.Json

data class UnsplashPhoto(
    @Json(name = "id") val id: String,
    @Json(name = "alternative_slugs") val title: AlternativeSlugs?,
    @Json(name = "description") val description: String?,
    @Json(name = "urls") val urls: Urls,
)

data class Urls(
    @Json(name = "full") val full: String,
    @Json(name = "thumb") val thumb: String
)

data class AlternativeSlugs(
    @Json(name = "ko") val ko: String
)