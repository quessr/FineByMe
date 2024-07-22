package com.example.finebyme.domain.repositoryInterface

import com.example.finebyme.domain.entity.Photo

interface PhotoRepository {
    fun getRandomPhotoList(onResult: (List<Photo>?) -> Unit)
    fun getSearchPhotoList(query: String, onResult: (List<Photo>?) -> Unit)

    fun getFavoritePhotoList(): List<Photo>
    fun addPhotoToFavorites(photo: Photo)
    fun removePhotoFromFavorites(photo: Photo)
    fun isPhotoFavorite(photoId: String): Boolean

}