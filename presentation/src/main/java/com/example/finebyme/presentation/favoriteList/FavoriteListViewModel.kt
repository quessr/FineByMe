package com.example.finebyme.presentation.favoriteList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : com.example.finebyme.presentation.base.BaseViewModel() {
    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    init {
        loadFavoritePhotos()
    }

    fun onResumeScreen() {
        loadFavoritePhotos()
    }

    private fun loadFavoritePhotos() {
        _photos.value = photoRepository.getFavoritePhotoList()
    }

}