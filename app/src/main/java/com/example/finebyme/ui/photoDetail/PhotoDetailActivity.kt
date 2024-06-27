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
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepositoryImpl
import com.example.finebyme.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.ImageLoader
import com.google.android.material.snackbar.Snackbar
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.finebyme.R
import com.example.finebyme.utils.LoadingHandler
import java.io.IOException


class PhotoDetailActivity() : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
        private const val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }

    private lateinit var binding: ActivityPhotoDetailBinding
    private var photo: Photo? = null
    private val photoDetailViewModel: PhotoDetailViewModel by viewModels {
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosRepositoryImpl(photoDao)
        AppViewModelFactory(application, favoritePhotosRepository)
    }

    private lateinit var loadingHandler: LoadingHandler<ActivityPhotoDetailBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingHandler = LoadingHandler(binding, this)

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
        isFavorite?.let { updateFavoriteIcon(it) }

        setupObservers()
        // TODO: setupListener()
        binding.ivFavorite.setOnClickListener {
            photo?.let { photo -> photoDetailViewModel.toggleFavorite(photo) }
        }

        binding.chipDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestTiramisuPermission()
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvDownloading.visibility = View.VISIBLE
                photo?.let { photoDetailViewModel.downloadImage(it) }
            }
        }

        handleOnBackPressed()
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
            binding.progressBar.visibility = View.VISIBLE
            binding.tvDownloading.visibility = View.VISIBLE
            photo?.let { photoDetailViewModel.downloadImage(it) }
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

    private fun setupObservers() {
        photoDetailViewModel.transformedPhoto.observe(this) { transformedPhoto ->
            setupPhotoDetails(transformedPhoto)
        }

        photoDetailViewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }

        photoDetailViewModel.LoadingState.observe(this) { state ->
            loadingHandler.setLoadingState(state)
        }

        photoDetailViewModel.isDownloading.observe(this) { isDownloading ->
            if (isDownloading) {
                binding.progressBar.isVisible = true
                binding.tvDownloading.isVisible = true
            } else {
                binding.progressBar.isVisible = false
                binding.tvDownloading.isVisible = false
            }
        }

        photoDetailViewModel.downloadState.observe(this) { message ->
            showSnackbar(message)
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

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // 기본 동작으로 MainActivity로 돌아가기
            }
        })
    }
}