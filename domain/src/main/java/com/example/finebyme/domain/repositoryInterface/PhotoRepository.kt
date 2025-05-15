package com.example.finebyme.domain.repositoryInterface

import androidx.paging.PagingData
import com.example.finebyme.domain.entity.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getRandomPhotoList(page: Int, perPage: Int): Result<List<Photo>>
    suspend fun getSearchPhotoList(query: String, page: Int, perPage: Int): Result<List<Photo>>

    fun getFavoritePhotoList(): Flow<List<Photo>>
    fun addPhotoToFavorites(photo: Photo)
    fun removePhotoFromFavorites(photo: Photo)
    fun isPhotoFavorite(photoId: String): Boolean

    fun getPhotoPagingList(query: String): Flow<PagingData<Photo>>
}