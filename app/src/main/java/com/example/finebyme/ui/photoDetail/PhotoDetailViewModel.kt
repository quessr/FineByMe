package com.example.finebyme.ui.photoDetail

import androidx.lifecycle.ViewModel

class PhotoDetailViewModel : ViewModel() {
    fun transformTitle(title: String): String {
        return title.replace("-", " ")
            .split(" ")
            .dropLast(1)
            .joinToString(" ")
    }
}