package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository

class GetFavoritePhotoListUseCase constructor(
    private val photoRepository: PhotoRepository
) {
    fun execute(): List<Photo> {
        return photoRepository.getFavoritePhotoList()
    }
}