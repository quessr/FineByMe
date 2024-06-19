package com.example.finebyme.ui.photoDetail

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.finebyme.R
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosImpl
import com.example.finebyme.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.ImageLoader

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

        binding.chipDownload.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestTiramisuPermission()
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
                } else {
                    finish()
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