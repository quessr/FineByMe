package com.example.finebyme.presentation.photoDetail

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import com.example.finebyme.domain.usecase.CheckFavoritePhotoUseCase
import com.example.finebyme.domain.usecase.SetFavoritePhotoUseCase
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.enums.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val setFavoritePhotoUseCase: SetFavoritePhotoUseCase,
    private val checkFavoritePhotoUseCase: CheckFavoritePhotoUseCase
    // private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _transformedPhoto = MutableLiveData<Photo>()
    val transformedPhoto: LiveData<Photo> get() = _transformedPhoto

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _loadingState: MutableLiveData<LoadingState> by lazy { MutableLiveData() }
    val loadingState: LiveData<LoadingState> get() = _loadingState

    private val _isDownloading = MutableLiveData<Boolean>()
    val isDownloading: LiveData<Boolean> get() = _isDownloading

    private var _downloadState = MutableLiveData<String>()
    val downloadState: LiveData<String> get() = _downloadState
    private val dateFormat =
        context.applicationContext.getString(R.string.date_format)
    private val appName =
        context.applicationContext.getString(R.string.app_name)

    init {
        _loadingState.value = LoadingState.LOADING
    }

    fun onPhotoLoadCompleted() {
        _loadingState.value = LoadingState.DONE
    }

    fun onPhotoLoadFail() {
        _loadingState.value = LoadingState.ERROR
    }

    fun onEntryScreen(photo: Photo) {
        val transformTitle = transformTitle(photo.title)
        photo.title = transformTitle
        _transformedPhoto.value = photo
        _isFavorite.value = isPhotoFavorite(photo.id)
    }

    fun isPhotoFavorite(id: String): Boolean {
//        return photoRepository.isPhotoFavorite(id)
        return checkFavoritePhotoUseCase.execute(id)
    }

    fun toggleFavorite(photo: Photo) {
        if (isPhotoFavorite(photo.id)) {
//            photoRepository.removePhotoFromFavorites(photo)
            setFavoritePhotoUseCase.execute(photo, false)
            _isFavorite.value = false
        } else {
//            photoRepository.addPhotoToFavorites(photo)
            setFavoritePhotoUseCase.execute(photo, true)
            _isFavorite.value = true
        }
    }

    private fun transformTitle(title: String): String {
        val parts = title.split("-")

        /**제목의 마지막 부분이 영어로 시작되거나 하이픈("-")으로 시작되는지 확인하기 위한 정규 표현식*/
        val lastWordFormat = "[a-zA-Z0-9_-]+\$".toRegex()

        /**마지막 부분이 영어형식이 맞는지 확인하고, 맞다면 제외*/
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
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(photo.fullUrl)
            .skipMemoryCache(true)  // 메모리 캐시 무시
            .diskCacheStrategy(DiskCacheStrategy.NONE)  // 디스크 캐시 무시
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveImageToGallery(resource)
                    } else {
                        saveImageToGalleryBelowAndroidQ(resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 로드 취소 또는 실패
                    _isDownloading.value = false
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    _isDownloading.value = false
                    _downloadState.value =
                        context.applicationContext.getString(R.string.download_failed)

                }
            })
    }

    fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "${appName}_${
            SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date())
        }.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri =
            context.applicationContext.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        var stream: OutputStream? = null

        try {
            uri.let {
                stream = it?.let { it1 ->
                    context.applicationContext.contentResolver.openOutputStream(
                        it1
                    )
                }
                stream?.let { it1 -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it1) }
                stream?.flush()
                _downloadState.value =
                    context.applicationContext.getString(R.string.download_success)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadState.value =
                context.applicationContext.getString(R.string.download_failed)
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            _isDownloading.value = false
        }
    }

    private fun saveImageToGalleryBelowAndroidQ(bitmap: Bitmap) {
        val filename = "${appName}_${
            SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date())
        }.jpg"
        val pictureDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(pictureDir, filename)
        var stream: OutputStream? = null

        try {
            stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            _downloadState.value =
                context.applicationContext.getString(R.string.download_success)
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadState.value =
                context.applicationContext.getString(R.string.download_failed)
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            _isDownloading.value = false
        }

        MediaScannerConnection.scanFile(
            context.applicationContext,
            arrayOf(file.toString()),
            null,
            null
        )
    }
}