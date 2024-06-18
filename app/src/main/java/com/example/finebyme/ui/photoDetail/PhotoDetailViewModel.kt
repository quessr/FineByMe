package com.example.finebyme.ui.photoDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.ui.photoList.PhotoListViewModel

class PhotoDetailViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : AndroidViewModel(application) {
    enum class State {
        LOADING,
        DONE,
        ERROR
    }
    private val context = getApplication<Application>().applicationContext

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
//        favoritePhotosRepository.insertPhoto(photo)
//        _isFavorite.value = !(_isFavorite.value ?: false)
    }

    private fun transformTitle(title: String): String {
        return title.replace("-", " ")
            .split(" ")
            .dropLast(1)
            .joinToString(" ")
    }
}