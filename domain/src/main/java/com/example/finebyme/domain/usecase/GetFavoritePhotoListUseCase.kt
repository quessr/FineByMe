package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class GetFavoritePhotoListUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    fun execute(): List<Photo> {
        return photoRepository.getFavoritePhotoList()
    }
}