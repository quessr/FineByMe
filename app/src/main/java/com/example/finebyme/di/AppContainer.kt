package com.example.finebyme.di

import android.content.Context
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.data.repository.FavoritePhotosRepositoryImpl

interface AppContainer {
    val favoritePhotosRepository: FavoritePhotosRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val favoritePhotosRepository: FavoritePhotosRepository by lazy {
        FavoritePhotosRepositoryImpl(FavoritePhotosDatabase.getDatabase(context).PhotoDao())
    }
}