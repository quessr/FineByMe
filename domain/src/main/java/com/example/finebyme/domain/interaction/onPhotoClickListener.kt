package com.example.finebyme.domain.interaction

import com.example.finebyme.domain.entity.Photo

interface OnPhotoClickListener {
    fun onPhotoClick(photo: Photo)
}