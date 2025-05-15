package com.example.finebyme.presentation.photoList

import androidx.lifecycle.ViewModel
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.usecase.GetPhotoPagingUseCase
import com.example.finebyme.presentation.common.enums.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository,
//    private val getRandomPhotoListUseCase: GetRandomPhotoListUseCase,
//    private val getSearchPhotoListUseCase: GetSearchPhotoListUseCase,
    private val getPhotoPagingUseCase: GetPhotoPagingUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> get() = _photos

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _loadingState = MutableStateFlow<LoadingState?>(null)
    val loadingState: StateFlow<LoadingState?> get() = _loadingState

    private val _errorMassage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMassage

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<Photo> = emptyList()

//    init {
//        fetchPhotos()
//        observeSearchQuery()
//    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val photoPagingFlow = _searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            getPhotoPagingUseCase(query)
        }



//    private fun fetchPhotos() {
//        _loadingState.value = LoadingState.LOADING
//        Log.d("PhotoListViewModel", "Starting to fetch photos")
//
//        viewModelScope.launch {
//            val result = getRandomPhotoListUseCase.execute()
//            result?.onSuccess { photos ->
//                Log.d("PhotoListViewModel", "Photos fetched successfully")
//                _photos.value = photos
//                cachedPhotos = photos
//                _loadingState.value = LoadingState.DONE
//                _searchQuery.value = ""
//            }?.onFailure { throwable ->
//                Log.e("PhotoListViewModel", "Failed to fetch photos: ${throwable.message}")
//                val errorMessage = throwable.message ?: "Unknown error"
//                _errorMassage.value = errorMessage
//                _photos.value = listOf()
//                _loadingState.value = LoadingState.ERROR
//            }
//        }
//    }
//
//    @OptIn(FlowPreview::class)
//    private fun observeSearchQuery() {
//        viewModelScope.launch {
//            _searchQuery
//                .debounce(500)
//                .distinctUntilChanged()
//                .collect { query ->
//                    Log.d("PhotoListViewModel", "Debounced query: $query")
//
//                    if (query.isEmpty()) {
//                        _photos.value = cachedPhotos
//                        _loadingState.value = LoadingState.DONE
//                    } else {
//                        searchPhotos(query)
//                    }
//                }
//        }
//    }
//
//    private fun searchPhotos(query: String) {
//        _loadingState.value = LoadingState.LOADING
//        Log.d("PhotoListViewModel", "Searching for photos with query: $query")
//
//        viewModelScope.launch {
//            val result = getSearchPhotoListUseCase.execute(query)
//            result?.onSuccess { photos ->
//                Log.d("PhotoListViewModel", "Received response: $photos")
//                _photos.value = photos
//                _loadingState.value = LoadingState.DONE
//            }?.onFailure { throwable ->
//                Log.d("PhotoListViewModel", "Search failed or no results found")
//                val errorMessage = throwable.message
//                if (errorMessage != null) {
//                    _errorMassage.value = errorMessage
//                }
//                _photos.value = emptyList()
//                _loadingState.value = LoadingState.ERROR
//            }
//        }
//    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}


