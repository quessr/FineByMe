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
import com.example.finebyme.utils.ErrorHandler.handleFailure
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
                val errorMessage = handleFailure(throwable)
                _errorMassage.postValue(errorMessage)
                _photos.postValue(listOf())
                _loadingState.postValue(LoadingState.ERROR)
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
        photoRepository.getSearchPhotoList(query) { result ->
            result?.onSuccess { photos ->
                Log.d("PhotoListViewModel", "Received response: $photos")
                _photos.postValue(photos)
                _loadingState.postValue(LoadingState.DONE)
            }?.onFailure { throwable ->
                Log.d("PhotoListViewModel", "Search failed or no results found")
                val errorMessage = handleFailure(throwable)
                _errorMassage.postValue(errorMessage)
                _photos.postValue(emptyList())
                _loadingState.postValue(LoadingState.ERROR)
            }
//            Log.d("PhotoListViewModel", "Received response: $response")
//            if (!response.isNullOrEmpty()) {
//                Log.d("PhotoListViewModel", "Search successful: ${response.size} results found")
//                _photos.postValue(response)
//                _loadingState.postValue(LoadingState.DONE)
//            } else {
//                Log.d("PhotoListViewModel", "Search failed or no results found")
//                _photos.postValue(emptyList())
//                _loadingState.postValue(LoadingState.ERROR)
//            }
        }
    }
}
