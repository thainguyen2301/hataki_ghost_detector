package com.ghostfinder.ghostdetector.radar.ui.welcome

import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.databinding.ActivityHatakiWelcomeBackBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer

class HatakiWelcomeBackActivity : BaseActivity<BaseViewModel, ActivityHatakiWelcomeBackBinding>() {
    private var welcomeBackNativeAdProducer: NativeBaseAdProducer? = null

    fun initAd() {
        welcomeBackNativeAdProducer?.setListener(listener = object : NativeAdListener {
            override fun onAdLoaded(isAutoLoad: Boolean) {
                super.onAdLoaded(isAutoLoad)
                welcomeBackNativeAdProducer?.show(this@HatakiWelcomeBackActivity, R.layout.layout_native_ad_small_button_bottom, binding.frAd)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        welcomeBackNativeAdProducer?.show(this, R.layout.layout_native_ad_small_button_bottom, binding.frAd)
    }

    override fun getLayoutResource(): Int = R.layout.activity_hataki_welcome_back

    override fun viewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun onCreateImpl() {
        binding.tvContinue.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        AppAdvertiseManager.loadInterOpenResumeAd(this)
        AppAdvertiseManager.loadWelcomeBackNativeAds(context = this)
        welcomeBackNativeAdProducer = AppAdvertiseManager.welcomeBackNativeAdProducer

        initAd()
    }

    override fun onResumeImpl() {
    }
}