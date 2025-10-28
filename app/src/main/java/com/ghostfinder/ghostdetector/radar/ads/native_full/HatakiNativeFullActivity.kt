package com.ghostfinder.ghostdetector.radar.ads.native_full

import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.databinding.ActivityHatakiFullNativeBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel


import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer


class HatakiNativeFullActivity : BaseActivity<BaseViewModel, ActivityHatakiFullNativeBinding>(
) {
    private var fullScreenNativeAdProducer: NativeBaseAdProducer? = null
    override fun onResume() {
        super.onResume()

        fullScreenNativeAdProducer = AppAdvertiseManager.currentNativeFullScreen ?: run {
            setResult(RESULT_OK)
            finish()
            return
        }
        fullScreenNativeAdProducer?.show(this, R.layout.layout_native_ad_large_button_top, binding.fullAdContainer)
    }

    override fun getLayoutResource(): Int = R.layout.activity_hataki_full_native

    override fun viewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun onCreateImpl() {
        binding.ivNativeFull.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onResumeImpl() {
    }
}
