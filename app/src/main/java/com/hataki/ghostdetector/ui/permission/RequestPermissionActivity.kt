package com.hataki.ghostdetector.ui.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityRequestPermissionBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.PermissionHelper
import com.hataki.ghostdetector.ui.start.StartActivity

class RequestPermissionActivity :
    BaseActivity<RequestPermissionViewModel, ActivityRequestPermissionBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RequestPermissionActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_request_permission

    override fun viewModelClass(): Class<RequestPermissionViewModel> =
        RequestPermissionViewModel::class.java

    override fun onCreateImpl() {
        eventListener()
    }

    private fun eventListener() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAndRequestPermissions()
            }
        }
        binding.btnGo.setOnClickListener {
            StartActivity.open(this)
        }
    }

    private fun updateUIPermissionStatus(isAllGranted: Boolean) {
        if (isAllGranted) {
            binding.customSwitch.isChecked = true
            binding.btnGo.text = resources.getString(R.string.permission_continue)
        } else {
            binding.customSwitch.isChecked = false
            binding.btnGo.text = resources.getString(R.string.continue_without_permission)
        }
    }

    private fun checkAndRequestPermissions() {
        val allGranted = PermissionHelper.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA
            ),
            PermissionHelper.REQ_CODE
        )
        updateUIPermissionStatus(allGranted)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateUIPermissionStatus(true)
            } else {
                updateUIPermissionStatus(false)
            }
        }
    }

    override fun onResumeImpl() {
    }
}