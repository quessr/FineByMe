package com.example.finebyme.presentation.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.enums.LoadingState
import com.example.finebyme.presentation.databinding.ActivityPhotoDetailBinding
import com.example.finebyme.presentation.databinding.FragmentPhotoListBinding

class LoadingHandler<T>(private val binding: T, private val context: Context) {
    fun setLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> showLoading()
            LoadingState.DONE -> hideLoading()
            LoadingState.ERROR -> hideLoading()
        }
    }

    private fun showLoading() {
        when (binding) {
            is ActivityPhotoDetailBinding -> {
                ImageLoader.loadGif(
                    context = binding.ivLoading.context,
                    resourceId = R.drawable.loading,
                    imageView = binding.ivLoading,
                    circleCrop = true,
                )

                binding.ivLoading.visibility = View.VISIBLE
                binding.ivPhoto.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black_40
                    )
                )
            }

            is FragmentPhotoListBinding -> {
                ImageLoader.loadGif(
                    context = binding.ivLoading.context,
                    resourceId = R.drawable.loading,
                    imageView = binding.ivLoading,
                    circleCrop = true,
                )

                binding.ivLoading.visibility = View.VISIBLE
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
            }
        }
    }

    private fun hideLoading() {
        when (binding) {
            is ActivityPhotoDetailBinding -> {
                binding.ivLoading.visibility = View.GONE
                binding.tvDownloading.visibility = View.GONE
                binding.ivFavorite.visibility = View.VISIBLE
                binding.chipDownload.visibility = View.VISIBLE
            }

            is FragmentPhotoListBinding -> {
                binding.ivLoading.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
            }
        }
    }
}