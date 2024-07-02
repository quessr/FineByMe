package com.example.finebyme.ui.photoList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.repository.PhotoRepository
import com.example.finebyme.ui.base.BaseViewModel

class PhotoListViewModel(
    application: Application,
    private val photoRepository: PhotoRepository,
) : BaseViewModel(application) {

    private val _photos: MutableLiveData<List<UnsplashPhoto>> by lazy { MutableLiveData() }
    val photos: LiveData<List<UnsplashPhoto>> get() = _photos

    private val _loadingState: MutableLiveData<State> by lazy { MutableLiveData() }
    val loadingState: LiveData<State> get() = _loadingState

    // 검색 전 사진 목록을 캐싱하기 위한 변수
    private var cachedPhotos: List<UnsplashPhoto> = emptyList()

    init {
        fetchPhotos()
    }


    private fun fetchPhotos() {
        _loadingState.postValue(State.LOADING)
        photoRepository.getRandomPhotoList { photos ->
            if (photos != null) {
                _photos.postValue(photos)
                cachedPhotos = photos
                _loadingState.postValue(State.DONE)
//                insertFirstPhotoInDb(photos[0])
            } else {
                _photos.postValue(listOf())
                _loadingState.postValue(State.ERROR)

            }
        }
    }

    fun searchPhotos(query: String) {

        // 검색어가 비어있을 때 캐시된 사진 목록을 다시 설정
        if (query.isEmpty()) {
            _photos.postValue(cachedPhotos)
            _loadingState.postValue(State.DONE)
            return
        }

        _loadingState.postValue(State.LOADING)
        Log.d("PhotoListViewModel", "Searching for photos with query: $query")
        photoRepository.getSearchPhotoList(query) { response ->
            Log.d("PhotoListViewModel", "Received response: $response")
            if (!response.isNullOrEmpty()) {
                Log.d("PhotoListViewModel", "Search successful: ${response.size} results found")
                _photos.postValue(response)
                _loadingState.postValue(State.DONE)
            } else {
                Log.d("PhotoListViewModel", "Search failed or no results found")
                _photos.postValue(emptyList())
                _loadingState.postValue(State.ERROR)
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
