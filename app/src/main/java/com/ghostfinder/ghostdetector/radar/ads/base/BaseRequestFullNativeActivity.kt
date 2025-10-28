package com.ghostfinder.ghostdetector.radar.ads.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.ads.native_full.HatakiNativeFullActivity
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import kotlin.jvm.java
import kotlin.let
import kotlin.run

abstract class BaseRequestFullNativeActivity<VB : ViewBinding, V: BaseViewModel>(): BaseActivity<V, VB>() {
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
        AppAdvertiseManager.currentNativeFullScreen = ad
        secondActivityResultLauncher.launch(Intent(this, HatakiNativeFullActivity::class.java))
        callbackAfterNativeFullScreenShown = callback

    }

    open fun onNextFromNativeFullScreenAd() {}
}