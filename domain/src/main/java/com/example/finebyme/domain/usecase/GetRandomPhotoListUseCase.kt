package com.example.finebyme.domain.usecase

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository
import javax.inject.Inject

class GetRandomPhotoListUseCase @Inject constructor (private val photoRepository: PhotoRepository) {
//    fun execute(onResult: (List<Photo>?) -> Unit) {
//        photoRepository.getRandomPhotoList(onResult)
//    }

    // 사용자는 랜덤 포토 리스트를 원한다.
    // 성공하면 Photo 타입의 리스트를 반환하고,
    // 실패하면 실패메시지를 전달받는다.
    fun execute(onResult: (Result<List<Photo>>) -> Unit) {
        photoRepository.getRandomPhotoList(onResult)
    }

}