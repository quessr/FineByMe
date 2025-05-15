package com.example.finebyme.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.datasource.UserDataSource
import com.example.finebyme.data.datasource.paging.PhotoPagingSource
import com.example.finebyme.data.mapper.PhotoMapper
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val unsplashDataSource: UnSplashDataSource,
    private val userDataSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotoRepository {


    override suspend fun getRandomPhotoList(page: Int, perPage: Int): Result<List<Photo>> {
        return withContext(ioDispatcher) {
            try {
                val response = unsplashDataSource.getRandomPhotoList(page, perPage)
                val photoList = PhotoMapper.mapToPhotoList(response)
                Result.success(photoList)
//                result.map { unsplashPhotos -> PhotoMapper.mapToPhotoList(unsplashPhotos) }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getSearchPhotoList(
        query: String,
        page: Int,
        perPage: Int
    ): Result<List<Photo>> {
        return withContext(ioDispatcher) {
            try {
                val response = unsplashDataSource.getSearchPhotoList(query, page, perPage)
                val photoList = PhotoMapper.mapToPhotoList(response)
                Result.success(photoList)
//                result.map { unsplashPhtos -> PhotoMapper.mapToPhotoList(unsplashPhtos) }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun getFavoritePhotoList(): Flow<List<Photo>> {
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

    override fun getPhotoPagingList(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PhotoPagingSource(unsplashDataSource, query) }
        ).flow
    }
}