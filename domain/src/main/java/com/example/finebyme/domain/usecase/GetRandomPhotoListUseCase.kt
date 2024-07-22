package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository

class GetRandomPhotoListUseCase(private val photoRepository: PhotoRepository) {
    fun execute(onResult: (List<Photo>?) -> Unit) {
        photoRepository.getRandomPhotoList(onResult)
    }
}