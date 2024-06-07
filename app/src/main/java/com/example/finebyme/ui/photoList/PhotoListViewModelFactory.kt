package com.example.finebyme.ui.photoList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finebyme.data.repository.FavoritePhotosRepository

class PhotoListViewModelFactory(private val application: Application, private val favoritePhotosRepository: FavoritePhotosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoListViewModel::class.java)) {
            return PhotoListViewModel(application, favoritePhotosRepository) as T
         }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}