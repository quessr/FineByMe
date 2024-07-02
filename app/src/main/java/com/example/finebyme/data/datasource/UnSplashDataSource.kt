package com.example.finebyme.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.BuildConfig
import com.example.finebyme.data.model.SearchPhotoResponse
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.network.RetrofitService
import com.example.finebyme.utils.NetworkUtils

class UnSplashDataSource(private val retrofitService: RetrofitService) {

    private val apiKey = BuildConfig.UNSPLASH_API_KEY

    // Fetch random photos
    fun getRandomPhotoList(onResult: (List<UnsplashPhoto>?) -> Unit) {
        NetworkUtils.enqueueCall(retrofitService.getRandomPhoto(apiKey, 1000),
            onSuccess = { response ->
                val photos = response.body()
                if (!photos.isNullOrEmpty()) {
                    onResult(photos)
                } else {
                    Log.d("RetrofitInstance", "No photos found")
                    onResult(null)
                }
            },
            onFailure = { t ->
                Log.e("RetrofitInstance", "Failed to fetch data: ${t.message}")
                onResult(null)
            })
    }

    // Search photos by query
    fun getSearchPhotoList(query: String, onResult: (List<UnsplashPhoto>?) -> Unit) {
        NetworkUtils.enqueueCall(retrofitService.getSearchPhoto(apiKey, query),
            onSuccess = { response ->
                val photos = response.body()?.results ?: emptyList()
                if (photos.isNotEmpty()) {
                    onResult(photos)
                } else {
                    Log.d("RetrofitInstance", "No photos found")
                    onResult(null)
                }
            }, onFailure = { t ->
                Log.e("RetrofitInstance", "Failed to fetch data: ${t.message}")
                onResult(null)
            })
    }
}