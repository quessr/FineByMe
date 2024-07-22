package com.example.finebyme.domain.entity

data class Photo(
    var id: String,
    var title: String,
    val description: String?,
    val fullUrl: String,
    val thumbUrl: String,
)
