package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository

class SetFavoritePhotoUseCase(private val photoRepository: PhotoRepository) {
    fun execute(photo: Photo, isSet: Boolean) {
        if (isSet) {
            photoRepository.addPhotoToFavorites(photo)
        } else {
            photoRepository.removePhotoFromFavorites(photo)
        }
    }
}