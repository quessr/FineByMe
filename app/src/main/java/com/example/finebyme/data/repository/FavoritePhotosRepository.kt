package com.example.finebyme.data.repository

import com.example.finebyme.data.db.Photo

interface FavoritePhotosRepository {
    fun getAllPhotos(): List<Photo>

    fun getPhoto(id: String): Photo

    fun insertPhoto(photo: Photo)

    fun deletePhoto(photo: Photo)

    fun updatePhoto(photo: Photo)
}