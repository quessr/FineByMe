package com.example.finebyme.ui.photoList

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.common.enums.ErrorType
import com.example.finebyme.common.enums.LoadingState
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.base.BaseViewModel
import com.example.finebyme.utils.SnackbarUtils
import java.io.IOException
import java.net.UnknownHostException

class PhotoListViewModel(
    application: Application,
    private val photoRepository: PhotoRepository,
) : BaseViewModel(application) {

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _loadingState: MutableLiveData<LoadingState> by lazy { MutableLiveData() }
    val loadingState: LiveData<LoadingState> get() = _loadingState

    private val _errorMassage: MutableLiveData<String> by lazy { MutableLiveData() }
    val errorMessage: LiveData<String> get() = _errorMassage

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
        val message = when (throwable) {
            is IOException -> {
                Log.e("PhotoListViewModel", "Network error: ${throwable.message}")
                // Show network error message
                ErrorType.NETWORK_ERROR.message
            }

            is retrofit2.HttpException -> {
                val message = when (throwable.code()) {
                    400 -> ErrorType.BAD_REQUEST.message
                    401 -> ErrorType.UNAUTHORIZED.message
                    403 -> ErrorType.FORBIDDEN.message
                    404 -> ErrorType.NOT_FOUND.message
                    500, 503 -> ErrorType.SERVER_ERROR.message
                    else -> "HTTP error: ${throwable.message()}"
                }
                Log.e("PhotoListViewModel", "HTTP error: ${throwable.message}")
                message
            }

            is UnknownHostException -> {
                Log.e("PhotoListViewModel", "No internet connection: ${throwable.message}")
                // Show no internet connection message
                ErrorType.NO_INTERNET.message
            }

            else -> {
                Log.e("PhotoListViewModel", "Unknown error: ${throwable.message}")
                // Show generic error message
                ErrorType.UNKNOWN_ERROR.message
            }
        }
        _errorMassage.postValue(message)
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
