package com.example.finebyme.ui.favoriteList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.ui.base.BaseViewModel
import java.lang.Appendable

class FavoriteListViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : BaseViewModel(application) {
    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    val context = getApplication<Application>().applicationContext
    val photoDao = FavoritePhotosDatabase.getDatabase(context).PhotoDao()

    init {
        fetchFavoritePhotos()
    }

    fun onResumeScreen() {
        fetchFavoritePhotos()
    }

    private fun fetchFavoritePhotos() {
        _photos.postValue(photoDao.getAllPhotos())
    }

}