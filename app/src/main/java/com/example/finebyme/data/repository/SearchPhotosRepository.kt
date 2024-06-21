package com.example.finebyme.data.repository

import androidx.lifecycle.LiveData
import com.example.finebyme.data.model.SearchPhotoResponse

interface SearchPhotosRepository {
    fun searchPhotos(query: String): LiveData<SearchPhotoResponse>
}