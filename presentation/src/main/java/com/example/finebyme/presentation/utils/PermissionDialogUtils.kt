package com.example.finebyme.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

object PermissionDialogUtils {
    fun showPermissionDeniedDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("권한이 필요합니다")
            .setMessage("이미지 다운로드를 위해 저장소 접근 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}