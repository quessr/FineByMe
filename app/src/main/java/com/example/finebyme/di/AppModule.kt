package com.example.finebyme.di

import android.app.Application
import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.datasource.UserDataSource
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.PhotoDao
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.data.network.RetrofitService
import com.example.finebyme.data.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitService(): RetrofitService {
        return RetrofitInstance.retrofitService
    }

    @Provides
    @Singleton
    fun provideUnsplashDataSource(retrofitService: RetrofitService): UnSplashDataSource {
        return UnSplashDataSource(retrofitService)
    }

    @Provides
    @Singleton
    fun providePhotoDao(application: Application): PhotoDao {
        return FavoritePhotosDatabase.getDatabase(application).PhotoDao()
    }

    @Provides
    @Singleton
    fun provideUserDataSource(photoDao: PhotoDao): UserDataSource {
        return UserDataSource(photoDao)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        remoteDataSource: UnSplashDataSource,
        localDataSource: UserDataSource
    ): PhotoRepository {
        return PhotoRepository(remoteDataSource, localDataSource)
    }

}