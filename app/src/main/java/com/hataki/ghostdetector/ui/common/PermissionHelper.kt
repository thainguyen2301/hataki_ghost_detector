package com.hataki.ghostdetector.ui.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    val REQ_CODE = 1001
    fun isAllPermissionGranted(context: Context): Boolean {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
        return permissions.all { perm ->
            ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int
    ): Boolean {
        val notGranted = permissions.filter { perm ->
            ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED
        }

        return if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, notGranted.toTypedArray(), requestCode)
            false
        } else {
            true
        }
    }
}