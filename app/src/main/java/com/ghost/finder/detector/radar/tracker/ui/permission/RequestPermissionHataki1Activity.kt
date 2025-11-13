package com.ghost.finder.detector.radar.tracker.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.databinding.ActivityRequestPermissionHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.common.PermissionHelper
import com.ghost.finder.detector.radar.tracker.ui.start.StartHataki1Activity
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestPermissionHataki1Activity :
    BaseActivity<RequestPermissionViewModel, ActivityRequestPermissionHataki1Binding>() {
    private var nativeInterAdProducer: NativeBaseAdProducer? = null
    override fun onResumeImpl() {

    }

    private fun loadNativeAd() {
        nativeInterAdProducer = HKTAppAdvertiseManager.permissionNativeAdProducer ?: return
        nativeInterAdProducer!!.setListener(object : NativeAdListener {
            override fun onAdLoaded(isAutoLoad: Boolean) {
                super.onAdLoaded(isAutoLoad)
                nativeInterAdProducer!!.show(this@RequestPermissionHataki1Activity, R.layout.layout_native_ad_medium_button_bottom, binding.frAdBottom)
            }
        })

        nativeInterAdProducer!!.show(this, R.layout.layout_native_ad_medium_button_bottom, binding.frAdBottom)
    }
    private fun eventListenerHataki1() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAndRequestPermissionsHataki1()
            }
        }
        binding.btnGo.setOnClickListener {
            StartHataki1Activity.Companion.open(this)
        }

        binding.btnSave.setOnClickListener {
            StartHataki1Activity.Companion.open(this)
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
                Manifest.permission.CAMERA
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
        lifecycleScope.launch {
            delay(300)
            loadNativeAd()
            HKTAppAdvertiseManager.loadHomeNativeDialogAd(this@RequestPermissionHataki1Activity)
        }
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RequestPermissionHataki1Activity::class.java))
        }
    }
}