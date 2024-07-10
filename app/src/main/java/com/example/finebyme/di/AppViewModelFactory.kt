package com.example.finebyme.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.favoriteList.FavoriteListViewModel
import com.example.finebyme.ui.photoDetail.PhotoDetailViewModel
import com.example.finebyme.ui.photoList.PhotoListViewModel

class AppViewModelFactory(
    private val application: Application,
    private val photoRepository: PhotoRepository? = null,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteListViewModel::class.java) -> {
                photoRepository?.let { FavoriteListViewModel(photoRepository) } as T
            }

            modelClass.isAssignableFrom(PhotoListViewModel::class.java) -> {
                photoRepository?.let {
                    PhotoListViewModel(photoRepository)
                } as T
            }

            modelClass.isAssignableFrom(PhotoDetailViewModel::class.java) -> {
                photoRepository?.let { PhotoDetailViewModel(application, photoRepository) } as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
