package com.example.finebyme.data.network

import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.model.SearchPhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("/photos/random/")
    fun getRandomPhoto(
        @Query("client_id") clientId: String,
        @Query("count") count: Int
    ): retrofit2.Call<List<UnsplashPhoto>>

    @GET("/search/photos/")
    fun getSearchPhoto(
        @Query("client_id") clientId: String,
        @Query("query") query: String
    ): retrofit2.Call<SearchPhotoResponse>
}