package com.example.finebyme.ui.photoDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.ui.photoList.PhotoListViewModel

class PhotoDetailViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : AndroidViewModel(application) {

    private val _transformedPhoto = MutableLiveData<Photo>()
    val transformedPhoto: LiveData<Photo> get() = _transformedPhoto

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _state: MutableLiveData<State> by lazy { MutableLiveData() }
    val state: LiveData<State> get() = _state

    init {
        _state.value = State.LOADING
    }

    fun onPhotoLoadCompleted() {
        _state.value = State.DONE
    }

    fun onPhotoLoadFail() {
        _state.value = State.ERROR
    }

    fun onEntryScreen(photo: Photo) {
        val transformTitle = transformTitle(photo.title)
        photo.title = transformTitle
        _transformedPhoto.value = photo
        _isFavorite.value = isPhotoFavorite(photo.id)
    }

    fun isPhotoFavorite(id: String):Boolean {
        return favoritePhotosRepository.isPhotoFavorite(id)
    }

    fun toggleFavorite(photo: Photo) {
        if(isPhotoFavorite(photo.id)) {
            favoritePhotosRepository.deletePhoto(photo)
            _isFavorite.value = false
        } else {
            favoritePhotosRepository.insertPhoto(photo)
            _isFavorite.value = true
        }
    }

    private fun transformTitle(title: String): String {
        val parts = title.split("-")

        // 제목의 마지막 부분이 영어로 시작되거나 하이픈("-")으로 시작되는지 확인하기 위한 정규 표현식
        val lastWordFormat = "[a-zA-Z0-9_-]+\$".toRegex()

        // 마지막 부분이 영어형식이 맞는지 확인하고, 맞다면 제외
        val filteredParts = if (parts.isNotEmpty() && lastWordFormat.matches(parts.last())) {
            parts.dropLast(1)
        } else {
            parts
        }

        return filteredParts.joinToString(" ")
    }
}