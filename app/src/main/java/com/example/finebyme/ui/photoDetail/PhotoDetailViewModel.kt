package com.example.finebyme.ui.photoDetail

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepository
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoDetailViewModel(
    application: Application,
    private val favoritePhotosRepository: FavoritePhotosRepository
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _transformedPhoto = MutableLiveData<Photo>()
    val transformedPhoto: LiveData<Photo> get() = _transformedPhoto

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _LoadingState: MutableLiveData<State> by lazy { MutableLiveData() }
    val LoadingState: LiveData<State> get() = _LoadingState

    private val _isDownloading =  MutableLiveData<Boolean>()
    val isDownloading: LiveData<Boolean> get() = _isDownloading

    private var _downloadState = MutableLiveData<String>()
    val downloadState: LiveData<String> get() = _downloadState



    init {
        _LoadingState.value = State.LOADING
    }

    fun onPhotoLoadCompleted() {
        _LoadingState.value = State.DONE
    }

    fun onPhotoLoadFail() {
        _LoadingState.value = State.ERROR
    }

    fun onEntryScreen(photo: Photo) {
        val transformTitle = transformTitle(photo.title)
        photo.title = transformTitle
        _transformedPhoto.value = photo
        _isFavorite.value = isPhotoFavorite(photo.id)
    }

    fun isPhotoFavorite(id: String):Boolean {
        return favoritePhotosRepository.isPhotoFavorite(id)
    }

    fun toggleFavorite(photo: Photo) {
        if(isPhotoFavorite(photo.id)) {
            favoritePhotosRepository.deletePhoto(photo)
            _isFavorite.value = false
        } else {
            favoritePhotosRepository.insertPhoto(photo)
            _isFavorite.value = true
        }
    }

    private fun transformTitle(title: String): String {
        val parts = title.split("-")

        // 제목의 마지막 부분이 영어로 시작되거나 하이픈("-")으로 시작되는지 확인하기 위한 정규 표현식
        val lastWordFormat = "[a-zA-Z0-9_-]+\$".toRegex()

        // 마지막 부분이 영어형식이 맞는지 확인하고, 맞다면 제외
        val filteredParts = if (parts.isNotEmpty() && lastWordFormat.matches(parts.last())) {
            parts.dropLast(1)
        } else {
            parts
        }

        return filteredParts.joinToString(" ")
    }

    fun downloadImage(photo: Photo) {
        if (_isDownloading.value == true) return

        _isDownloading.value = true
        Glide.with(context)
            .asBitmap()
            .load(photo?.fullUrl)
            .skipMemoryCache(true)  // 메모리 캐시 무시
            .diskCacheStrategy(DiskCacheStrategy.NONE)  // 디스크 캐시 무시
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    saveImageToGallery(resource)
//                    _isDownloading.value = false  // 다운로드 완료
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 로드 취소 또는 실패
                    _isDownloading.value = false
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    _isDownloading.value = false
                    _downloadState.value = "다운로드에 실패했습니다."
                }
            })
    }

    fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "Finebyme_${
            SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date())
        }.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        var stream: OutputStream? = null

        try {
            uri.let {
                stream = it?.let { it1 -> context.contentResolver.openOutputStream(it1) }
                stream?.let { it1 -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it1) }
                stream?.flush()
                _downloadState.value = "이미지가 갤러리에 다운로드 되었습니다."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadState.value = "다운로드에 실패했습니다."
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            _isDownloading.value = false
        }
    }
}