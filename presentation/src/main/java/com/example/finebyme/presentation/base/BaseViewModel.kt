package com.example.finebyme.presentation.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _photoHeights = mutableMapOf<Int, Int>()

    fun getPhotoHeight(position: Int): Int {
        return _photoHeights[position] ?: generateRandomHeight().also {
            _photoHeights[position] = it
        }
    }

    private fun generateRandomHeight(): Int {
        val minHeight = 100
        val maxHeight = 300
        return (minHeight..maxHeight).random()
    }
}



