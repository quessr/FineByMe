package com.example.finebyme.data.mapper

import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.domain.entity.Photo

object PhotoMapper {
    private fun mapToPhoto(unsplashPhoto: UnsplashPhoto): Photo =
        unsplashPhoto.run {
            Photo(
                id = id,
                title = title?.ko ?: "",
                description = description,
                fullUrl = urls.full,
                thumbUrl = urls.thumb
            )
        }

    fun mapToPhotoList(unsplashPhotoList: List<UnsplashPhoto>): List<Photo> =
        unsplashPhotoList.map { mapToPhoto(it) }
}