package com.example.finebyme.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.finebyme.R
import com.example.finebyme.common.enums.State
import com.example.finebyme.databinding.FragmentPhotoListBinding

class LoadingHandler(private val binding: FragmentPhotoListBinding, private val context: Context) {
    fun setLoadingState(state: State) {
        when (state) {
            State.LOADING -> showLoading()
            State.DONE -> hideLoading()
            State.ERROR -> hideLoading()
        }
    }

    private fun showLoading() {
        ImageLoader.loadGif(
            context = binding.imageViewLoading.context,
            resourceId = R.drawable.loading,
            imageView = binding.imageViewLoading
        )

        binding.imageViewLoading.visibility = View.VISIBLE
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
    }

    private fun hideLoading() {
        binding.imageViewLoading.visibility = View.GONE
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
    }
}