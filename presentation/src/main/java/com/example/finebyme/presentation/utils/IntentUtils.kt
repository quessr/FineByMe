package com.example.finebyme.presentation.utils

import android.content.Context
import android.content.Intent
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.photoDetail.PhotoDetailActivity

object IntentUtils {
    private const val ARG_PHOTO = "photo"
    fun newPhotoDetail(context: Context, photo: Photo): Intent {
        return Intent(context, PhotoDetailActivity::class.java).apply {
             putExtra(ARG_PHOTO, photo)
        }
    }
}