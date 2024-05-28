package com.example.finebyme.data.network

import android.telecom.Call
import android.util.Log
import com.example.finebyme.BuildConfig
import com.example.finebyme.data.model.AlternativeSlugs
import com.example.finebyme.data.model.Photo
import com.example.finebyme.data.model.SearchPhotoResponse
import com.google.gson.internal.GsonBuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.internal.operators.flowable.FlowableCount
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.RowId

object RetrofitInstance {
    val API_KEY = BuildConfig.UNSPLASH_API_KEY

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
            .baseUrl("https://api.unsplash.com/")
            .build()

    }


    interface UnsplashApi {
        @GET("/photos/random")
        fun getRandomPhoto(@Query("client_id") clientId: String = RetrofitInstance.API_KEY,
                           @Query("count") count: Int): retrofit2.Call<List<Photo>>
        @GET("/search/photos/")
        fun getSearchPhoto(@Query("client_id") clientId: String = RetrofitInstance.API_KEY,
                           @Query("query") query: String): retrofit2.Call<SearchPhotoResponse>
    }

    val api: UnsplashApi by lazy {
        retrofit.create(UnsplashApi::class.java)
    }

    fun fetchRandomPhoto() {
        val call = api.getRandomPhoto(count = 5)
        call.enqueue(object  : retrofit2.Callback<List<Photo>> {
            override fun onResponse(p0: retrofit2.Call<List<Photo>>, response: retrofit2.Response<List<Photo>>) {
                if (response.isSuccessful) {
                    val photos = response.body()
                    if (!photos.isNullOrEmpty()) {
                        for (photo in photos) {
                            Log.d("RetrofitInstance", "Topic : ${photo.alternativeSlugs?.ko}")
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
        val call = api.getSearchPhoto(query = "고양이")
        call.enqueue(object  : retrofit2.Callback<SearchPhotoResponse> {
            override fun onResponse(p0: retrofit2.Call<SearchPhotoResponse>, response: retrofit2.Response<SearchPhotoResponse>) {
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


