package com.example.finebyme.ui.photoDetail

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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
    private lateinit var viewModel: PhotoDetailViewModel
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
        viewModel = ViewModelProvider(this)[PhotoDetailViewModel::class.java]

        if (photo != null) {
            // ViewModel에 Photo 객체를 전달하여 photo title 데이터 변환
            viewModel.onEntryScreen(photo!!)
        } else {
            finish()
        }

        setupObservers()
        // TODO: setupListener()
        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupObservers() {
        viewModel.transformedPhoto.observe(this) { transformedPhoto ->
            setupPhotoDetails(transformedPhoto)
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }
    }

    private fun setupPhotoDetails(photo: Photo) {
        photo?.let {
            binding.ivLoading.visibility = View.GONE
            binding.ivPhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            Glide.with(this).load(it.fullUrl).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade()).into(binding.ivPhoto)

            val transformedTitle = it.title?.let { title ->
                viewModel.transformTitle(title)
            }
            binding.tvTitle.text = transformedTitle
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
        Glide.with(this).asGif().load(R.drawable.loading).centerCrop().override(40, 40)
            .into(binding.ivLoading)
        binding.ivLoading.visibility = View.VISIBLE
//        binding.root.setBackgroundColor(
//            ContextCompat.getColor(
//                this,
//                R.color.white
//            )
//        )
    }

    private fun showError() {
        // TODO
    }
}