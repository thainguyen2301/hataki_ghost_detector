package com.ghost.finder.detector.radar.tracker.ads.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.ads.native_full.HatakiNativeFullActivity
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import kotlin.jvm.java
import kotlin.let
import kotlin.run

abstract class BaseRequestFullNativeActivity<VM: BaseViewModel, VB : ViewBinding>(): BaseActivity<VM, VB>() {
    var callbackAfterNativeFullScreenShown: (()-> Unit)? = null
        private set
    private val secondActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            callbackAfterNativeFullScreenShown?.let {
                it()
            } ?: run {
                onNextFromNativeFullScreenAd()
            }
        }
    }
    fun showNativeFullScreenAd(ad: NativeBaseAdProducer, callback: (() -> Unit)? = null){
        HKTAppAdvertiseManager.currentNativeFullScreen = ad
        secondActivityResultLauncher.launch(Intent(this, HatakiNativeFullActivity::class.java))
        callbackAfterNativeFullScreenShown = callback
    }

    open fun onNextFromNativeFullScreenAd() {}
}