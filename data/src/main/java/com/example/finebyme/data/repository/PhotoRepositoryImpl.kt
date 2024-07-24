package com.example.finebyme.data.repository

import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.datasource.UserDataSource
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

open class PhotoRepositoryImpl @Inject constructor(
    private val unsplashDataSource: UnSplashDataSource,
    private val userDataSource: UserDataSource
): PhotoRepository {


    override fun getRandomPhotoList(onResult: (Result<List<Photo>>) -> Unit) {
        // unsplashDataSource.getRandomPhotoList(onResult)
    }

    override fun getSearchPhotoList(query: String, onResult: (Result<List<Photo>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getFavoritePhotoList(): List<Photo> {
        TODO("Not yet implemented")
    }

    override fun addPhotoToFavorites(photo: Photo) {
        TODO("Not yet implemented")
    }

    override fun removePhotoFromFavorites(photo: Photo) {
        TODO("Not yet implemented")
    }

    override fun isPhotoFavorite(photoId: String): Boolean {
        TODO("Not yet implemented")
    }


//    fun getRandomPhotoList(onResult: (Result<List<UnsplashPhoto>>?) -> Unit) {
//        unsplashDataSource.getRandomPhotoList(onResult)
//    }
//
//    fun getSearchPhotoList(query: String, onResult: (Result<List<UnsplashPhoto>>?) -> Unit) {
//        return unsplashDataSource.getSearchPhotoList(query, onResult)
//    }

//    fun getFavoritePhotoList(): List<Photo> {
//        return userDataSource.getAllPhotos()
//    }
//
//    fun getFavoritePhoto(id: String): Photo {
//        return userDataSource.getPhoto(id)
//    }
//
//    fun addPhotoToFavorites(photo: Photo) {
//        userDataSource.insertPhoto(photo)
//    }
//
//    fun removePhotoFromFavorites(photo: Photo) {
//        userDataSource.deletePhoto(photo)
//    }

    //    fun isPhotoFavorite(photoId: String): Boolean {
//        return userDataSource.isPhotoFavorite(photoId)
//    }

}