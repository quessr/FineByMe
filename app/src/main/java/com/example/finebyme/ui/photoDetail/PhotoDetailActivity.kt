package com.example.finebyme.ui.photoDetail

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosImpl
import com.example.finebyme.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.ImageLoader
import com.google.android.material.snackbar.Snackbar
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.finebyme.R


class PhotoDetailActivity() : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
        private const val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }

    private lateinit var binding: ActivityPhotoDetailBinding
    private var photo: Photo? = null
    private val photoDetailViewModel: PhotoDetailViewModel by viewModels {
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosImpl(photoDao)
        AppViewModelFactory(application, favoritePhotosRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_PHOTO, Photo::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ARG_PHOTO)
        }

        if (photo != null) {
            // ViewModel에 Photo 객체를 전달하여 photo title 데이터 변환
            photoDetailViewModel.onEntryScreen(photo!!)
        } else {
            finish()
        }

        // 즐겨찾기 상태를 확인하고 UI 업데이트
        val isFavorite = photo?.let { photoDetailViewModel.isPhotoFavorite(it.id) }
        if (isFavorite != null) {
            updateFavoriteIcon(isFavorite)
        }

        setupObservers()
        // TODO: setupListener()
        binding.ivFavorite.setOnClickListener {
            photo?.let { photo -> photoDetailViewModel.toggleFavorite(photo) }
        }

        binding.chipDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestTiramisuPermission()
            } else {
                downloadImage()
            }
        }
    }

    // Android 13 이상일 때
    private fun requestTiramisuPermission() {
        checkPermissionsAndStartMotion(arrayOf(imagePermission), 200)
    }

    private fun checkPermissionsAndStartMotion(permissions: Array<String>, requestCode: Int) {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionResults.all { it }) {
            // TODO 권한이 허용 되었을 때의 액선
            downloadImage()
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100, 200 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO 권한이 허용 되었을 때의 액션
                    showSnackbar("권한이 허용 되었습니다.")
                } else {
                    finish()
                    showSnackbar("권한이 부여되지 않았습니다.")
                }
            }
        }
    }

    private fun downloadImage() {
        Glide.with(this)
            .asBitmap()
            .load(photo?.fullUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImageToGallery(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Do nothing
                }
            })
    }


    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "Finebyme_${
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                Date()
            )
        }.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri.let {
                val stream: OutputStream? = it?.let { it1 -> contentResolver.openOutputStream(it1) }
                if (stream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.flush()
                    stream.close()
                    showSnackbar("이미지가 갤러리에 다운로드 되었습니다.")
                } else {
                    showSnackbar("다운로드에 실패했습니다.")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackbar("다운로드에 실패했습니다.")

        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackbarView: View = snackbar.view

        // 상단 중앙으로 이동
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        snackbarView.layoutParams = params

        val color = ContextCompat.getColor(this, R.color.black_40)
        snackbar.setBackgroundTint(color)

        snackbar.show()
    }

    private fun setupObservers() {
        photoDetailViewModel.transformedPhoto.observe(this) { transformedPhoto ->
            setupPhotoDetails(transformedPhoto)
        }

        photoDetailViewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }

        photoDetailViewModel.state.observe(this) { state ->
            if (state.equals(State.LOADING)) {
                showLoading()
            } else binding.ivLoading.visibility = View.GONE
        }
    }

    private fun setupPhotoDetails(photo: Photo) {
        photo?.let {

            binding.ivPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            ImageLoader.loadImage(
                context = this,
                url = photo.fullUrl,
                imageView = binding.ivPhoto,
                photoDetailViewModel = photoDetailViewModel
            )

            binding.tvTitle.text = it.title
            binding.tvDescription.text = it?.description

            Log.d("@@@@@@", " photo id : ${photo!!.id}")
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_nav_favorite_selected)

        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_nav_favorite_normal)
        }
    }

    private fun showLoading() {
        binding.ivLoading.visibility = View.VISIBLE

        ImageLoader.loadGif(
            context = this,
            resourceId = R.drawable.loading,
            imageView = binding.ivLoading,
            centerCrop = true,
            overrideWidth = 40,
            overrideHeight = 40
        )
    }
}