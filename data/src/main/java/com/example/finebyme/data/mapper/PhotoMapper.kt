package com.example.finebyme.data.mapper

import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.domain.entity.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PhotoMapper {
    private fun mapToPhoto(unsplashPhoto: UnsplashPhoto): Photo =
        unsplashPhoto.run {
            Photo(
                id = id,
                title = title?.ko ?: "",
                description = description,
                fullUrl = urls.full,
                thumbUrl = urls.thumb,
                width = width,
                height = height
            )
        }

    fun mapToPhotoList(unsplashPhotoList: Result<List<UnsplashPhoto>>): List<Photo> =
        unsplashPhotoList.fold(
            onSuccess = { list -> list.map { mapToPhoto(it) } },
            onFailure = { emptyList() }
        )


    private fun mapToDomainPhoto(dataPhoto: com.example.finebyme.data.db.Photo): Photo =
        dataPhoto.run {
            Photo(
                id = id,
                title = title,
                description = description,
                fullUrl = fullUrl,
                thumbUrl = thumbUrl,
                width = width,
                height = height
            )
        }

    fun mapToDomainPhotoList(dataPhotoFlow: Flow<List<com.example.finebyme.data.db.Photo>>): Flow<List<Photo>> =
        dataPhotoFlow.map { dataList ->
            dataList.map { dataPhoto -> mapToDomainPhoto(dataPhoto) }
        }

    fun mapToDataPhoto(domainPhoto: Photo): com.example.finebyme.data.db.Photo =
        domainPhoto.run {
            com.example.finebyme.data.db.Photo(
                id = id,
                title = title,
                description = description,
                fullUrl = fullUrl,
                thumbUrl = thumbUrl,
                inputAt = System.currentTimeMillis(), // 필요한 경우 적절한 값을 설정
                width = width,
                height = height
            )
        }

    fun mapToDataPhotoList(domainPhotoList: List<Photo>): List<com.example.finebyme.data.db.Photo> =
        domainPhotoList.map { mapToDataPhoto(it) }
}