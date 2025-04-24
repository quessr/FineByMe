package com.example.finebyme.presentation.favoriteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.usecase.GetFavoritePhotoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository
    private val getFavoritePhotoListUseCase: GetFavoritePhotoListUseCase
) : ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> get() = _photos

    init {
        viewModelScope.launch {
            getFavoritePhotoListUseCase.execute().collect { photoList ->
                _photos.value = photoList
            }
        }
//        loadFavoritePhotos()
    }

    fun onResumeScreen() {
//        loadFavoritePhotos()
    }

//    private fun loadFavoritePhotos() {
////        _photos.value = photoRepository.getFavoritePhotoList()
//        viewModelScope.launch {
//            _photos.value = getFavoritePhotoListUseCase.execute()
//        }
//    }

}