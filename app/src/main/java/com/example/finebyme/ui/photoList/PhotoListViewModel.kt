package com.example.finebyme.ui.photoList

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.model.toPhoto
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.data.repository.FavoritePhotosRepository
import java.util.concurrent.Executor

class PhotoListViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : AndroidViewModel(application) {

    enum class State {
        LOADING,
        DONE,
        ERROR
    }

    //    private var context: Context? = null
    private val context = getApplication<Application>().applicationContext

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _state: MutableLiveData<State> by lazy { MutableLiveData() }
    val state: LiveData<State> get() = _state

    init {
        fetchPhotos()
    }

//    fun setContext(context: Context) {
//        this.context = context
//    }

    private fun fetchPhotos() {
        _state.postValue(State.LOADING)
        RetrofitInstance.fetchRandomPhoto { photos ->
            if (photos != null) {
                _photos.postValue(photos)
                _state.postValue(State.DONE)
                insertFirstPhotoInDb(photos[0])
            } else {
                _photos.postValue(listOf())
                _state.postValue(State.ERROR)

            }
        }
    }

    private fun insertFirstPhotoInDb(unsplashPhoto: UnsplashPhoto) {

        context?.let { ctx ->
            val photo = unsplashPhoto.toPhoto()

//            FavoritePhotosDatabase.getDatabase(ctx).PhotoDao().insert(photo)
            favoritePhotosRepository.insertPhoto(photo)

            val result = FavoritePhotosDatabase.getDatabase(ctx).PhotoDao().getAllPhotos()
            Log.d("getAllPhotos", "getAllPhotos : ${result.size}")
            for (item in result) {
                Log.d("getAllPhotos ==> ", "getAllPhotos : ${item.title}")
            }
        }


    }
}