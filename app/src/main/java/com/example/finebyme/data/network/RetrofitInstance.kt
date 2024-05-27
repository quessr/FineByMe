package com.example.finebyme.data.network

import android.telecom.Call
import android.util.Log
import com.example.finebyme.BuildConfig
import com.google.gson.internal.GsonBuildConfig
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

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://api.unsplash.com/")
            .build()

    }


    interface UnsplashApi {
        @GET("/photos/random")
        fun getRandomPhoto(@Query("client_id") clientId: String = RetrofitInstance.API_KEY,
                           @Query("count") count: Int): retrofit2.Call<ResponseBody>
        @GET("/search/photos/")
        fun getSearchPhoto(@Query("client_id") clientId: String = RetrofitInstance.API_KEY,
                           @Query("query") query: String): retrofit2.Call<ResponseBody>
    }

    val api: UnsplashApi by lazy {
        retrofit.create(UnsplashApi::class.java)
    }

    fun fetchRandomPhoto() {
//        val call = api.getRandomPhoto(count = 5)
        val callSearchPhoto = api.getSearchPhoto(query = "고양이")
        callSearchPhoto.enqueue(object  : retrofit2.Callback<ResponseBody> {
            override fun onResponse(p0: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RetrofitInstance", "Response: ${response.body()?.string()}")
                } else {
                    Log.e("RetrofitInstance", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(p0: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.e("RetrofitInstance", "Failed to fetch data: ${t.message}")
            }
        })
    }
}


