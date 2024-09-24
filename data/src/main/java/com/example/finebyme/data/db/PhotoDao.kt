package com.example.finebyme.data.db

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    //    @Query("SELECT * FROM favorite_photos ORDER BY inputAt DESC")
//    fun getAllPhotos(): List<Photo>
    @Query("SELECT * FROM favorite_photos ORDER BY inputAt DESC")
    fun getAllPhotos(): List<Photo>

    @Query("SELECT * FROM favorite_photos WHERE id = :id LIMIT 1")
    fun getPhoto(id: String): Photo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photo: Photo): Long

    @Update
    fun update(photo: Photo): Int

    @Delete
    fun delete(photo: Photo)

    @Query("DELETE FROM favorite_photos")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM favorite_photos WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("SELECT * FROM favorite_photos")
    fun getAllPhotoCursor(): Cursor?

    @Query("SELECT * FROM favorite_photos WHERE id = :id")
    fun getPhotoByIdCursor(id: String): Cursor?

}