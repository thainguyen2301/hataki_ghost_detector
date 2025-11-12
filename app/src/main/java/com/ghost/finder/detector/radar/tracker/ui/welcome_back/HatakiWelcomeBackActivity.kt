package com.ghost.finder.detector.radar.tracker.ui.welcome_back

import android.util.Log
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.databinding.ActivityHatakiWelcomeBackBinding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel
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
        HKTAppAdvertiseManager.loadInterOpenResumeAd(this)
        HKTAppAdvertiseManager.loadWelcomeBackNativeAds(context = this)
        welcomeBackNativeAdProducer =HKTAppAdvertiseManager.welcomeBackNativeAdProducer

        initAd()
    }

    override fun onResumeImpl() {

    }
}