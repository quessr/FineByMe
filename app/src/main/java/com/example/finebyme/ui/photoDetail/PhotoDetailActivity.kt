package com.example.finebyme.ui.photoDetail

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosRepositoryImpl
import com.example.finebyme.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.ImageLoader
import com.google.android.material.snackbar.Snackbar
import com.example.finebyme.R
import com.example.finebyme.utils.LoadingHandler


class PhotoDetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
        private val IMAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        private const val REQUEST_CODE_TIRAMISU = 200
        private const val REQUEST_CODE_LEGACY = 100
//        private const val WRITE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
            setupUI()
        } else {
            finish()
        }

        setupObservers()
        // TODO: setupListener()
        handleOnBackPressed()
    }

    private fun setupUI() {
        val isFavorite = photo?.let { photoDetailViewModel.isPhotoFavorite(it.id) }
        isFavorite?.let { updateFavoriteIcon(it) }

        binding.ivFavorite.setOnClickListener {
            photo?.let { photoDetailViewModel.toggleFavorite(it) }
        }

        binding.chipDownload.setOnClickListener {
            requestPermissionDownload()
        }
    }

    private fun requestPermissionDownload() {
//        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arrayOf(IMAGE_PERMISSION)
//        } else {
//            arrayOf(WRITE_PERMISSION)
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissionsAndStartMotion(arrayOf(IMAGE_PERMISSION), REQUEST_CODE_TIRAMISU)
        } else {
            checkPermissionsAndStartMotion(arrayOf(IMAGE_PERMISSION), REQUEST_CODE_LEGACY)
        }
    }

    private fun checkPermissionsAndStartMotion(permissions: Array<String>, requestCode: Int) {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionResults.all { it }) {
            startDownload()
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
                    showSnackbar(R.string.permission_granted.toString())
                    startDownload()
                } else {
                    finish()
                    showSnackbar(R.string.permission_granted.toString())
                }
            }
        }
    }

    private fun startDownload() {
        binding.progressBar.isVisible = true
        binding.tvDownloading.isVisible = true
        photo?.let { photoDetailViewModel.downloadImage(it) }
    }

    private fun setupObservers() {
        photoDetailViewModel.transformedPhoto.observe(this) { transformedPhoto ->
            setupPhotoDetails(transformedPhoto)
        }

        photoDetailViewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }

        photoDetailViewModel.loadingState.observe(this) { state ->
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
        photo.let {

            binding.ivPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            ImageLoader.loadImage(
                context = this,
                url = photo.fullUrl,
                imageView = binding.ivPhoto,
                photoDetailViewModel = photoDetailViewModel
            )

            binding.tvTitle.text = it.title
            binding.tvDescription.text = it.description

            Log.d("@@@@@@", " photo id : ${photo.id}")
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