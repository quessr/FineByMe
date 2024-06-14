package com.example.finebyme.ui.photoDetail

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.finebyme.R
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.data.repository.FavoritePhotosImpl
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.ui.photoList.PhotoListViewModel

class PhotoDetailActivity() : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
    }

    private lateinit var binding: ActivityPhotoDetailBinding
    private var photo: Photo? = null
    private val photoDetailViewModel: PhotoDetailViewModel by viewModels {
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosImpl(photoDao)
        AppViewModelFactory(application, favoritePhotosRepository)
    }
//    private val photoListViewModel: PhotoListViewModel by viewModels {
//        val application = this.application
//        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
//        val favoritePhotosRepository = FavoritePhotosImpl(photoDao)
//        AppViewModelFactory(application, favoritePhotosRepository)
//    }

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
//        viewModel = ViewModelProvider(this)[PhotoDetailViewModel::class.java]

        if (photo != null) {
            // ViewModel에 Photo 객체를 전달하여 photo title 데이터 변환
            photoDetailViewModel.onEntryScreen(photo!!)
        } else {
            finish()
        }

        setupObservers()
        // TODO: setupListener()
        binding.ivFavorite.setOnClickListener {
            photo?.let { it1 -> viewModel.toggleFavorite(it1) }
            photo?.let { photo -> photoDetailViewModel.toggleFavorite(photo) }
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
            if (state.equals(PhotoDetailViewModel.State.LOADING)) {
                showLoading()
            } else binding.ivLoading.visibility = View.GONE
        }
    }

    private fun setupPhotoDetails(photo: Photo) {
        photo?.let {

            binding.ivPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            Glide.with(this)
                .load(it.fullUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        photoDetailViewModel.onPhotoLoadFail()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        photoDetailViewModel.onPhotoLoadCompleted()
                        return false
                    }
                })
                .into(binding.ivPhoto)

//            val transformedTitle = it.title?.let { title ->
//                viewModel.transformTitle(title)
//            }
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

        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .centerCrop()
            .override(40, 40)
            .into(binding.ivLoading)
    }
}