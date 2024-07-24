package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class CheckFavoritePhotoUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    fun execute(photoId: String): Boolean {
        return photoRepository.isPhotoFavorite(photoId)
    }
}