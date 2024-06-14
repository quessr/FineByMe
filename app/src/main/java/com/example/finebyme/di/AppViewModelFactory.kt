package com.example.finebyme.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.ui.favoriteList.FavoriteListViewModel
import com.example.finebyme.ui.photoDetail.PhotoDetailViewModel
import com.example.finebyme.ui.photoList.PhotoListViewModel

class AppViewModelFactory(
    private val application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteListViewModel::class.java) -> {
                FavoriteListViewModel(application) as T
            }

            modelClass.isAssignableFrom(PhotoListViewModel::class.java) -> {
                favoritePhotosRepository?.let { PhotoListViewModel(application, it) } as T
            }

            modelClass.isAssignableFrom(PhotoDetailViewModel::class.java) -> {
                favoritePhotosRepository?.let { PhotoDetailViewModel(application, it) } as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}