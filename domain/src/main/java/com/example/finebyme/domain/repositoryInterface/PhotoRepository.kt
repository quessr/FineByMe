package com.example.finebyme.domain.repositoryInterface

import com.example.finebyme.domain.entity.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getRandomPhotoList(): Result<List<Photo>>
    suspend fun getSearchPhotoList(query: String): Result<List<Photo>>

    fun getFavoritePhotoList(): Flow<List<Photo>>
    fun addPhotoToFavorites(photo: Photo)
    fun removePhotoFromFavorites(photo: Photo)
    fun isPhotoFavorite(photoId: String): Boolean
}