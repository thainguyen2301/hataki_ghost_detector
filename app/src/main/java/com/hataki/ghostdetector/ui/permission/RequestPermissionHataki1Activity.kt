package com.hataki.ghostdetector.ui.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityRequestPermissionHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.PermissionHelper
import com.hataki.ghostdetector.ui.start.StartHataki1Activity

class RequestPermissionHataki1Activity :
    BaseActivity<RequestPermissionViewModel, ActivityRequestPermissionHataki1Binding>() {

    override fun onResumeImpl() {
    }

    private fun eventListenerHataki1() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAndRequestPermissionsHataki1()
            }
        }
        binding.btnGo.setOnClickListener {
            StartHataki1Activity.open(this)
        }

        binding.btnSave.setOnClickListener {
            StartHataki1Activity.open(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateUIPermissionStatusHataki1(true)
            } else {
                updateUIPermissionStatusHataki1(false)
            }
        }
    }

    private fun updateUIPermissionStatusHataki1(isAllGranted: Boolean) {
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

    private fun checkAndRequestPermissionsHataki1() {
        val allGranted = PermissionHelper.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA
            ),
            PermissionHelper.REQ_CODE
        )
        updateUIPermissionStatusHataki1(allGranted)
    }

    override fun getLayoutResource(): Int = R.layout.activity_request_permission_hataki_1

    override fun viewModelClass(): Class<RequestPermissionViewModel> =
        RequestPermissionViewModel::class.java

    override fun onCreateImpl() {
        eventListenerHataki1()
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RequestPermissionHataki1Activity::class.java))
        }
    }
}