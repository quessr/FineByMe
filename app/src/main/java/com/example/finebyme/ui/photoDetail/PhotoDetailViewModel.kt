package com.example.finebyme.ui.photoDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.db.Photo
import com.example.finebyme.ui.photoList.PhotoListViewModel

class PhotoDetailViewModel : ViewModel() {
    enum class State {
        LOADING,
        DONE,
        ERROR
    }

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
    }

    fun toggleFavorite() {

        _isFavorite.value = !(_isFavorite.value ?: false)
    }

    private fun transformTitle(title: String): String {
        return title.replace("-", " ")
            .split(" ")
            .dropLast(1)
            .joinToString(" ")
    }
}