package com.example.finebyme.presentation.favoriteList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import com.example.finebyme.domain.usecase.GetFavoritePhotoListUseCase
import com.example.finebyme.domain.usecase.GetRandomPhotoListUseCase
import com.example.finebyme.domain.usecase.GetSearchPhotoListUseCase
import com.example.finebyme.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
//    private val photoRepository: PhotoRepository
    private val getFavoritePhotoListUseCase: GetFavoritePhotoListUseCase
) : BaseViewModel() {
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
        _photos.value = getFavoritePhotoListUseCase.execute()
    }

}