package com.example.finebyme.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.data.repository.SearchPhotosRepository
import com.example.finebyme.ui.favoriteList.FavoriteListViewModel
import com.example.finebyme.ui.photoDetail.PhotoDetailViewModel
import com.example.finebyme.ui.photoList.PhotoListViewModel

class AppViewModelFactory(
    private val application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository,
    private val searchPhotosRepository: SearchPhotosRepository? = null,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteListViewModel::class.java) -> {
                FavoriteListViewModel(application, favoritePhotosRepository) as T
            }
            modelClass.isAssignableFrom(PhotoListViewModel::class.java) -> {
                searchPhotosRepository?.let {
                    PhotoListViewModel(application, favoritePhotosRepository,
                        it
                    )
                } as T
            }
            modelClass.isAssignableFrom(PhotoDetailViewModel::class.java) -> {
                PhotoDetailViewModel(application, favoritePhotosRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
