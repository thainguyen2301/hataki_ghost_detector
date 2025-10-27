package com.ghostfinder.ghostdetector.radar.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.databinding.ActivityRequestPermissionBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.common.PermissionHelper
import com.ghostfinder.ghostdetector.radar.ui.start.StartActivity

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            binding.parent.setPadding(0,80,0,0)
        }
    }

    private fun eventListener() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAndRequestPermissions()
            }
        }
        binding.btnGo.setOnClickListener {
            StartActivity.Companion.open(this)
        }

        binding.btnSave.setOnClickListener {
            StartActivity.Companion.open(this)
        }
    }

    private fun updateUIPermissionStatus(isAllGranted: Boolean) {
        if (isAllGranted) {
            binding.customSwitch.isChecked = true
            binding.btnSave.visibility = View.VISIBLE
            binding.btnGo.text = resources.getString(R.string.permission_continue)
        } else {
            binding.customSwitch.isChecked = false
            binding.btnSave.visibility = View.GONE
            binding.btnGo.text = resources.getString(R.string.continue_without_permission)
        }
    }

    private fun checkAndRequestPermissions() {
        val allGranted = PermissionHelper.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA
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