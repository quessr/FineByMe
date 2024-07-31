package com.example.finebyme.domain.repositoryInterface

import com.example.finebyme.domain.entity.Photo

interface PhotoRepository {
    suspend fun getRandomPhotoList(): Result<List<Photo>>
    suspend fun getSearchPhotoList(query: String): Result<List<Photo>>

    fun getFavoritePhotoList(): List<Photo>
    fun addPhotoToFavorites(photo: Photo)
    fun removePhotoFromFavorites(photo: Photo)
    fun isPhotoFavorite(photoId: String): Boolean

}