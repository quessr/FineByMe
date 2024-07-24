package com.example.finebyme.data.datasource

import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.db.PhotoDao

class UserDataSource(private val photoDao: PhotoDao) {

    fun getAllPhotos(): List<Photo> = photoDao.getAllPhotos()

    fun getPhoto(id: String): Photo = photoDao.getPhoto(id)

    fun insertPhoto(photo: Photo) = photoDao.insert(photo)

    fun deletePhoto(photo: Photo) = photoDao.delete(photo)

    fun updatePhoto(photo: Photo) = photoDao.update(photo)

    // 사진이 즐겨찾기에 있는지 확인
    @Suppress("SENSELESS_COMPARISON")
    fun isPhotoFavorite(photoId: String): Boolean {
        return photoDao.getPhoto(photoId) != null
    }
}