package com.example.finebyme.presentation.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun checkAndRequestImagePermission(
        activity: Activity,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else android.Manifest.permission.READ_EXTERNAL_STORAGE

        val granted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), 100)
            onDenied()
        }
    }
}