package com.example.finebyme.presentation.favoriteList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.usecase.GetFavoritePhotoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository
    private val getFavoritePhotoListUseCase: GetFavoritePhotoListUseCase
) : ViewModel() {
    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    init {
        loadFavoritePhotos()
    }

    fun onResumeScreen() {
        loadFavoritePhotos()
    }

    private fun loadFavoritePhotos() {
//        _photos.value = photoRepository.getFavoritePhotoList()
        viewModelScope.launch {
            _photos.value = getFavoritePhotoListUseCase.execute()
        }
    }

}