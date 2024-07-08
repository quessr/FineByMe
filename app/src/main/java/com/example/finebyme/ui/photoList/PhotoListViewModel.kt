package com.example.finebyme.ui.photoList

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.common.enums.LoadingState
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.base.BaseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.io.IOException
import java.net.UnknownHostException

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
) : BaseViewModel() {

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _loadingState: MutableLiveData<LoadingState> by lazy { MutableLiveData() }
    val loadingState: LiveData<LoadingState> get() = _loadingState

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<UnsplashPhoto> = emptyList()

    init {
        fetchPhotos()
    }


    private fun fetchPhotos() {
        _loadingState.postValue(LoadingState.LOADING)
        Log.d("PhotoListViewModel", "Starting to fetch photos")
        photoRepository.getRandomPhotoList { result ->
            result?.onSuccess { photos ->
                Log.d("PhotoListViewModel", "Photos fetched successfully")
                _photos.postValue(photos)
                cachedPhotos = photos
                _loadingState.postValue(LoadingState.DONE)
            }?.onFailure { throwable ->
                Log.e("PhotoListViewModel", "Failed to fetch photos: ${throwable.message}")
                handleFailure(throwable)
                _photos.postValue(listOf())
                _loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        when (throwable) {
            is IOException -> {
                Log.e("PhotoListViewModel", "Network error: ${throwable.message}")
                // Show network error message
            }

            is retrofit2.HttpException -> {
                val message = when (throwable.code()) {
                    400 -> "Bad Request: The request was unacceptable, often due to missing a required parameter."
                    401 -> "Unauthorized: Invalid Access Token."
                    403 -> "Forbidden: Missing permissions to perform request."
                    404 -> "Not Found: The requested resource doesn’t exist."
                    500, 503 -> "Server Error: Something went wrong on our end. Please try again later."
                    else -> "HTTP error: ${throwable.message()}"
                }
                Log.e("PhotoListViewModel", "HTTP error: ${throwable.message}")
            }

            is UnknownHostException -> {
                Log.e("PhotoListViewModel", "No internet connection: ${throwable.message}")
                // Show no internet connection message
            }

            else -> {
                Log.e("PhotoListViewModel", "Unknown error: ${throwable.message}")
                // Show generic error message
            }
        }
    }

    fun searchPhotos(query: String) {

        // 검색어가 비어있을 때 캐시된 사진 목록을 다시 설정
        if (query.isEmpty()) {
            _photos.postValue(cachedPhotos)
            _loadingState.postValue(LoadingState.DONE)
            return
        }

        _loadingState.postValue(LoadingState.LOADING)
        Log.d("PhotoListViewModel", "Searching for photos with query: $query")
        photoRepository.getSearchPhotoList(query) { response ->
            Log.d("PhotoListViewModel", "Received response: $response")
            if (!response.isNullOrEmpty()) {
                Log.d("PhotoListViewModel", "Search successful: ${response.size} results found")
                _photos.postValue(response)
                _loadingState.postValue(LoadingState.DONE)
            } else {
                Log.d("PhotoListViewModel", "Search failed or no results found")
                _photos.postValue(emptyList())
                _loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

//    private fun insertFirstPhotoInDb(unsplashPhoto: UnsplashPhoto) {
//
//        context?.let { ctx ->
//            val photo = unsplashPhoto.toPhoto()
//
//            favoritePhotosRepository.insertPhoto(photo)
//
//            val result = FavoritePhotosDatabase.getDatabase(ctx).PhotoDao().getAllPhotos()
//            Log.d("getAllPhotos", "getAllPhotos : ${result.size}")
//            for (item in result) {
//                Log.d("getAllPhotos ==> ", "getAllPhotos : ${item.title}")
//            }
//        }


}
