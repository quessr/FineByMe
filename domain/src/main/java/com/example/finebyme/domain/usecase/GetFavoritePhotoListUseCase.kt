package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePhotoListUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    fun execute(): Flow<List<Photo>> {
        return photoRepository.getFavoritePhotoList()
    }
}