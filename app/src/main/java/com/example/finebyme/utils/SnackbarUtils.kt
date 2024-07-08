package com.example.finebyme.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.finebyme.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showSnackbar(context: Context, rootView: View, message: String, isError: Boolean) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        val snackbarView: View = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams

        if (!isError) {
            params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            snackbarView.layoutParams = params
        }

        val color = if (isError) {
            ContextCompat.getColor(context, R.color.error)
        } else {
            ContextCompat.getColor(context, R.color.black_40)
        }
        snackbar.setBackgroundTint(color)

        snackbar.show()
    }
}