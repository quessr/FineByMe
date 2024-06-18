package com.example.finebyme.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.finebyme.ui.photoDetail.PhotoDetailViewModel

object ImageLoader {
    fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        centerCrop: Boolean = true,
        crossFade: Boolean = true,
        photoDetailViewModel: PhotoDetailViewModel? = null,
    ) {
        val glideRequest = Glide.with(context)
            .load(url)
            .apply {
                if (centerCrop) centerCrop()
                if (crossFade) transition(DrawableTransitionOptions.withCrossFade())
            }

        if (photoDetailViewModel != null) {
            glideRequest.listener(object : RequestListener<Drawable> {
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
        }

        glideRequest.into(imageView)

    }

    fun loadGif(
        context: Context,
        resourceId: Int,
        imageView: ImageView,
        circleCrop: Boolean = false,
        centerCrop: Boolean = false,
        overrideWidth: Int? = null,
        overrideHeight: Int? = null
    ) {
        val glideRequest = Glide.with(context)
            .asGif()
            .load(resourceId)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        if (circleCrop) {
            glideRequest.circleCrop()
        }

        if (centerCrop) {
            glideRequest.centerCrop()
        }

        if (overrideWidth != null && overrideHeight != null) {
            glideRequest.override(overrideWidth, overrideHeight)
        }

        glideRequest.into(imageView)
    }
}