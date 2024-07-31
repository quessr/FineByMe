package com.example.finebyme.data.repository

import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.repositoryInterface.PhotoRepository

class PhotoRepositoryFake : PhotoRepository {
//    override fun getRandomPhotoList(onResult: (Result<List<Photo>>) -> Unit) {
//        val image = "https://www.blockchaintoday.co.kr/news/photo/202304/31348_35502_138.jpg"
//        val dummy = listOf(
//            Photo(
//                id = "0",
//                title = "test0",
//                description = "description0",
//                fullUrl = image,
//                thumbUrl = image
//            ),
//            Photo(
//                id = "1",
//                title = "test1",
//                description = "description1",
//                fullUrl = image,
//                thumbUrl = image
//            ),
//            Photo(
//                id = "2",
//                title = "test2",
//                description = "description2",
//                fullUrl = image,
//                thumbUrl = image
//            ),
//            Photo(
//                id = "3",
//                title = "test3",
//                description = "description3",
//                fullUrl = image,
//                thumbUrl = image
//            ),
//            Photo(
//                id = "4",
//                title = "test4",
//                description = "description4",
//                fullUrl = image,
//                thumbUrl = image
//            ),
//            Photo(
//                id = "5",
//                title = "test5",
//                description = "description5",
//                fullUrl = image,
//                thumbUrl = image
//            )
//        )
//
//        onResult(Result.success(dummy))
//    }
//
//    override fun getSearchPhotoList(query: String, onResult: (Result<List<Photo>>) -> Unit) {
//    }

    override suspend fun getRandomPhotoList(): Result<List<Photo>> {
        val image = "https://www.blockchaintoday.co.kr/news/photo/202304/31348_35502_138.jpg"
        val dummy = listOf(
            Photo(
                id = "0",
                title = "test0",
                description = "description0",
                fullUrl = image,
                thumbUrl = image
            ),
            Photo(
                id = "1",
                title = "test1",
                description = "description1",
                fullUrl = image,
                thumbUrl = image
            ),
            Photo(
                id = "2",
                title = "test2",
                description = "description2",
                fullUrl = image,
                thumbUrl = image
            ),
            Photo(
                id = "3",
                title = "test3",
                description = "description3",
                fullUrl = image,
                thumbUrl = image
            ),
            Photo(
                id = "4",
                title = "test4",
                description = "description4",
                fullUrl = image,
                thumbUrl = image
            ),
            Photo(
                id = "5",
                title = "test5",
                description = "description5",
                fullUrl = image,
                thumbUrl = image
            )
        )
        return Result.success(dummy)
    }

    override suspend fun getSearchPhotoList(query: String): Result<List<Photo>> {
        TODO("Not yet implemented")
    }

    override fun getFavoritePhotoList(): List<Photo> {
        return emptyList()
    }

    override fun addPhotoToFavorites(photo: Photo) {
    }

    override fun removePhotoFromFavorites(photo: Photo) {
    }

    override fun isPhotoFavorite(photoId: String): Boolean {
        return true
    }
}