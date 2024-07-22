package com.example.finebyme.presentation.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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



