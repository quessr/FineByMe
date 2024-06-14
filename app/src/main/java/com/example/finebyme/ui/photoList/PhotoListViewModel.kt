package com.example.finebyme.ui.photoList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.ui.base.BaseViewModel

class PhotoListViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : BaseViewModel(application) {

    enum class State {
        LOADING,
        DONE,
        ERROR
    }

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
        RetrofitInstance.fetchRandomPhoto { photos ->
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
