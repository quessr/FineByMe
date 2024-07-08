package com.example.finebyme.ui.photoList

import android.app.Application
import android.net.http.HttpException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.base.BaseViewModel
import java.io.IOException
import java.net.UnknownHostException

class PhotoListViewModel(
    application: Application,
    private val photoRepository: PhotoRepository,
) : BaseViewModel(application) {

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _loadingState: MutableLiveData<State> by lazy { MutableLiveData() }
    val loadingState: LiveData<State> get() = _loadingState

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<UnsplashPhoto> = emptyList()

    init {
        fetchPhotos()
    }


    private fun fetchPhotos() {
        _loadingState.postValue(State.LOADING)
        Log.d("PhotoListViewModel", "Starting to fetch photos")
        photoRepository.getRandomPhotoList { result ->
            result?.onSuccess { photos ->
                Log.d("PhotoListViewModel", "Photos fetched successfully")
                _photos.postValue(photos)
                cachedPhotos = photos
                _loadingState.postValue(State.DONE)
            }?.onFailure { throwable ->
                Log.e("PhotoListViewModel", "Failed to fetch photos: ${throwable.message}")
                handleFailure(throwable)
                _photos.postValue(listOf())
                _loadingState.postValue(State.ERROR)
            }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        when (throwable) {
            is IOException -> {
                Log.e("PhotoListViewModel", "Network error: ${throwable.message}")
                // Show network error message
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Network error. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("PhotoListViewModel", "HTTP error: ${throwable.message}")
            }

            is UnknownHostException -> {
                Log.e("PhotoListViewModel", "No internet connection: ${throwable.message}")
                // Show no internet connection message
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "No internet connection. Please check your connection.",
                    Toast.LENGTH_SHORT
                )
            }

            else -> {
                Log.e("PhotoListViewModel", "Unknown error: ${throwable.message}")
                // Show generic error message
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "An unknown error occurred. Please try again.",
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    fun searchPhotos(query: String) {

        // 검색어가 비어있을 때 캐시된 사진 목록을 다시 설정
        if (query.isEmpty()) {
            _photos.postValue(cachedPhotos)
            _loadingState.postValue(State.DONE)
            return
        }

        _loadingState.postValue(State.LOADING)
        Log.d("PhotoListViewModel", "Searching for photos with query: $query")
        photoRepository.getSearchPhotoList(query) { response ->
            Log.d("PhotoListViewModel", "Received response: $response")
            if (!response.isNullOrEmpty()) {
                Log.d("PhotoListViewModel", "Search successful: ${response.size} results found")
                _photos.postValue(response)
                _loadingState.postValue(State.DONE)
            } else {
                Log.d("PhotoListViewModel", "Search failed or no results found")
                _photos.postValue(emptyList())
                _loadingState.postValue(State.ERROR)
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
