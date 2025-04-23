package com.example.finebyme.presentation.photoList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.usecase.GetRandomPhotoListUseCase
import com.example.finebyme.domain.usecase.GetSearchPhotoListUseCase
import com.example.finebyme.presentation.common.enums.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository,
    private val getRandomPhotoListUseCase: GetRandomPhotoListUseCase,
    private val getSearchPhotoListUseCase: GetSearchPhotoListUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> get() = _photos

    private val _loadingState = MutableStateFlow<LoadingState?>(null)
    val loadingState: StateFlow<LoadingState?> get() = _loadingState

    private val _errorMassage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMassage

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<Photo> = emptyList()

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        _loadingState.value = LoadingState.LOADING
        Log.d("PhotoListViewModel", "Starting to fetch photos")

        viewModelScope.launch {
            val result = getRandomPhotoListUseCase.execute()
            result?.onSuccess { photos ->
                Log.d("PhotoListViewModel", "Photos fetched successfully")
                _photos.value = photos
                cachedPhotos = photos
                _loadingState.value = LoadingState.DONE
            }?.onFailure { throwable ->
                Log.e("PhotoListViewModel", "Failed to fetch photos: ${throwable.message}")
                val errorMessage = throwable.message ?: "Unknown error"
                _errorMassage.value = errorMessage
                _photos.value = listOf()
                _loadingState.value = LoadingState.ERROR
            }
        }


//        getRandomPhotoListUseCase.execute { result ->
//            result?.onSuccess { photos ->
//                Log.d("PhotoListViewModel", "Photos fetched successfully")
//                _photos.postValue(photos)
//                cachedPhotos = photos
//                _loadingState.postValue(LoadingState.DONE)
//            }?.onFailure { throwable ->
//                Log.e("PhotoListViewModel", "Failed to fetch photos: ${throwable.message}")
//                val errorMessage = throwable.message ?: "Unknown error"
//                _errorMassage.postValue(errorMessage)
//                _photos.postValue(listOf())
//                _loadingState.postValue(LoadingState.ERROR)
//            }
//        }
    }

    fun searchPhotos(query: String) {
        // 검색어가 비어있을 때 캐시된 사진 목록을 다시 설정
        if (query.isEmpty()) {
            _photos.value = cachedPhotos
            _loadingState.value = LoadingState.DONE
            return
        }

        _loadingState.value = LoadingState.LOADING
        Log.d("PhotoListViewModel", "Searching for photos with query: $query")

        viewModelScope.launch {
            val result = getSearchPhotoListUseCase.execute(query)
            result?.onSuccess { photos ->
                Log.d("PhotoListViewModel", "Received response: $photos")
                _photos.value = photos
                _loadingState.value = LoadingState.DONE
            }?.onFailure { throwable ->
                Log.d("PhotoListViewModel", "Search failed or no results found")
                val errorMessage = throwable.message
                if (errorMessage != null) {
                    _errorMassage.value = errorMessage
                }
                _photos.value = emptyList()
                _loadingState.value = LoadingState.ERROR
            }
        }

//        getSearchPhotoListUseCase.execute(query) { result ->
//            result?.onSuccess { photos ->
//                Log.d("PhotoListViewModel", "Received response: $photos")
//                _photos.postValue(photos)
//                _loadingState.postValue(LoadingState.DONE)
//            }?.onFailure { throwable ->
//                Log.d("PhotoListViewModel", "Search failed or no results found")
//                val errorMessage = throwable.message
//                _errorMassage.postValue(errorMessage)
//                _photos.postValue(emptyList())
//                _loadingState.postValue(LoadingState.ERROR)
//            }
//        }
    }
}
