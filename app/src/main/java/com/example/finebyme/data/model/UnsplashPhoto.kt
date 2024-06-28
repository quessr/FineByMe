package com.example.finebyme.data.model

import android.content.Context
import com.example.finebyme.R
import com.example.finebyme.data.db.Photo
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

fun UnsplashPhoto.toPhoto(context: Context): Photo {
    return Photo(
        id = id,
        title = title?.ko ?: context.getString(R.string.photo_no_title),
        description = description ?: context.getString(R.string.photo_no_description),
        fullUrl = urls.full,
        thumbUrl = urls.thumb
    )
}

// List<UnsplashPhoto>를 List<Photo>로 변환하는 함수
fun List<UnsplashPhoto>.toPhotoList(context: Context): List<Photo> {
    return this.map { it.toPhoto(context) }
}