package com.example.finebyme.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

//사용자가 즐겨찾기한 사진들
@Parcelize
@Entity(tableName = "favorite_photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val fullUrl: String,
    val thumbUrl: String,
) : Parcelable
