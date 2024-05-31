package com.example.finebyme.ui.photoList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.model.Photo
import com.example.finebyme.data.network.RetrofitInstance

class PhotoListViewModel : ViewModel() {

    enum class State {
        LOADING,
        DONE,
        ERROR
    }

    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData() }
    val photos: LiveData<List<Photo>> get() = _photos

    private val _state: MutableLiveData<State> by lazy { MutableLiveData() }
    val state: LiveData<State> get() = _state

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        _state.postValue(State.LOADING)
        RetrofitInstance.fetchRandomPhoto { photos ->
            if (photos != null) {
                _photos.postValue(photos)
                _state.postValue(State.DONE)
            } else {
                _photos.postValue(listOf())
                _state.postValue(State.ERROR)
            }
        }
    }
}