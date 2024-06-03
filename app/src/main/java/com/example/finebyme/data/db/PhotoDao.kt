package com.example.finebyme.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    @Query("SELECT * from favorite_photos ORDER BY title ASC")
    fun getAllPhotos(): List<Photo>

    @Query("SELECT * from favorite_photos WHERE id = :id")
    fun getPhoto(id: Int): Photo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)

}