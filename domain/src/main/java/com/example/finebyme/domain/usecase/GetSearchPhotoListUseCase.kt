package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository

class GetSearchPhotoListUseCase(private val photoRepository: PhotoRepository) {
    fun execute(query: String, onResult: (List<Photo>?) -> Unit) {
        photoRepository.getSearchPhotoList(query, onResult)
    }
}