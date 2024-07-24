package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class GetSearchPhotoListUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    fun execute(query: String, onResult: (Result<List<Photo>>) -> Unit) {
        photoRepository.getSearchPhotoList(query, onResult)
    }
}