package com.example.finebyme.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.BuildConfig
import com.example.finebyme.data.model.SearchPhotoResponse
import com.example.finebyme.data.network.RetrofitService
import com.example.finebyme.utils.NetworkUtils

class SearchPhotosRepositoryImpl(private val retrofitService: RetrofitService) :
    SearchPhotosRepository {
    private val apiKey = BuildConfig.UNSPLASH_API_KEY
    override fun searchPhotos(query: String): MutableLiveData<SearchPhotoResponse> {
        val result = MutableLiveData<SearchPhotoResponse>()

        NetworkUtils.enqueueCall(
            retrofitService.getSearchPhoto(apiKey, query),
            onSuccess = { response ->
                val photos = response.body()
                result.postValue(photos ?: SearchPhotoResponse(emptyList()))
                if (photos != null) {
                    Log.d("@@@@@@", "fetchSearchPhotos API Call Successful: ${response.body()}")
                } else {
                    Log.d("@@@@@@", "fetchSearchPhotos API Call Successful but no data found")
                }
            }, onFailure = { t ->
                result.postValue(SearchPhotoResponse(emptyList()))
                Log.e("@@@@@@", "fetchSearchPhotos Failed to fetch data: ${t.message}")

            }
        )

        return result
    }
}