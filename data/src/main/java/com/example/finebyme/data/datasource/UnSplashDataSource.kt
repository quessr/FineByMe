package com.example.finebyme.data.datasource

import com.example.finebyme.data.BuildConfig
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.network.RetrofitService

class UnSplashDataSource(private val retrofitService: RetrofitService) {

    private val apiKey = BuildConfig.UNSPLASH_API_KEY

    // Fetch random photos
    suspend fun getRandomPhotoList(): Result<List<UnsplashPhoto>> {
        return try {
            val response = retrofitService.getRandomPhoto(apiKey, 1000)
            if (response != null) {
                Result.success(response)
            } else {
                Result.failure(Exception("Failed to fetch photos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Search photos by query
    suspend fun getSearchPhotoList(query: String): Result<List<UnsplashPhoto>> {
        return try {
            val response = retrofitService.getSearchPhoto(apiKey, query).results
            if (response != null) {
                Result.success(response)
            } else {
                Result.failure(Exception("Failed to fetch photos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}