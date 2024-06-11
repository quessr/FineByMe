package com.example.finebyme.ui.photoDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.finebyme.R
import com.example.finebyme.data.db.Photo
import com.example.finebyme.databinding.ActivityPhotoDetailBinding

class PhotoDetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
    }

    private lateinit var binding: ActivityPhotoDetailBinding
    private var photo: Photo? = null
    private lateinit var viewModel: PhotoDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photo = intent.getParcelableExtra(ARG_PHOTO)
        viewModel = ViewModelProvider(this).get(PhotoDetailViewModel::class.java)

        setupPhotoDetails()
    }

    private fun setupPhotoDetails() {
        photo?.let {
            Glide.with(this)
                .load(it?.fullUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivPhoto)

            val transformedTitle = it.title?.let {title ->
                viewModel.transformTitle(title)
            }
            binding.tvTitle.text = transformedTitle
            binding.tvDescription.text = it?.description
        }
    }
}