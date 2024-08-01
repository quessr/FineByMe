package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class GetRandomPhotoListUseCase @Inject constructor (private val photoRepository: PhotoRepository) {
//    fun execute(onResult: (List<Photo>?) -> Unit) {
//        photoRepository.getRandomPhotoList(onResult)
//    }

    suspend fun execute() :Result<List<Photo>> {
        return photoRepository.getRandomPhotoList()
    }
}