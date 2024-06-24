package com.example.finebyme.data.network

import android.util.Log
import com.example.finebyme.BuildConfig
import com.example.finebyme.data.model.SearchPhotoResponse
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.utils.NetworkUtils.enqueueCall
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.unsplash.com/"
    private val API_KEY = BuildConfig.UNSPLASH_API_KEY

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

    fun fetchRandomPhotos(onResult: (List<UnsplashPhoto>?) -> Unit) {
        val service = RetrofitInstance.retrofitService

        enqueueCall(
            service.getRandomPhoto(API_KEY, 1000),
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

//    fun fetchSearchPhotos(query: String, onResult: (SearchPhotoResponse?) -> Unit) {
//        val service = retrofitService
//
//        enqueueCall(
//            service.getSearchPhoto(API_KEY, query),
//            onSuccess = { response ->
//                val photos = response.body()
//                onResult(photos)
//                if (photos != null) {
//                    Log.d("@@@@@@", "fetchSearchPhotos API Call Successful: ${response.body()}")
//                } else {
//                    Log.d("@@@@@@", "fetchSearchPhotos API Call Successful but no data found")
//                }
////                Log.d("RetrofitInstance", "Response: ${response.body()}")
//            },
//            onFailure = { t ->
//                Log.e("@@@@@@", "fetchSearchPhotos Failed to fetch data: ${t.message}")
//                onResult(null)
//            }
//        )
//    }
}


