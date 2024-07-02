package com.example.finebyme.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.datasource.UserDataSource
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.model.SearchPhotoResponse
import com.example.finebyme.data.model.UnsplashPhoto

class PhotoRepository(
    private val unsplashDataSource: UnSplashDataSource,
    private val userDataSource: UserDataSource
) {
    fun getRandomPhotoList(onResult: (List<UnsplashPhoto>?) -> Unit) {
        unsplashDataSource.getRandomPhotoList(onResult)
    }

    fun getSearchPhotoList(query: String, onResult: (List<UnsplashPhoto>?) -> Unit) {
        return unsplashDataSource.getSearchPhotoList(query, onResult)
    }

    fun getFavoritePhotoList(): List<Photo> {
        return userDataSource.getAllPhotos()
    }

    fun getFavoritePhoto(id: String): Photo {
        return userDataSource.getPhoto(id)
    }

    fun addPhotoToFavorites(photo: Photo) {
        userDataSource.insertPhoto(photo)
    }

    fun removePhotoFromFavorites(photo: Photo) {
        userDataSource.deletePhoto(photo)
    }

    fun isPhotoFavorite(photoId: String): Boolean {
        return userDataSource.isPhotoFavorite(photoId)
    }
}