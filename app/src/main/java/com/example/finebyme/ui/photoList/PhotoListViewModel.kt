package com.example.finebyme.ui.photoList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.model.Photo
import com.example.finebyme.data.network.RetrofitInstance

class PhotoListViewModel : ViewModel() {
    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        RetrofitInstance.fetchRandomPhoto { photos ->
            if (photos != null) {
                _photos.postValue(photos)
            } else {
                _photos.postValue(listOf())
            }
        }
    }
}