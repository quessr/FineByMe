package com.example.finebyme.data.model

import android.content.Context
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

fun UnsplashPhoto.toPhoto(context: Context): com.example.finebyme.data.db.Photo {
    return com.example.finebyme.data.db.Photo(
        id = id,
        title = title?.ko ?: "aaa",//context.getString(R.string.photo_no_title),
        description = description ?: "bbb",//context.getString(R.string.photo_no_description),
        fullUrl = urls.full,
        thumbUrl = urls.thumb
    )
}

// List<UnsplashPhoto>를 List<Photo>로 변환하는 함수
fun List<UnsplashPhoto>.toPhotoList(context: Context): List<com.example.finebyme.data.db.Photo> {
    return this.map { it.toPhoto(context) }
}