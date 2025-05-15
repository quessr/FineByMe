package com.example.finebyme.data.network

import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.model.SearchPhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
//    @GET("/photos/random/")
//    suspend fun getRandomPhoto(
//        @Query("client_id") clientId: String,
//        @Query("count") count: Int
//    ): List<UnsplashPhoto>

    @GET("/photos/")
    suspend fun getRandomPhoto(
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnsplashPhoto>

    @GET("/search/photos/")
    suspend fun getSearchPhoto(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchPhotoResponse
}