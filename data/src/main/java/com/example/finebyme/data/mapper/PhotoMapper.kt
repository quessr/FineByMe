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

    private fun mapToDomainPhoto(dataPhoto: com.example.finebyme.data.db.Photo): Photo =
        dataPhoto.run {
            Photo(
                id = id,
                title = title,
                description = description,
                fullUrl = fullUrl,
                thumbUrl = thumbUrl
            )
        }

    fun mapToDomainPhotoList(dataPhotoList: List<com.example.finebyme.data.db.Photo>): List<Photo> =
        dataPhotoList.map { mapToDomainPhoto(it) }

    fun mapToDataPhoto(domainPhoto: Photo): com.example.finebyme.data.db.Photo =
        domainPhoto.run {
            com.example.finebyme.data.db.Photo(
                id = id,
                title = title,
                description = description,
                fullUrl = fullUrl,
                thumbUrl = thumbUrl,
                inputAt = System.currentTimeMillis() // 필요한 경우 적절한 값을 설정
            )
        }

    fun mapToDataPhotoList(domainPhotoList: List<Photo>): List<com.example.finebyme.data.db.Photo> =
        domainPhotoList.map { mapToDataPhoto(it) }

}