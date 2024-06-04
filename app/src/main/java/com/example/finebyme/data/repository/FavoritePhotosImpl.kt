package com.example.finebyme.data.repository

import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.db.PhotoDao

class FavoritePhotosImpl(private val photoDao: PhotoDao) : FavoritePhotosRepository {
    override fun getAllPhotos(): List<Photo> = photoDao.getAllPhotos()

    override fun getPhoto(id: Int): Photo = photoDao.getPhoto(id)

    override fun insertPhoto(photo: Photo) = photoDao.insert(photo)

    override fun deletePhoto(photo: Photo) = photoDao.delete(photo)

    override fun updatePhoto(photo: Photo) = photoDao.update(photo)
}