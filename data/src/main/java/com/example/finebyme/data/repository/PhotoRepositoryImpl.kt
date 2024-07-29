package com.example.finebyme.data.repository

import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.datasource.UserDataSource
import com.example.finebyme.data.mapper.PhotoMapper
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

open class PhotoRepositoryImpl @Inject constructor(
    private val unsplashDataSource: UnSplashDataSource,
    private val userDataSource: UserDataSource
) : PhotoRepository {


    override fun getRandomPhotoList(onResult: (Result<List<Photo>>) -> Unit) {
        unsplashDataSource.getRandomPhotoList { result ->
            result?.onSuccess { unsplashPhotos ->
                val photos = PhotoMapper.mapToPhotoList(unsplashPhotos)
                onResult(Result.success(photos))
            }?.onFailure { throwable ->
                onResult(Result.failure(throwable))
            }
        }
    }

    override fun getSearchPhotoList(query: String, onResult: (Result<List<Photo>>) -> Unit) {
        unsplashDataSource.getSearchPhotoList(query) { result ->
            result?.onSuccess { unsplashPhtos ->
                val photos = PhotoMapper.mapToPhotoList(unsplashPhtos)
                onResult(Result.success(photos))
            }?.onFailure { throwable ->
                onResult(Result.failure(throwable))
            }
        }
    }

    override fun getFavoritePhotoList(): List<Photo> {
        val dataPhotos = userDataSource.getAllPhotos()
        return PhotoMapper.mapToDomainPhotoList(dataPhotos)
    }

    override fun addPhotoToFavorites(photo: Photo) {
        val dataPhoto = PhotoMapper.mapToDataPhoto(photo)
        userDataSource.insertPhoto(dataPhoto)
    }

    override fun removePhotoFromFavorites(photo: Photo) {
        val dataPhoto = PhotoMapper.mapToDataPhoto(photo)
        userDataSource.deletePhoto(dataPhoto)
    }

    override fun isPhotoFavorite(photoId: String): Boolean {
        return userDataSource.isPhotoFavorite(photoId)
    }
}