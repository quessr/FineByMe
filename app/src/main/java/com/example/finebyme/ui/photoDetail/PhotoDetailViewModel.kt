package com.example.finebyme.ui.photoDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.db.Photo

class PhotoDetailViewModel : ViewModel() {
    private val _transformedPhoto = MutableLiveData<Photo>()
    val transformedPhoto: LiveData<Photo> get() = _transformedPhoto

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> get() = _isFavorite

    fun onEntryScreen(photo: Photo) {
        val transformTitle = transformTitle(photo.title)
        val transformedPhoto = photo.copy(title = transformTitle)
        _transformedPhoto.value = transformedPhoto
    }

    fun transformTitle(title: String): String {
        return title.replace("-", " ")
            .split(" ")
            .dropLast(1)
            .joinToString(" ")
    }

    fun toggleFavorite() {
        _isFavorite.value = !(_isFavorite.value ?: false)
    }
}