package com.ghost.finder.detector.radar.tracker.ui.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.ads.base.BaseRequestFullNativeActivity
import com.ghost.finder.detector.radar.tracker.ads.dialog.HKTNativeDialog
import com.ghost.finder.detector.radar.tracker.databinding.ActivityStartHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.rada.RadaHataki1Activity
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartHataki1Activity : BaseRequestFullNativeActivity<StartViewModel, ActivityStartHataki1Binding>() {

    override fun getLayoutResource(): Int = R.layout.activity_start_hataki_1

    override fun onCreateImpl() {
        binding.btnAccept.setOnClickListener {
            showInterAdsIfNeeded {
                RadaHataki1Activity.Companion.open(this@StartHataki1Activity)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            HKTAppAdvertiseManager.loadHomeInterstitialAd(this@StartHataki1Activity)
            showNativeDialog()
        }
    }

    private fun showInterAdsIfNeeded(callback: () -> Unit) {
        Log.d(TAG, "Event Requesting show Inter home")
        HKTAppAdvertiseManager.homeInterstitialAdProducer?.let {
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
                    return this@StartHataki1Activity
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

    private fun showNativeDialog() {
        val nativeDialogAd = HKTAppAdvertiseManager.homeNativeDialogAd ?: return
        val nativeDialog = HKTNativeDialog(this, nativeDialogAd, R.layout.layout_native_ad_medium_button_bottom)
        nativeDialog.setOnVisibilityListener(object : HKTNativeDialog.OnVisibilityListener {
            override fun onShow() {
                Log.d("NativeDialog", "Dialog is shown")
            }

            override fun onHide() {
                Log.d("NativeDialog", "Dialog is hidden")
                HKTAppAdvertiseManager.showAdaptiveHomeBanner(this@StartHataki1Activity, binding.frAdBottom)
                HKTAppAdvertiseManager.homeNativeDialogAd = null
            }
        })
        nativeDialog.show()
    }
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, StartHataki1Activity::class.java))
        }
    }

    override fun viewModelClass(): Class<StartViewModel> = StartViewModel::class.java

    override fun onResumeImpl() {

    }
}