package com.example.finebyme.presentation.photoList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.usecase.GetRandomPhotoListUseCase
import com.example.finebyme.domain.usecase.GetSearchPhotoListUseCase
import com.example.finebyme.presentation.base.BaseViewModel
import com.example.finebyme.presentation.common.enums.LoadingState
import com.example.finebyme.presentation.utils.ErrorHandler.handleFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository,
    private val getRandomPhotoListUseCase: GetRandomPhotoListUseCase,
    private val getSearchPhotoListUseCase: GetSearchPhotoListUseCase
) : BaseViewModel() {

    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    private val _loadingState: MutableLiveData<LoadingState> by lazy { MutableLiveData() }
    val loadingState: LiveData<LoadingState> get() = _loadingState

    private val _errorMassage: MutableLiveData<String> by lazy { MutableLiveData() }
    val errorMessage: LiveData<String> get() = _errorMassage

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<Photo> = emptyList()

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        _loadingState.postValue(LoadingState.LOADING)
        Log.d("PhotoListViewModel", "Starting to fetch photos")
//        getRandomPhotoListUseCase.execute { result ->
//            Log.d("PhotoListViewModel", "Starting to fetch photos $result, ${result?.size}")
//            _photos.postValue(result)
//            if (result != null) {
//                cachedPhotos = result
//            }
//            _loadingState.postValue(LoadingState.DONE)
//
//
//        }

        getRandomPhotoListUseCase.execute { result ->
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
        getSearchPhotoListUseCase.execute(query) { result -> }

//        photoRepository.getSearchPhotoList(query) { result ->
//            result?.onSuccess { photos ->
//                Log.d("PhotoListViewModel", "Received response: $photos")
//                _photos.postValue(photos)
//                _loadingState.postValue(LoadingState.DONE)
//            }?.onFailure { throwable ->
//                Log.d("PhotoListViewModel", "Search failed or no results found")
//                val errorMessage = handleFailure(throwable)
//                _errorMassage.postValue(errorMessage)
//                _photos.postValue(emptyList())
//                _loadingState.postValue(LoadingState.ERROR)
//            }
//        }
    }
}
