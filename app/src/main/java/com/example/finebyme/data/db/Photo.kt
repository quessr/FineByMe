package com.example.finebyme.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

//사용자가 즐겨찾기한 사진들
@Entity(tableName = "favorite_photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val fullUrl: String,
    val thumbUrl: String,
)
