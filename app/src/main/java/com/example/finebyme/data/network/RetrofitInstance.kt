package com.example.finebyme.data.network

import android.util.Log
import com.example.finebyme.BuildConfig
import com.example.finebyme.data.model.Photo
import com.example.finebyme.data.model.SearchPhotoResponse
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
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()

    }

    val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

    fun fetchRandomPhoto() {
        val service = RetrofitInstance.retrofitService

        service.getRandomPhoto(API_KEY, 5).enqueue(object : retrofit2.Callback<List<Photo>> {
            override fun onResponse(
                p0: retrofit2.Call<List<Photo>>,
                response: retrofit2.Response<List<Photo>>
            ) {
                if (response.isSuccessful) {
                    val photos = response.body()
                    if (!photos.isNullOrEmpty()) {
                        for (photo in photos) {
                            Log.d("RetrofitInstance", "Topic : ${photo.title?.ko}")
                        }
                    } else {
                        Log.d("RetrofitInstance", "No photos found")
                    }
                } else {
                    Log.e("RetrofitInstance", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(p0: retrofit2.Call<List<Photo>>, t: Throwable) {
                Log.e("RetrofitInstance", "Failed to fetch data: ${t.message}")
            }
        })
    }

    fun fetchSearchPhoto() {
        val service = RetrofitInstance.retrofitService

        service.getSearchPhoto(API_KEY, "고양이")
            .enqueue(object : retrofit2.Callback<SearchPhotoResponse> {
                override fun onResponse(
                    p0: retrofit2.Call<SearchPhotoResponse>,
                    response: retrofit2.Response<SearchPhotoResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("RetrofitInstance", "Response: ${response.body()}")
                    } else {
                        Log.e("RetrofitInstance", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(p0: retrofit2.Call<SearchPhotoResponse>, t: Throwable) {
                    Log.e("RetrofitInstance", "Failed to fetch data: ${t.message}")
                }
            })
    }
}


