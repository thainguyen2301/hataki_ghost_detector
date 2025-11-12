package com.ghost.finder.detector.radar.tracker.ads.native_full

import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.databinding.ActivityHatakiFullNativeBinding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel


import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer

class HatakiNativeFullActivity : BaseActivity<BaseViewModel, ActivityHatakiFullNativeBinding>(
) {
    private var fullScreenNativeAdProducer: NativeBaseAdProducer? = null
    override fun onResume() {
        super.onResume()

        fullScreenNativeAdProducer = HKTAppAdvertiseManager.currentNativeFullScreen ?: run {
            setResult(RESULT_OK)
            finish()
            return
        }
        fullScreenNativeAdProducer?.show(
            this,
            R.layout.layout_native_ad_large_button_top,
            binding.fullAdContainer
        )
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
