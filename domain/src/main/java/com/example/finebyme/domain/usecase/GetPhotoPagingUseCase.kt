package com.example.finebyme.domain.usecase

import androidx.paging.PagingData
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoPagingUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    operator fun invoke(query: String): Flow<PagingData<Photo>> {
        return photoRepository.getPhotoPagingList(query)
    }
}