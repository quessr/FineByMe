package com.example.finebyme.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    var id: String,
    var title: String,
    val description: String?,
    val fullUrl: String,
    val thumbUrl: String,
) : Parcelable
