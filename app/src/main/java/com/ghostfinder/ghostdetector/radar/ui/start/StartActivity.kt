package com.ghostfinder.ghostdetector.radar.ui.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.ads.base.BaseRequestFullNativeActivity
import com.ghostfinder.ghostdetector.radar.databinding.ActivityStartBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.rada.RadaActivity
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseRequestFullNativeActivity<ActivityStartBinding, StartViewModel>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, StartActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_start

    override fun viewModelClass(): Class<StartViewModel> = StartViewModel::class.java

    override fun onCreateImpl() {
        binding.btnAccept.setOnClickListener {
            showInterAdsIfNeeded {
                RadaActivity.Companion.open(this@StartActivity)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            val params = binding.cornerOverlay.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 80, 0, 0)
            binding.cornerOverlay.layoutParams = params
        }
    }

    override fun onResumeImpl() {
        AppAdvertiseManager.showAdaptiveBanner(this, binding.frAdBottom)
    }

    private fun showInterAdsIfNeeded(callback: () -> Unit) {
        Log.d(TAG, "Event Requesting show Inter home")
        AppAdvertiseManager.homeInterstitialAdProducer?.let {
            it.setListener(listener = object : InterstitialAdListener {
                override fun onAdLoadFailed(isAutoPreload: Boolean) {
                    super.onAdLoadFailed(isAutoPreload)
                    Log.d(TAG, "Event call callback from onAdLoadFailed")
                    if (isAutoPreload.not()) {
                        callback()
                    }
                }

                override fun onAdFailedToShow() {
                    super.onAdFailedToShow()
                    callback()
                }

                override fun showNativeFullAd(ad: NativeBaseAdProducer) {
                    super.showNativeFullAd(ad)
                    showNativeFullScreenAd(ad, callback)
                }

                override fun requireActivityForLoadAndShowAd(): Activity? {
                    return this@StartActivity
                }

                override fun onNextAction() {
                    super.onNextAction()
                    Log.d(TAG, "Event call callback from onNextAction")
                    callback()
                }
            })

            it.show(this, lifecycle)
        } ?: run {
            Log.d(TAG, "inter is not existed, call callback")
            callback()
            return
        }
    }
}