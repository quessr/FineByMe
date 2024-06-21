package com.example.finebyme.ui.photoList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.data.repository.SearchPhotosRepository
import com.example.finebyme.ui.base.BaseViewModel

class PhotoListViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository,
    private val searchPhotosRepository: SearchPhotosRepository
) : BaseViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _state: MutableLiveData<State> by lazy { MutableLiveData() }
    val state: LiveData<State> get() = _state

    init {
        fetchPhotos()
    }


    private fun fetchPhotos() {
        _state.postValue(State.LOADING)
        RetrofitInstance.fetchRandomPhotos { photos ->
            if (photos != null) {
                _photos.postValue(photos)
                _state.postValue(State.DONE)
//                insertFirstPhotoInDb(photos[0])
            } else {
                _photos.postValue(listOf())
                _state.postValue(State.ERROR)

            }
        }
    }

    fun searchPhotos(query: String) {
        _state.postValue(State.LOADING)
        Log.d("PhotoListViewModel", "Searching for photos with query: $query")
        searchPhotosRepository.searchPhotos(query).observeForever { response ->
            Log.d("PhotoListViewModel", "Received response: $response")
            if (response != null && response.results.isNotEmpty()) {
                Log.d("PhotoListViewModel", "Search successful: ${response.results.size} results found")
                _photos.postValue(response.results)
                _state.postValue(State.DONE)
            } else {
                Log.d("PhotoListViewModel", "Search failed or no results found")
                _photos.postValue(emptyList())
                _state.postValue(State.ERROR)
            }
        }

    }

//    private fun insertFirstPhotoInDb(unsplashPhoto: UnsplashPhoto) {
//
//        context?.let { ctx ->
//            val photo = unsplashPhoto.toPhoto()
//
//            favoritePhotosRepository.insertPhoto(photo)
//
//            val result = FavoritePhotosDatabase.getDatabase(ctx).PhotoDao().getAllPhotos()
//            Log.d("getAllPhotos", "getAllPhotos : ${result.size}")
//            for (item in result) {
//                Log.d("getAllPhotos ==> ", "getAllPhotos : ${item.title}")
//            }
//        }


}
