package com.example.finebyme.ui.favoriteList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.base.BaseViewModel

class FavoriteListViewModel(
    application: Application,
    private val photoRepository: PhotoRepository
) : BaseViewModel(application) {
    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    private val photoDao = FavoritePhotosDatabase.getDatabase(getApplication<Application>().applicationContext).PhotoDao()

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