package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class GetSearchPhotoListUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    suspend fun execute(query: String): Result<List<Photo>> {
        return photoRepository.getSearchPhotoList(query)
    }
}